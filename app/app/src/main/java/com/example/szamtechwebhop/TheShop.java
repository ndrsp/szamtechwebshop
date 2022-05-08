package com.example.szamtechwebhop;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TheShop extends AppCompatActivity {  //Ez a class maga a shop
    private static final String LOG_TAG = TheShop.class.getName();
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<ShoppingItem> mItemList;
    private ArrayList<ShoppingItem> mCartList;
    private com.example.szamtechwebhop.ShoppingItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private CollectionReference mCarted_Items;
    private NotificationHandler mNotificationHandler;




    private int gridNumber = 1;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_shop);

        user = FirebaseAuth.getInstance().getCurrentUser(); //Bekéri a usert

        if(user == null){       //Ha nem hiteles, akkor logout
            Log.d(LOG_TAG, "Nem hitelesített!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        mCartList = new ArrayList<>();

        mAdapter = new com.example.szamtechwebhop.ShoppingItemAdapter(this, mItemList);

        mRecyclerView.setAdapter(mAdapter);


        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        mCarted_Items = mFirestore.collection("Cart");

        mNotificationHandler = new NotificationHandler(this);



        queryData();



    }
        private void queryData() {
            String[] itemList = getResources().getStringArray(R.array.shopping_item_names); //Adatok lekérése
            String[] itemsInfo = getResources().getStringArray(R.array.shopping_item_desc);
            String[] itemsPrice = getResources().getStringArray(R.array.shopping_item_price);

            TypedArray itemsImageRes = getResources().obtainTypedArray(R.array.shopping_item_images);
            TypedArray itemRate = getResources().obtainTypedArray(R.array.shopping_item_rates);

            mItemList.clear();

            mItems.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {  //Adatok lekérése és megjelenítése az adatbázisból
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    ShoppingItem item = document.toObject((ShoppingItem.class));
                    item.setId(document.getId());
                    mItemList.add(item);


                    if(item.getCartedCount() > 0){


                        for(int i = 0; i < itemList.length; i++) {
                            if(itemList[i].equals(item.getName())) {
                                mCarted_Items.add(new ShoppingItem(itemList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0), itemsImageRes.getResourceId(i, 0), 0));
                                mItems.document(item._getId()).update("cartedCount", item.getCartedCount() - 1);
                            }

                        }





                    }

                }
                if(mItemList.size() == 0){ //Ellenőrzi, hogy van e már adat
                    initializeData();
                    queryData();

                }

                mAdapter.notifyDataSetChanged();
            });

        }







    private void initializeData() {
        String[] itemList = getResources().getStringArray(R.array.shopping_item_names); //Adatok lekérése
        String[] itemsInfo = getResources().getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources().getStringArray(R.array.shopping_item_price);

        TypedArray itemsImageRes = getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemRate = getResources().obtainTypedArray(R.array.shopping_item_rates);



        for(int i = 0; i < itemList.length; i++) {
            mItems.add(new ShoppingItem(itemList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0), itemsImageRes.getResourceId(i, 0), 0));

        }




            //Adatok feltöltése a firestore adatbázisba


        itemsImageRes.recycle();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Menüsáv hozzárendelése a shophoz
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sav, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){       //A menüsávban mi történjen ha pl a logoutra vagy a kosárra kattintok
        switch (item.getItemId()){
            case R.id.logout:
                Log.d(LOG_TAG, "Kijelentkezés!");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(TheShop.this,"Sikeres kijelentkezés!",Toast.LENGTH_LONG).show(); //Toast(kiírja) ha valaki kijelentkezik
                finish();
                return true;

            case R.id.cart:
                Intent gotocart = new Intent(this,ShoppingCart.class);
                startActivity(gotocart);
                return true;

            case R.id.order:
                Toast.makeText(TheShop.this,"Sikeres megrendelés!",Toast.LENGTH_LONG).show();
                cartItems = 0;
                countTextView.setText("0");

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {                            //Kosár ikonhoz
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }



    public void updateAlertIcon(ShoppingItem item){      //Kosár tartalmához ha hozzáadunk egy elemet, akkor a piros pötty a kosár felett +1 értéket kap és megjelenítődik
        cartItems = (cartItems + 1);
        if (cartItems > 0){
            countTextView.setText(String.valueOf(cartItems));

        }else{
            countTextView.setText("");

        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);

        mItems.document(item._getId()).update("cartedCount", item.getCartedCount() + 1)
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Nem lehetséges!", Toast.LENGTH_LONG).show();
                });

        mNotificationHandler.send(item.getName());
        queryData();




    }



}