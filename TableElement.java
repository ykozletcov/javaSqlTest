package com.company;

public class TableElement {
    protected int id;
    protected String name;

    public TableElement() {
    }

    public TableElement (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%-4s%-15s",id,name);
    }
}
