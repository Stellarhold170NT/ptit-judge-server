package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ProductY", namespace = "http://ptit.vn/judge/soap/object")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapProductY {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private String name;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object")
    private double price;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object")
    private double taxRate;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object")
    private double discount;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object")
    private double finalPrice;

    public SoapProductY() {}

    public static SoapProductY from(vn.ptit.judge.model.ProductY model) {
        SoapProductY sp = new SoapProductY();
        sp.name = model.getName();
        sp.price = model.getPrice();
        sp.taxRate = model.getTaxRate();
        sp.discount = model.getDiscount();
        sp.finalPrice = model.getFinalPrice();
        return sp;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }
}
