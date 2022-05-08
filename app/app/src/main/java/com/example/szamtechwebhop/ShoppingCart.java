package com.example.szamtechwebhop;

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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShoppingCart extends AppCompatActivity {  //ShoppingCart
    private static final String LOG_TAG = ShoppingCart.class.getName();
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<com.example.szamtechwebhop.ShoppingItem> mItemList;

    private com.example.szamtechwebhop.ShoppingItemAdapterForShoppingCart mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;




    private int gridNumber = 1;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        mRecyclerView = findViewById(R.id.recycleViewShoppingCart);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new com.example.szamtechwebhop.ShoppingItemAdapterForShoppingCart(this, mItemList);

        mRecyclerView.setAdapter(mAdapter);


        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Cart");



        queryData();



    }
    private void queryData() {
        mItemList.clear();

        mItems.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {  //Adatok lekérése és megjelenítése az adatbázisból
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                com.example.szamtechwebhop.ShoppingItem item = document.toObject((com.example.szamtechwebhop.ShoppingItem.class));
                item.setId(document.getId());
                mItemList.add(item);



            }
            if(mItemList.size() == 0){ //Ellenőrzi, hogy van e már adat
                queryData();

            }

            mAdapter.notifyDataSetChanged();
        });

    }




    public void deleteItem(com.example.szamtechwebhop.ShoppingItem item) { //Elem törlése a kosárból
        DocumentReference ref = mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Sikeres törlés: " + item._getId());
                    if (cartItems > 0) {
                        cartItems -= 1;
                        countTextView.setText(String.valueOf(cartItems));
                    }
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " Sikertelen törlés!", Toast.LENGTH_LONG).show();
                });

        queryData();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Menüsáv hozzárendelése a shophoz (activity_the_shop)
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sav, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){       //A menüsávban mi történjen ha pl a logoutra vagy a kosárra kattintok
        switch (item.getItemId()){

            case R.id.order:
                Toast.makeText(ShoppingCart.this,"Sikeres megrendelés!",Toast.LENGTH_LONG).show();
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





}