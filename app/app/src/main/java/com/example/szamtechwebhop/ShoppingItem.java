package com.example.szamtechwebhop;

public class ShoppingItem {

    private String id;
    private String name;
    private String info;
    private String price;
    private float rate;
    private int imageRes;
    private int cartedCount;

    public ShoppingItem(){

    }


    public ShoppingItem(String name, String info, String price, float rate, int imageRes, int cartedCount) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rate = rate;
        this.imageRes = imageRes;
        this.cartedCount = cartedCount;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public float getRate() {
        return rate;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setCartedCount(int n){this.cartedCount = n;}
    public int getCartedCount() {
        return this.cartedCount;
    }


    public String _getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


}
