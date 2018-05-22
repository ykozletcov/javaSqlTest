package com.company;

import java.util.ArrayList;

public class List_Singleton {
    private static List_Singleton instance;
    private static ArrayList<TableElement> modelsT = new ArrayList<>();
    private static ArrayList<TableElement> brandsT = new ArrayList<>();
    private static ArrayList<TableElement> categoriesT = new ArrayList<>();
    private static ArrayList<TableElement> typesT = new ArrayList<>();
    private static ArrayList<MainTElement> mainT = new ArrayList<>();
    private static ArrayList<ServiceTElement> servT = new ArrayList<>();
    private static ArrayList<ServiceTElement> searchL = new ArrayList<>();
    private static ArrayList<journalRecord> journal = new ArrayList<>();

    private List_Singleton() {

    }

    public static List_Singleton getInstance() {
        if (instance == null)
            instance = new List_Singleton();
        return instance;
    }

    public void setModelsT(ArrayList<TableElement> modelsT) {
        List_Singleton.modelsT = modelsT;
    }

    public static void setBrandsT(ArrayList<TableElement> brandsT) {
        List_Singleton.brandsT = brandsT;
    }

    public static void setCategoriesT(ArrayList<TableElement> categoriesT) {
        List_Singleton.categoriesT = categoriesT;
    }

    public static void setTypesT(ArrayList<TableElement> typesT) {
        List_Singleton.typesT = typesT;
    }

    public static void setMainT(ArrayList<MainTElement> mainT) {
        List_Singleton.mainT = mainT;
    }

    public static void setServT(ArrayList<ServiceTElement> servT) {
        List_Singleton.servT = servT;
    }

    public static void setSearchL(ArrayList<ServiceTElement> searchL) {
        List_Singleton.searchL = searchL;
    }

    public static void setJournal(ArrayList<journalRecord> journal) {
        List_Singleton.journal = journal;
    }
}
