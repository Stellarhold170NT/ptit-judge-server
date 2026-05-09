package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "submitProductYResponse", namespace = "http://ptit.vn/judge/soap/object")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitProductYResponse {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private String status;

    public SubmitProductYResponse() {}

    public SubmitProductYResponse(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
