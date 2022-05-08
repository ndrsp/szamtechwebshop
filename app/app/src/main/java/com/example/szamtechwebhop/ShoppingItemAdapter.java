package com.example.szamtechwebhop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.VH> implements Filterable {

    private ArrayList<ShoppingItem> mShoppingItemsData;
    private ArrayList<ShoppingItem> mShoppingItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData){
        this.mShoppingItemsData = itemsData;
        this.mShoppingItemsDataAll = itemsData;
        this.mContext = context;

    }





    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewHolder
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.list_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ShoppingItem currentItem = mShoppingItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide);
            Animation animation2 = AnimationUtils.loadAnimation(mContext,R.anim.roll);
            holder.itemView.startAnimation(animation);
            holder.itemView.startAnimation(animation2);
            lastPosition = holder.getAdapterPosition();
        }


    }

    @Override
    public int getItemCount() {
        return mShoppingItemsData.size();
    }
    @Override
    public Filter getFilter() {
        return null;
    }


    class VH extends RecyclerView.ViewHolder{

        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;

        public VH(View itemView){  // layouthoz bindolás
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mPriceText = itemView.findViewById(R.id.price);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mRatingBar = itemView.findViewById(R.id.ratingBar);



        }

        public void bindTo(ShoppingItem currentItem) {

            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());
            mRatingBar.setRating(currentItem.getRate());

            Glide.with(mContext).load(currentItem.getImageRes()).into(mItemImage);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(view -> ((TheShop)mContext).updateAlertIcon(currentItem));

        }
    }
}





