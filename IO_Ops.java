package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class IO_Ops {
    private static Scanner s = new Scanner(System.in);

    static int mainMenu() {
        System.out.println("Выбор действия:");
        System.out.println("1 - показать содержимое базы");
        System.out.println("2 - добавление нового лота в базу");
        System.out.println("3 - поиск лота в базе");
        System.out.println("4 - редактирование/удаление лота");
        System.out.println("5 - просмотреть журнал");
        System.out.println("6 - экспорт базы в XML");
        System.out.println("7 - экспорт журнала в XML");
        System.out.println("8 - вывод справки");
        System.out.println("0 - выход");
        return ProtectedIntInp(8);
    }

    static int ModifyMenu () {
        System.out.println("Выберите действие:");
        System.out.println("1 - изменить производителя");
        System.out.println("2 - изменить название");
        System.out.println("3 - изменить категорию");
        System.out.println("4 - изменить тип");
        System.out.println("5 - изменить количество");
        System.out.println("6 - удалить");
        System.out.println("0 - отказ от действия");
        return ProtectedIntInp(6);
    }

    static int SearchMenu () {
        System.out.println("Поиск по:");
        System.out.println("1 - названию бренда;");
        System.out.println("2 - названию модели;");
        System.out.println("3 - категории;");
        System.out.println("4 - типу");
        return ProtectedIntInp(4);
    }

    static void ShowMainTList(List<MainTElement> toShowList) {
        System.out.println("Список продуктов\n");
        System.out.println(String.format("%-4s%-15s%-25s%-25s%-25s%-4s","№","Бренд","Название","Категория","Тип","Количество"));
        StringBuilder splitter = new StringBuilder();
        for (int i=0;i<90;i++) splitter.append("-");
        System.out.println(splitter+"\n");
        for (MainTElement T: toShowList) {
            System.out.println(T);
        }
        System.out.println(splitter);
    }

    static void ShowJournal() {
        System.out.println("Журнал событий\n");
        System.out.println(String.format("%-30s%-30s","Дата", "Запись"));
        StringBuilder splitter = new StringBuilder();
        for (int i =0;i<90;i++) splitter.append("-");
        System.out.println(splitter +"\n");
        for ( journalRecord Tj: List_Ops.getJournal()) {
            System.out.println(Tj);
        }
        System.out.println(splitter);
    }

    static void SQLExceptionMessage() {
        System.out.println("Неудачное обращение к базе, нажмите любую клавишу");
        s.nextLine();
    }

    static void ClassNotFoundException() {
        System.out.println("Драйвер базы данных не найден, нажмите любую клавишу");
        s.nextLine();
    }

    static void RetToMenu () {
        System.out.println("Для возврата в меню нажмите Enter");
        s.nextLine();
    }

    static String ProtectedStrInp (String substring) {
        String retStr;
        do {
            System.out.println(substring + ":");
            retStr = s.nextLine();
            if (retStr.equals("")) System.out.println("Неверный ввод! Поле не может быть пустым");
        } while (retStr.equals(""));
        return retStr;
    }


    static int ProtectedIntInp (int lim) {
        int retInt;
        String message;
        if (lim==0) message = "Неверный ввод! Введите неотрицательное число";
        else message = "Неверный ввод! Введите число от 0 до " + lim;
        do {
            try {
                retInt = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException E) {
                System.out.println(message);
                retInt=-1;
            }
        } while (! (retInt>-1 && (retInt<=lim || lim == 0)));
        return retInt;
    }

    static String YesOrNot (String message) {
        System.out.println(message);
        String inpStr;
        do {
            inpStr=s.nextLine();
            if ((!inpStr.equals("y"))&&(!inpStr.equals("n"))) System.out.println("Введите y или n");
            else break;
        } while (true);
        return inpStr;
    }

    static int ShowSearchParams (int[] isParamChosen, boolean isUserInput) {
        String Criteries = "Критерии поиска:";
        int hashPCosen=0;
        for (int i = 0; i < 4; i++) {
            hashPCosen+=isParamChosen[i];
        }
        if (isParamChosen[0]==1) Criteries=Criteries+" по бренду;";
        if (isParamChosen[1]==1) Criteries=Criteries+" по модели;";
        if (isParamChosen[2]==1) Criteries=Criteries+" по категории;";
        if (isParamChosen[3]==1) Criteries=Criteries+" по типу.";
        if (hashPCosen==0) Criteries=Criteries+" отсутствуют";
        else if (isUserInput) Criteries = Criteries + "\n" +  "Для исключения критерия из поиска выберите его повторно и нажмите 0";
        System.out.println(Criteries);
        //if (hashPCosen!=0) System.out.println("Выберите критерий поиска повторно и нажмите 0 для исключения из поиска");
        return hashPCosen;
    }

    static void callHelp() {
        System.out.println("Контрольная работа на тему 'Музыкальный магазин'");
        System.out.println("Пользователю предлагается выбор действия с базой данных магазина, ");
        System.out.println("выбор осуществляется в соответствии с указаниями в меню.\n");
        System.out.println("ВЫВОД СОДЕРЖИМОГО БАЗЫ ДАННЫХ\n");
        System.out.println("Содержимое базы данных выводится в виде списка с горизонтальными разделителями, отделяющими");
        System.out.println("категории товаров для облегчения восприятия. Поля для каждого лота выводятся в следующем");
        System.out.println("порядке: а) название производителя, б) название модели, в) маркировка или заводской код модели,");
        System.out.println("г) категория товара, д) тип товара\n");
        System.out.println("ДОБАВЛЕНИЕ ТОВАРА\n");
        System.out.println("При добавлении в базу заносятся значения полей в порядке, соответствующем их следованию в базе,");
        System.out.println("значения вводятся пользователем последовательно, пошагово");
        System.out.println("Пример корректной полной записи для занесения в базу: \n");
        System.out.println("Vox / Analog Valve Amplifier / AV15 / Для студии и концертов / гитарное усиление\n");
        System.out.println("Пользователь может не заполнять поля 'категория товара', 'тип товара', и одно из полей ");
        System.out.println("'название модели'/'маркировка или заводской код', в этом случае данные поля");
        System.out.println("остаются пустыми и товар добавляется в категорию 'разное'.\n");
        System.out.println("ПОИСК, РЕДАКТИРОВАНИЕ И УДАЛЕНИЕ\n");
        System.out.println("Поиск лота осуществляется по значению одного из полей, либо по совокупному значению полей");
        System.out.println("Если пользователь вводит название производителя, категорию товара или тип товара (или их комбинацию)");
        System.out.println("то результатом поиска будет список всех лотов, соответствующих введенному значению, после чего пользователь");
        System.out.println("может уточнить запрос для получения единственной записи");
        System.out.println("если пользователь вводит название модели или заводскую маркировку, результатом поиска");
        System.out.println("будет являться единственная запись в базе данных");
        System.out.println("Редактирование товаров (изменение значений полей или количества, удаление) происходит посредством отыскания");
        System.out.println("и последующего изменения нужного лота в базе данных, т.е. удалению и редактированию предшествует процедура поиска.\n");
        System.out.println("нажмите Enter для возврата в меню");
        s.nextLine();
    }

    static int inputID(int tableNameIndex, boolean allowAdd) {
        int isNum;                                                                                                    //для определения, что введен код
        int retID=-1;                                                                                           //возвращаемый ID
        String substring="";
        String tableName="";
        String insField;
        ArrayList<TableElement> fieldList = new ArrayList<>();
        switch (tableNameIndex) {                                                                                      //объявление подстроки для вывода
            case 1:
                substring="Производитель";
                tableName = "brand";
                fieldList = List_Ops.getBrandsT();
                break;
            case 2:
                substring="Категория";
                tableName="productCategory";
                fieldList = List_Ops.getCategoriesT();
                break;
            case 3:
                substring="Тип";
                tableName="productType";
                fieldList = List_Ops.getTypesT();
                break;
        }
        do {
            do {
                insField = ProtectedStrInp(substring+" или идентификатор (s - показать список)");
                if (insField.equals("s")) {                                                                          //вывод подсказки
                    for (TableElement T : fieldList) {
                        System.out.println(T);
                    }
                }
            } while (insField.equals("s"));
            try {
                    isNum=Integer.parseInt(insField);                                                                   //попытка ввода ID из списка
            } catch (NumberFormatException E) {
                isNum = -1;                                                                                         //или попытка ввода названия элемента таблицы
            }
            if (isNum==0) return 0;
            for (TableElement T: fieldList) {
                if (isNum==T.getID() || insField.equals(T.getName())) retID = T.getID();
            }
            if (retID==-1 && isNum>0) System.out.println("Неверный ввод! Введите ID из списка!");
            if (retID==-1 && isNum==-1 && !allowAdd) System.out.println("Неверный ввод! Введите наименование из списка!");
            if (retID==-1 && isNum==-1 && allowAdd) {
                System.out.println(substring+" не содержится в базе. Добавить?");
                String tmpStr = YesOrNot("(y - добавление, n - повторный ввод)");
                if (tmpStr.equals("y")) {
                    SQL_Ops.addTElement(tableName,insField);                                              //добавляю в базу новый элемент
                    retID = SQL_Ops.getTElementID(tableName,insField);                             //получаю ID добавленного элемента
                    List_Ops.updateT(tableNameIndex);
                }
            }
        } while (retID==-1);
        return retID;                                                                                          //возвращаю ID бренда для нового продукта
    }
}
