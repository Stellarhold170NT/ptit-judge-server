package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "requestString", namespace = "http://ptit.vn/judge/soap/character")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestString {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String studentCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String qCode;

    public RequestString() {}

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getQCode() { return qCode; }
    public void setQCode(String qCode) { this.qCode = qCode; }
}
