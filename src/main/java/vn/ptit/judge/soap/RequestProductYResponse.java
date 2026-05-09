package vn.ptit.judge.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "requestProductYResponse", namespace = "http://ptit.vn/judge/soap/object")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestProductYResponse {

    @XmlElement(namespace = "http://ptit.vn/judge/soap/object", required = true)
    private SoapProductY productY;

    public RequestProductYResponse() {}

    public RequestProductYResponse(SoapProductY productY) { this.productY = productY; }

    public SoapProductY getProductY() { return productY; }
    public void setProductY(SoapProductY productY) { this.productY = productY; }
}
