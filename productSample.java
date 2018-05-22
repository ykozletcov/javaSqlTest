package com.company;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//@XmlRootElement(name = "model")
@XmlType(propOrder = {"brandName", "modelName", "prodCategoryName", "prodTypeName", "qnty"})
public class productSample {
    private String brandName;
    private String modelName;
    private String prodCategoryName;
    private String prodTypeName;
    private int qnty;

    public productSample() {

    }

    public productSample(String brandName, String modelName, String prodCategoryName, String prodTypeName, int qnty) {
        this.brandName = brandName;
        this.modelName = modelName;
        this.prodCategoryName = prodCategoryName;
        this.prodTypeName = prodTypeName;
        this.qnty = qnty;
    }
@XmlElement(name = "brand")
    public String getBrandName() {
        return brandName;
    }
    @XmlElement(name = "name")
    public String getModelName() {
        return modelName;
    }
    @XmlElement(name = "category")
    public String getProdCategoryName() {
        return prodCategoryName;
    }
    @XmlElement(name = "type")
    public String getProdTypeName() {
        return prodTypeName;
    }
    @XmlElement(name = "quantity")
    public int getQnty() {
        return qnty;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setProdCategoryName(String prodCategoryName) {
        this.prodCategoryName = prodCategoryName;
    }

    public void setProdTypeName(String prodTypeName) {
        this.prodTypeName = prodTypeName;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    @Override
    public String toString() {
        return String.format("%-15s%-25s%-25s%-25s%-4s",brandName,modelName,prodCategoryName,prodTypeName,qnty);
    }
}
