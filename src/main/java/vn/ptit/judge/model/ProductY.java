package vn.ptit.judge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductY {
    private String name;
    private double price;
    private double taxRate;
    private double discount;
    private double finalPrice;

    public ProductY() {
    }

    @JsonCreator
    public ProductY(
            @JsonProperty("name") String name,
            @JsonProperty("price") double price,
            @JsonProperty("taxRate") double taxRate,
            @JsonProperty("discount") double discount,
            @JsonProperty("finalPrice") double finalPrice) {
        this.name = name;
        this.price = price;
        this.taxRate = taxRate;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
