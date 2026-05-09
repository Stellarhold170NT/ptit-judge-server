package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "requestStringResponse", namespace = "http://ptit.vn/judge/soap/character")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestStringResponse {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String data;

    public RequestStringResponse() {}

    public RequestStringResponse(String data) { this.data = data; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
