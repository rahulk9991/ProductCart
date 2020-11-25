package com.productscart;

import com.google.gson.annotations.SerializedName;

public class GetAllProductsPojo {


    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @SerializedName("price")
    private String price;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("description")
    private String description;

    @SerializedName("Image")
    private String Image;


    public GetAllProductsPojo()
    {

    }
}



