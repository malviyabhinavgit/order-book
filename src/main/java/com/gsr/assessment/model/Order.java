package com.gsr.assessment.model;

public class Order {
    private  Float price;
    private  Float size;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Order(Float price, Float size) {
        this.price = price;
        this.size = size;
    }
}
