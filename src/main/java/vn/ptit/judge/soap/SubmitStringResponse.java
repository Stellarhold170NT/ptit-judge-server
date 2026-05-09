package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "submitStringResponse", namespace = "http://ptit.vn/judge/soap/character")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitStringResponse {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String status;

    public SubmitStringResponse() {}

    public SubmitStringResponse(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
