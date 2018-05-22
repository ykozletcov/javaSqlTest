package com.company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder = {"date","rec"})
public class journalRecord {
    private String date;
    private String rec;

    public journalRecord() {
    }

    public journalRecord(String date, String rec) {
        this.date = date;
        this.rec = rec;
    }

    public String getDate() {
        return date;
    }
@XmlElement(name = "message")
    public String getRec() {
        return rec;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    @Override
    public String toString() {
        return String.format("%-30s%-30s",date,rec);
    }
}
