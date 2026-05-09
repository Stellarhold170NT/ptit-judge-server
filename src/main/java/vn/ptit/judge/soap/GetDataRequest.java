package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getDataRequest", namespace = "http://ptit.vn/judge/soap/data")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDataRequest {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/data", required = true)
    private String studentCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/data", required = true)
    private String qCode;

    public GetDataRequest() {}

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getQCode() { return qCode; }
    public void setQCode(String qCode) { this.qCode = qCode; }
}
