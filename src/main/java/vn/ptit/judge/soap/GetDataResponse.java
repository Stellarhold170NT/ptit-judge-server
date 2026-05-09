package vn.ptit.judge.soap;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getDataResponse", namespace = "http://ptit.vn/judge/soap/data")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDataResponse {

    @XmlElement(name = "data", namespace = "http://ptit.vn/judge/soap/data")
    private List<String> data;

    public GetDataResponse() {
        this.data = new ArrayList<>();
    }

    public List<String> getData() { return data; }
    public void setData(List<String> data) { this.data = data; }
}
