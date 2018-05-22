package com.company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"samples"}, name = "models")
public class DBList {
    private List<MainTElement> samples = new ArrayList<>();
@XmlElement(name = "model")
    public List<MainTElement> getSamples() {
        return samples;
    }

    public void setSamples(List<MainTElement> samples) {
        this.samples = samples;
    }

    public void addSample (MainTElement productSample){
        samples.add(productSample);
    }

    @Override
    public String toString() {
        return "DBList{" +
                "samples=" + samples +
                '}';
    }
}
