package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "submitDataIntResponse", namespace = "http://ptit.vn/judge/soap/data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitDataIntResponse {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/data", required = true)
    private String status;

    public SubmitDataIntResponse() {}

    public SubmitDataIntResponse(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
