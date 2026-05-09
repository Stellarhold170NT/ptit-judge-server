package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "submitString", namespace = "http://ptit.vn/judge/soap/character")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitString {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String studentCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String qCode;

    @XmlElement(namespace = "http://ptit.vn/judge/soap/character", required = true)
    private String answer;

    public SubmitString() {}

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getQCode() { return qCode; }
    public void setQCode(String qCode) { this.qCode = qCode; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
