package com.company;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"records"}, name = "record")
public class journal {
    private List<journalRecord> records = new ArrayList<>();

    public journal() {
    }

@XmlElement(name = "record")
    public List<journalRecord> getRecords() {
        return records;
    }

    public void setRecords(List<journalRecord> records) {
        this.records = records;
    }

    public void addRec (journalRecord journalRecord) {
        records.add(journalRecord);
    }

    @Override
    public String toString() {
        return "journal{" +
                "records=" + records +
                '}';
    }
}
