package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "submitProductY", namespace = "http://ptit.vn/judge/soap/object")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitProductY {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private String studentCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private String qCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private SoapProductY productY;

    public SubmitProductY() {}

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getQCode() { return qCode; }
    public void setQCode(String qCode) { this.qCode = qCode; }
    public SoapProductY getProductY() { return productY; }
    public void setProductY(SoapProductY productY) { this.productY = productY; }
}
