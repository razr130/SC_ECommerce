package com.example.sc_ecommerce.Model;

public class Cart {

    private String product_id, product_name, discount, product_price, quantity;

    public Cart() {
    }

    public Cart(String product_id, String product_name, String discount, String product_price, String quantity) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.discount = discount;
        this.product_price = product_price;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
