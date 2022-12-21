package entity;

import util.Currencies;

public class Company {
    private String name;
    private String address;
    private String currency;
    private int salePercent;

    public Company(String name, String address, int salePercent, String currency) {
        this.name = name;
        this.address = address;
        this.salePercent = salePercent;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getSalePercent() {
        return salePercent;
    }

    public void setSalePercent(int salePercent) {
        this.salePercent = salePercent;
    }
}
