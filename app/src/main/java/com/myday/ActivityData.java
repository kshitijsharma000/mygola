package com.myday;

import java.io.Serializable;

/**
 * Created by Kshitij on 11/7/2015.
 */
public class ActivityData implements Serializable{
    String name;
    String image_url;
    Float actual_price;
    Float discount;
    Float rating;
    String city;
    String location;
    String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Float getActual_price() {
        return actual_price;
    }

    public void setActual_price(Float actual_price) {
        this.actual_price = actual_price;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
