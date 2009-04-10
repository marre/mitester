//
// This file was com.mitester.jaxbparser.server by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// com.mitester.jaxbparser.server on: 2009.02.21 at 12:49:08 PM IST 
//


package com.mitester.jaxbparser.server;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}fline"/>
 *         &lt;element ref="{}req-line" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}status-line" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fline",
    "reqLine",
    "statusLine"
})
@XmlRootElement(name = "LINE")
public class LINE {

    @XmlElement(required = true)
    protected Fline fline;
    @XmlElement(name = "req-line")
    protected List<ReqLine> reqLine;
    @XmlElement(name = "status-line")
    protected List<StatusLine> statusLine;

    /**
     * Gets the value of the fline property.
     * 
     * @return
     *     possible object is
     *     {@link Fline }
     *     
     */
    public Fline getFline() {
        return fline;
    }

    /**
     * Sets the value of the fline property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fline }
     *     
     */
    public void setFline(Fline value) {
        this.fline = value;
    }

    /**
     * Gets the value of the reqLine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reqLine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReqLine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReqLine }
     * 
     * 
     */
    public List<ReqLine> getReqLine() {
        if (reqLine == null) {
            reqLine = new ArrayList<ReqLine>();
        }
        return this.reqLine;
    }

    /**
     * Gets the value of the statusLine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statusLine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatusLine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatusLine }
     * 
     * 
     */
    public List<StatusLine> getStatusLine() {
        if (statusLine == null) {
            statusLine = new ArrayList<StatusLine>();
        }
        return this.statusLine;
    }

}
