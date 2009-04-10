//
// This file was com.mitester.jaxbparser.server by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// com.mitester.jaxbparser.server on: 2009.02.21 at 12:49:08 PM IST 
//


package com.mitester.jaxbparser.server;

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
 *         &lt;element ref="{}SDP_BODY" minOccurs="0"/>
 *         &lt;element ref="{}XML_BODY" minOccurs="0"/>
 *         &lt;element ref="{}TXT_BODY" minOccurs="0"/>
 *         &lt;element ref="{}OTHERS_BODY" minOccurs="0"/>
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
    "sdpbody",
    "xmlbody",
    "txtbody",
    "othersbody"
})
@XmlRootElement(name = "CONTENT")
public class CONTENT {

    @XmlElement(name = "SDP_BODY")
    protected SDPBODY sdpbody;
    @XmlElement(name = "XML_BODY")
    protected XMLBODY xmlbody;
    @XmlElement(name = "TXT_BODY")
    protected TXTBODY txtbody;
    @XmlElement(name = "OTHERS_BODY")
    protected OTHERSBODY othersbody;

    /**
     * Gets the value of the sdpbody property.
     * 
     * @return
     *     possible object is
     *     {@link SDPBODY }
     *     
     */
    public SDPBODY getSDPBODY() {
        return sdpbody;
    }

    /**
     * Sets the value of the sdpbody property.
     * 
     * @param value
     *     allowed object is
     *     {@link SDPBODY }
     *     
     */
    public void setSDPBODY(SDPBODY value) {
        this.sdpbody = value;
    }

    /**
     * Gets the value of the xmlbody property.
     * 
     * @return
     *     possible object is
     *     {@link XMLBODY }
     *     
     */
    public XMLBODY getXMLBODY() {
        return xmlbody;
    }

    /**
     * Sets the value of the xmlbody property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLBODY }
     *     
     */
    public void setXMLBODY(XMLBODY value) {
        this.xmlbody = value;
    }

    /**
     * Gets the value of the txtbody property.
     * 
     * @return
     *     possible object is
     *     {@link TXTBODY }
     *     
     */
    public TXTBODY getTXTBODY() {
        return txtbody;
    }

    /**
     * Sets the value of the txtbody property.
     * 
     * @param value
     *     allowed object is
     *     {@link TXTBODY }
     *     
     */
    public void setTXTBODY(TXTBODY value) {
        this.txtbody = value;
    }

    /**
     * Gets the value of the othersbody property.
     * 
     * @return
     *     possible object is
     *     {@link OTHERSBODY }
     *     
     */
    public OTHERSBODY getOTHERSBODY() {
        return othersbody;
    }

    /**
     * Sets the value of the othersbody property.
     * 
     * @param value
     *     allowed object is
     *     {@link OTHERSBODY }
     *     
     */
    public void setOTHERSBODY(OTHERSBODY value) {
        this.othersbody = value;
    }

}
