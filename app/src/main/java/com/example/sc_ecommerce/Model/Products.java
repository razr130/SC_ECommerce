package com.example.sc_ecommerce.Model;

public class Products {

    private String product_name, product_desc, product_price, product_image, product_category, product_id, product_time, product_date;

    public Products()
    {

    }

    public Products(String product_name, String product_desc, String product_price, String product_image, String product_category, String product_id, String product_time, String product_date) {
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_category = product_category;
        this.product_id = product_id;
        this.product_time = product_time;
        this.product_date = product_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_time() {
        return product_time;
    }

    public void setProduct_time(String product_time) {
        this.product_time = product_time;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }
}
