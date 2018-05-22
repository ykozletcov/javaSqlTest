package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

class SQL_Ops {
    private static SQL_Ops instance;
    private static Connection DBConnection;

    private SQL_Ops() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            IO_Ops.ClassNotFoundException();
        }
        try {
            DBConnection = DriverManager.getConnection("jdbc:sqlite:musicStoreDB.db");
        } catch (SQLException E) {
            IO_Ops.SQLExceptionMessage();
        }
    }

    static SQL_Ops getInstance() {
        if (instance == null)
            instance = new SQL_Ops();
        return instance;
    }

    static ArrayList<TableElement> getColumnFromDB(String tableName){
        //получаю содержимое указанной таблицы
        ArrayList<TableElement> retList = new ArrayList<>();
        try (Statement statement = DBConnection.createStatement()) {
            ResultSet rsList = statement.executeQuery("select id, name from " + tableName);
            while (rsList.next()) {
                int tmpID = rsList.getInt(1);
                String tmpCont = rsList.getString(2);
                TableElement tmpE = new TableElement(tmpID, tmpCont);
                retList.add(tmpE);
            }
        } catch (SQLException E) {
            IO_Ops.SQLExceptionMessage();
        }
        return retList;
    }

    static void addTElement(String tableName, String addName) {
        //добавление нового элемента в указанную таблицу
        try (PreparedStatement preparedStatement = DBConnection.prepareStatement("insert into "+tableName+" (name) values (?)")){
            preparedStatement.setString(1,addName);
            preparedStatement.executeUpdate();
            journalRec(tableName + " - добавлено: " +addName);
        } catch (SQLException e) {
            IO_Ops.SQLExceptionMessage();
        }

    }

    static int getTElementID(String tableName, String toGetIDName) {
        int retID=0;
        try (Statement statement = DBConnection.createStatement()){
            ResultSet rsList = statement.executeQuery("select id from "+tableName+" where name = '"+toGetIDName+"'");
            retID = rsList.getInt(1);
        } catch (SQLException e) {
            IO_Ops.SQLExceptionMessage();
        }
        return retID;
    }


    static void addNewToDB() {
        //добавление нового элемента в базу
        //читаю содержимое базы:
        ArrayList<ServiceTElement> serviceDBList = getServiceListFromDB ();
        System.out.println("Последовательно заполните поля для добавляемого продукта.");
        //определяю id бренда для нового продукта
        int toAddProdBrandID = IO_Ops.inputID(1, true);
        //определяю название модели для нового продукта
        String toAddModelName = IO_Ops.ProtectedStrInp("Название модели");
        //определяю id категории
        int toAddProdCategoryID = IO_Ops.inputID(2, true);
        //определяю id типа
        int toAddProdTypeID = IO_Ops.inputID(3, true);
        //определяю количество
        System.out.println("Количество:");
        int toAddProdQ = IO_Ops.ProtectedIntInp(0);
        //создаю экземпляр с введенными параметрами:
        ServiceTElement toAdd = new ServiceTElement(toAddProdBrandID, toAddModelName, toAddProdCategoryID, toAddProdTypeID);
        boolean isThere = false;
        //определяю, содержится ли введенное в базе:
        for ( ServiceTElement counter: serviceDBList ) {
            if (toAdd.equals(counter)) isThere = true;
        }
        //если нет, то добавляю в базу:
        if (!isThere) {
            String SQLinsert = "insert into models (name, brandName, productCategoryName, productTypeName, quantity) values (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = DBConnection.prepareStatement(SQLinsert)) {
                preparedStatement.setString(1,toAddModelName);
                preparedStatement.setInt(2, toAddProdBrandID);
                preparedStatement.setInt(3,toAddProdCategoryID);
                preparedStatement.setInt(4,toAddProdTypeID);
                preparedStatement.setInt(5,toAddProdQ);
                int r=preparedStatement.executeUpdate();
                if (r>0) {
                    System.out.println("Продукт успешно добавлен!");
                    journalRec("models - добавлено: " + toAddModelName + ", " + toAddProdQ);
                }
            } catch (SQLException E) {
                IO_Ops.SQLExceptionMessage();
            }
            List_Ops.updateDBLists();
            //если да, то предлагаю изменить количество:
        } else {
            String toUpdate = IO_Ops.YesOrNot("Введенныый продукт содержится в базе - добавить количество? (y/n)");
            if (toUpdate.equals("y")) {
                int isThereID=0, isThereQ = 0;
                //определяю id элемента с введенными параметрами
                String SQLselect = "select id, quantity from models where name = ? and brandName = ? and productCategoryName = ? and productTypeName = ? ";
                try (PreparedStatement preparedStatement = DBConnection.prepareStatement(SQLselect)) {
                    preparedStatement.setString(1, toAddModelName);
                    preparedStatement.setInt(2, toAddProdBrandID);
                    preparedStatement.setInt(3, toAddProdCategoryID);
                    preparedStatement.setInt(4, toAddProdTypeID);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    isThereID = resultSet.getInt(1);
                    isThereQ = resultSet.getInt(2);
                } catch (SQLException E) {
                    IO_Ops.SQLExceptionMessage();
                }
                toAddProdQ+=isThereQ;
                //по определенному id записываю количество (сумма "было"+"ввели")
                String SQLupdate = "update models set quantity = ? where id = ?";
                try (PreparedStatement preparedStatement = DBConnection.prepareStatement(SQLupdate)) {
                    preparedStatement.setInt(1, toAddProdQ);
                    preparedStatement.setInt(2, isThereID);
                    int r=preparedStatement.executeUpdate();
                    if (r>0) {
                        System.out.println("Количество изменено");
                        journalRec(toAddModelName +" изменено количество: "+ isThereQ + "->" + toAddProdQ);
                    }
                } catch (SQLException E) {
                    IO_Ops.SQLExceptionMessage();
                }
            }
        }
    }

    static ArrayList<MainTElement> getFromDBwithParams(/*String modelName, int brandID, int CategoryID, int TypeID*/) {
        //выборка из базы по введенным значениям бренда, названия модели, категории, типа
        ArrayList<MainTElement> retList = new ArrayList<>();
        String SQLSelect =  "select models.id, brand.name, models.name, productCategory.name, productType.name, models.quantity from models " +
                "join brand on models.brandName = brand.id " +
                "join productCategory on models.productCategoryName = productCategory.id " +
                "join productType on models.productTypeName= productType.id";
//        int numberOfParams = 0, addedParams = 0;
//        //определяю количество ненулевых переданных параметров
//        if (!modelName.equals("")) numberOfParams++;
//        if (brandID!=0) numberOfParams++;
//        if (CategoryID!=0) numberOfParams++;
//        if (TypeID!=0) numberOfParams++;
//
//        //если переданы какие-то параметры
//        if (numberOfParams>0) {
//            SQLSelect=SQLSelect+" where ";
//            //добавление к запросу переданного названия модели
//            if (!modelName.equals("")) {
//                SQLSelect = SQLSelect + "models.name = '"+modelName+"'";
//                addedParams++;
//                if (addedParams<numberOfParams) SQLSelect = SQLSelect + " and ";
//            }
//            //добавление к запросу переданного бренда
//            if (brandID!=0) {
//                SQLSelect = SQLSelect + "brand.id = '"+brandID+"'";
//                addedParams++;
//                if (addedParams<numberOfParams) SQLSelect = SQLSelect + " and ";
//            }
//            //добавление к запросу переданной категории
//            if (CategoryID!=0) {
//                SQLSelect = SQLSelect + "productCategory.id = '"+CategoryID+"'";
//                addedParams++;
//                if (addedParams<numberOfParams) SQLSelect = SQLSelect + " and ";
//            }
//            //добавление к запросу переданного типа
//            if (TypeID!=0) SQLSelect = SQLSelect + "productType.id = '"+TypeID+"'";
//        }
        //выборка из базы элементов, удовлетворяющих сформированному запросу
        try (Statement statement = DBConnection.createStatement()) {
            ResultSet rs = statement.executeQuery(SQLSelect);
            while (rs.next()) {
                int id = rs.getInt(1);
                String brand = rs.getString(2);
                String modName = rs.getString(3);
                String catName = rs.getString(4);
                String typename = rs.getString(5);
                int q = rs.getInt(6);
                MainTElement T = new MainTElement(id, brand, modName, catName, typename, q);
                retList.add(T);
            }
        } catch (SQLException e) {
            IO_Ops.SQLExceptionMessage();
        }
        return retList;
    }

    static ArrayList<ServiceTElement> getServiceListFromDB() {
        //выборка из таблицы с числовыми значениями вторичных ключей (без подстановки текстовых значений)
        ArrayList<ServiceTElement> retServiceList = new ArrayList<>();
        try (Statement statement = DBConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select brandName, name, productCategoryName, productTypeName from models");
            while (resultSet.next()) {
                int BrandID = resultSet.getInt(1);
                String ModelName = resultSet.getString(2);
                int CategoryID = resultSet.getInt(3);
                int TypeID = resultSet.getInt(4);
                ServiceTElement T = new ServiceTElement(BrandID, ModelName, CategoryID, TypeID);
                retServiceList.add(T);
            }
        } catch (SQLException e) {
            IO_Ops.SQLExceptionMessage();
        }
        return retServiceList;
    }

    private static int modifyMainTable(ArrayList<ServiceTElement> modifyList, int modifyIndex, String toSetValue) {
        //изменение параметра элемента в таблице (изменение бренда, названия, категории, типа, количества)
        int modifiedStringsNum=0;
        String fieldName="";
        //определяю название параметра по переданному индексу
        switch (modifyIndex) {
            case 1:
                fieldName="brandName";
                break;
            case 2:
                fieldName="name";
                break;
            case 3:
                fieldName="productCategoryName";
                break;
            case 4:
                fieldName="productTypeName";
                break;
            case 5:
                fieldName="quantity";
                break;
        }
        //провожу изменение параметра
        try {
            Statement statement = DBConnection.createStatement();
            for ( ServiceTElement T: modifyList ) {
                String SQLupdate = "update models set "+ fieldName + " = " + toSetValue +
                        " where name = '" +T.getModelName() +
                        "' and brandName = "+T.getBrandID() +
                        " and productCategoryName = " + T.getCategoryID() +
                        " and productTypeName = " + T.getTypeID();
                modifiedStringsNum += statement.executeUpdate(SQLupdate);
                journalRec(T.getModelName() + " - изменено " + fieldName + ": установлено" + toSetValue);
            }
        } catch (SQLException E) {
            System.out.println("Неудачное обращение в базу!");
        }
        return modifiedStringsNum;
    }

    private static int deleteFromMainT(ArrayList<ServiceTElement> toDeleteList) {
        int deletedStrings = 0;
        try {
            Statement statement = DBConnection.createStatement();
            //прохожу по всем элементам выбранного множества
            for (ServiceTElement T : toDeleteList) {
                //для каждого формирую запрос с значениями полей
                String SQLdelete = "delete from models where" +
                        " name = '" + T.getModelName() +
                        "' and brandName = " + T.getBrandID() +
                        " and productCategoryName = " + T.getCategoryID() +
                        " and productTypeName = " + T.getTypeID();
                //удаляю элемент из базы, увеличиваю счетчик удаленных на 1
                deletedStrings = statement.executeUpdate(SQLdelete);
                journalRec("удалено: " + T.getModelName());
            }
        } catch (SQLException E) {
            IO_Ops.SQLExceptionMessage();
        }
        if (deletedStrings>0) List_Ops.updateDBLists();
        return deletedStrings;
    }

    static ArrayList<journalRecord> getJournalfromDB() {
        //чтение журнала из базы
        ArrayList<journalRecord> retJournal = new ArrayList<>();
        try {
            Statement statement = DBConnection.createStatement();
            String SQLselect = "select dateTime, message from journal";
            ResultSet resultSet = statement.executeQuery(SQLselect);
            while (resultSet.next()) {
                journalRecord jr = new journalRecord(resultSet.getString(1), resultSet.getString(2));
                retJournal.add(jr);
            }
        } catch (SQLException E) {
            System.out.println("Журнал поврежден или отсутствует!");
        }
        return retJournal;
    }

    private static void journalRec(String message) throws SQLException {
        Calendar c = new GregorianCalendar();
        String datetime=String.valueOf(c.getTime());
        //заношу в журнал запись с текущей датой и сформированным сообщением
        String SQLinsert = "insert into journal (datetime, message) values ('"+ datetime + "', '" +message+"')";
        try (Statement statement = DBConnection.createStatement()) {
            statement.executeUpdate(SQLinsert);
        }
        journalRecord jr = new journalRecord(datetime, message);
        List_Ops.addJournalRec(jr);
    }

    static void modifyDB() {
        String isSearchNeed="";
        if (!List_Ops.getSearchL().isEmpty()) {
            isSearchNeed = IO_Ops.YesOrNot("Использовать результаты предыдущего поиска? (y - да/n - новый поиск)");
        }
        if (isSearchNeed.equals("n") || List_Ops.getSearchL().isEmpty()) {
            System.out.println("Выберите товары для изменения/удаления:");
            List_Ops.search();
        }
        String toModifyValue="";
        String tmpS;
        int modIndex= IO_Ops.ModifyMenu();
        int modifiedStrings;
        if (modIndex == 0) return;
        if (modIndex!=6) {
            switch (modIndex) {
                case 1:
                    //выбрано "изменить производителя"
                    System.out.println("Введите нового производителя:");
                    toModifyValue = toModifyValue+ IO_Ops.inputID(1, true);
                    break;
                case 2:
                    //выбрано "изменить наименование"
                    tmpS = IO_Ops.ProtectedStrInp("Введите новое наименование");
                    toModifyValue = "'"+tmpS+"'";
                    break;
                case 3:
                    //выбрано "изменить категорию"
                    System.out.println("Введите новую категорию:");
                    toModifyValue = toModifyValue+ IO_Ops.inputID(2, true);
                    break;
                case 4:
                    //выбрано "изменить тип"
                    System.out.println("Введите новый тип:");
                    toModifyValue = toModifyValue+ IO_Ops.inputID(3, true);
                    break;
                case 5:
                    //выбрано "изменить количество"
                    System.out.println("Введите новое количество:");
                    //получаю количество от пользователя
                    toModifyValue = Integer.toString(IO_Ops.ProtectedIntInp(0));
                    break;
            }
            //изменяю выбранные элементы
            modifiedStrings = SQL_Ops.modifyMainTable(List_Ops.getSearchL(), modIndex, toModifyValue);
            if (modifiedStrings>0) {
                List_Ops.updateDBLists();
                List_Ops.clearSearchL();
            }
            System.out.println("Успешно изменено " + modifiedStrings + "записей");
        }
        else {
            //выбрано удаление
            tmpS = IO_Ops.ProtectedStrInp("Действительно удалить выбранные позиции? (y/n)");
            if (tmpS.equals("y")) {
                int deletedStrings=SQL_Ops.deleteFromMainT(List_Ops.getSearchL());
                if (deletedStrings>0) {
                    List_Ops.updateDBLists();
                    List_Ops.clearSearchL();
                }
                System.out.println("Удалено " + deletedStrings +" элементов");
            }
        }
    }

}
