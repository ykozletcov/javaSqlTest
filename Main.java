package com.company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (List_Ops.getInstance(SQL_Ops.getInstance())!=null) {
            do {
                int ch = IO_Ops.mainMenu();
                if (ch==0) break;
                switch (ch) {
                    case 1:
                        //вывод содержимого базы на экран
                        IO_Ops.ShowMainTList(List_Ops.getMainT());
                        IO_Ops.RetToMenu();
                        break;
                    case 2:
                        //добавление нового элемента
                        SQL_Ops.addNewToDB();
                        IO_Ops.RetToMenu();
                        break;
                    case 3:
                        //поиск
                        List_Ops.search();
                        IO_Ops.RetToMenu();
                        break;
                    case 4:
                        //изменение/удаление
                        SQL_Ops.modifyDB();
                        IO_Ops.RetToMenu();
                        break;
                    case 5:
                        //просмотр журнала
                        IO_Ops.ShowJournal();
                        IO_Ops.RetToMenu();
                        break;
                    case 6:
                        //экспорт базы в XML
                        String fileName = "DB.xml";
                        ArrayList<MainTElement> toXMLList = SQL_Ops.getFromDBwithParams();
                        //создаю экземпляр класса базы
                        DBList dbList = new DBList();
                        for ( MainTElement ps: toXMLList ) {
                            dbList.addSample(ps);
                        }
                        //преобразую экземпляр в xml
                        int isOk = convertToXML(dbList, fileName);
                        if (isOk == 1 ) System.out.println("XML-файл 'DB.xml' сгенерирован");
                        else System.out.println("Ошибка экспорта базы в XML");
                        IO_Ops.RetToMenu();
                        break;
                    case 7:
                        //экспорт журнала в xml
                        String JfileName = "journal.xml";
                        ArrayList<journalRecord> jXml = new ArrayList<>();
                        //читаю журнал из базы
                       // jXml = getJournalRecors(DBconnection);
                        //создаю экземпляр класса журнал
                        journal j = new journal();
                        for (journalRecord jx: jXml ) {
                            j.addRec(jx);
                        }
                        //преобразую экземпляр в xml
                        int isOkJ = journalToXML(j,JfileName);
                        if (isOkJ == 1 ) System.out.println("XML-файл журнала сгенерирован");
                        else System.out.println("Ошибка экспорта журнала в XML");
                        IO_Ops.RetToMenu();
                        break;
                    case 8:
                        //вывод справки
                        IO_Ops.callHelp();
                        break;
                }
            } while (true);
        }
    }

    private static int convertToXML(DBList dbList, String fileName) {
        //экспорт экземпляра класса базы в xml
        try {
            JAXBContext context = JAXBContext.newInstance(DBList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(dbList, new File(fileName));
            return 1;
        } catch (JAXBException e) {
            return 0;
        }
    }

    private static int journalToXML(journal j, String fileName) {
        //экспорт экземпляра класса журнала в xml
        try {
            JAXBContext context = JAXBContext.newInstance(journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(j, new File(fileName));
            return 1;
        } catch (JAXBException e) {
            return 0;
        }
    }
}