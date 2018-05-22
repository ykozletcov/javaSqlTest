package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class List_Ops {
    private static List_Ops instance;
    private static ArrayList<TableElement> modelsT = new ArrayList<>();
    private static ArrayList<TableElement> brandsT = new ArrayList<>();
    private static ArrayList<TableElement> categoriesT = new ArrayList<>();
    private static ArrayList<TableElement> typesT = new ArrayList<>();
    private static ArrayList<MainTElement> mainT = new ArrayList<>();
    private static ArrayList<ServiceTElement> servT = new ArrayList<>();
    private static ArrayList<ServiceTElement> searchL = new ArrayList<>();
    private static ArrayList<journalRecord> journal = new ArrayList<>();

    private List_Ops() {
        modelsT = SQL_Ops.getColumnFromDB("models");
        brandsT = SQL_Ops.getColumnFromDB("brand");
        categoriesT = SQL_Ops.getColumnFromDB("productCategory");
        typesT = SQL_Ops.getColumnFromDB("productType");
        mainT = SQL_Ops.getFromDBwithParams();
        servT = SQL_Ops.getServiceListFromDB();
        journal = SQL_Ops.getJournalfromDB();
    }

    static List_Ops getInstance(SQL_Ops sqlInstance) {

        if (instance == null && sqlInstance != null)
            instance = new List_Ops();
        return instance;
    }

    static void updateT(int tableIndex) {
        switch (tableIndex) {
            case 1:
                brandsT = SQL_Ops.getColumnFromDB("brand");
                break;
            case 2:
                categoriesT = SQL_Ops.getColumnFromDB("productCategory");
                break;
            case 3:
                typesT = SQL_Ops.getColumnFromDB("productType");
                break;
        }
    }

    static void updateDBLists() {
        mainT = SQL_Ops.getFromDBwithParams();
        servT = SQL_Ops.getServiceListFromDB();
    }

    private static ArrayList<TableElement> getModelsT() {
        return modelsT;
    }

    static ArrayList<TableElement> getBrandsT() {
        return brandsT;
    }

    static ArrayList<TableElement> getCategoriesT() {
        return categoriesT;
    }

    static ArrayList<TableElement> getTypesT() {
        return typesT;
    }

    static ArrayList<MainTElement> getMainT() {
        return mainT;
    }

    static ArrayList<ServiceTElement> getSearchL() {
        return searchL;
    }

    private static ArrayList<ServiceTElement> getServT() {
        return servT;
    }

    private static void addSearchL(ServiceTElement toAddE) {
        searchL.add(toAddE);
    }

    static void clearSearchL() {
        searchL.clear();
    }

    static void addJournalRec (journalRecord Record) {
        journal.add(Record);
    }

    static ArrayList<journalRecord> getJournal() {
        return journal;
    }

    private static ArrayList<MainTElement> ServToMain(List<ServiceTElement> toConvertList) {
        String tmpBrand="";
        String tmpCategory="";
        String tmpType="";
        ArrayList<MainTElement> retList = new ArrayList<>();
        for ( ServiceTElement T: toConvertList) {
            for ( TableElement Tb: brandsT ) {
                if (T.getBrandID() == Tb.getID()) tmpBrand = Tb.getName();
            }
            for ( TableElement Tc: categoriesT) {
                if (T.getCategoryID() == Tc.getID()) tmpCategory = Tc.getName();
            }
            for ( TableElement Tt: typesT) {
                if (T.getTypeID() == Tt.getID()) tmpType = Tt.getName();
            }

            for ( MainTElement Tm: mainT) {
                if (Tm.getName().equals(T.getModelName())&& Tm.getBrand().equals(tmpBrand) && Tm.getCategory().equals(tmpCategory) && Tm.getType().equals(tmpType))
                    retList.add(Tm);
            }
        }
        return retList;
    }

    static void search() {
        int[] isParamChosen = new int[4];
        String isSearchOver;
        ArrayList<String> toSearchModelNameList = new ArrayList<>();
        int toSearchBrandID=0;
        int toSearchCategoryID=0;
        int toSearchTypeID=0;
        do {
            //читаю выбор пользователя
            IO_Ops.ShowSearchParams(isParamChosen, true);
            int searchParam=IO_Ops.SearchMenu();
            switch (searchParam) {
                case 1:
                    //для поиска по бренду
                    toSearchBrandID = IO_Ops.inputID(1, false);
                    if (toSearchBrandID>0) isParamChosen[0] = 1;
                    if (toSearchBrandID==0) isParamChosen[0] = 0;
                    break;
                case 2:
                    //для поиска по названию модели
                    String message = "название или его часть";
                    if (isParamChosen[1]==1) message = message + " (0 - исключение критерия из поиска)";
                    String inputLine=IO_Ops.ProtectedStrInp(message);
                    //определяю название модели / исключаю из поиска, если этот критерий уже ранее был введен
                    toSearchModelNameList.clear();
                    if (inputLine.equals("0")) isParamChosen[1]=0;
                    else {
                        for (TableElement T: getModelsT()) {
                            if (T.getName().contains(inputLine)) {
                                toSearchModelNameList.add(T.getName());
                            }
                        }
                        if (!toSearchModelNameList.isEmpty()) isParamChosen[1]=1;
                    }
                    break;
                case 3:
                    //для поиска по категории
                    toSearchCategoryID = IO_Ops.inputID(2,false);
                    if (toSearchCategoryID>0) isParamChosen[2]=1;
                    if (toSearchCategoryID==0) isParamChosen[2]=0;
                    break;
                case 4:
                    //для поиска по типу
                    toSearchTypeID = IO_Ops.inputID(3,false);
                    if (toSearchTypeID>0) isParamChosen[3]=1;
                    if (toSearchTypeID==0) isParamChosen[3]=0;
                    break;
            }
            //очищаю результат поиска
            clearSearchL();
            if (IO_Ops.ShowSearchParams(isParamChosen,false)>0) {
                //формирование списка элементов, совпадающих с вводом
                Iterator<String> iterator = toSearchModelNameList.iterator();
                String tmp="";
                do {
                    if (iterator.hasNext())
                        tmp = iterator.next();
                    for ( ServiceTElement Ts: getServT() ) {
                        ServiceTElement tElement = new ServiceTElement(isParamChosen[0]==1?toSearchBrandID:Ts.getBrandID(),isParamChosen[1]==1?tmp:Ts.getModelName(),isParamChosen[2]==1?toSearchCategoryID:Ts.getCategoryID(),isParamChosen[3]==1?toSearchTypeID:Ts.getTypeID());
                        if (Ts.equals(tElement))
                            addSearchL(Ts);
                    }
                } while (iterator.hasNext());
                //вывод результата поиска
                IO_Ops.ShowMainTList(ServToMain(getSearchL()));
            }
        } while (IO_Ops.YesOrNot("Уточнить поиск? (y/n)").equals("y"));
    }
}
