package com.company;

import java.util.ArrayList;
import java.util.List;

public class ServiceTElement {
    private String name;
    private int brandID;
    private int categoryID;
    private int typeID;

    public ServiceTElement() {
    }

    public ServiceTElement(int brandID, String name, int categoryID, int typeID) {
        this.name = name;
        this.brandID = brandID;
        this.categoryID = categoryID;
        this.typeID = typeID;
    }

    public String getModelName() { return name; }

    public int getBrandID() {
        return brandID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getTypeID() {
        return typeID;
    }



    @Override
    public int hashCode() {
        return Integer.valueOf(name) + brandID + categoryID + typeID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        ServiceTElement other = (ServiceTElement) obj;
        if (!this.name.equals(other.name)) return false;
        if (this.brandID != other.brandID) return false;
        if (this.categoryID != other.categoryID) return false;
        if (this.typeID != other.typeID) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-15s%-4s%-4s%-4s",name,brandID,categoryID,typeID);
    }
}
