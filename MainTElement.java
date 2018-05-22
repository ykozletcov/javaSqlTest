package com.company;

public class MainTElement extends TableElement {
    private String brand;
    private String category;
    private String type;
    private int qnty;

    public MainTElement() {
    }

    public MainTElement(int id, String brand, String name, String category, String type, int qnty) {
        super(id, name);
        this.brand = brand;
        this.category = category;
        this.type = type;
        this.qnty = qnty;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public int getQnty() {
        return qnty;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        MainTElement other = (MainTElement) obj;
        if (this.id!=other.id) return false;
        if (!this.brand.equals(other.brand)) return false;
        if (!this.name.equals(other.name)) return false;
        if (!this.category.equals(other.category)) return false;
        if (!this.type.equals(other.type)) return false;
        if (this.qnty!=other.qnty) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-4s%-15s%-25s%-25s%-25s%-4s",id,brand,name,category,type,qnty);
    }
}
