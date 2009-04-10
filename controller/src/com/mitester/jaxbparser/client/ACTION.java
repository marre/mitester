//
// This file was com.mitester.jaxbparser.client by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.02.21 at 02:33:54 AM EST 
//


package com.mitester.jaxbparser.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}PARAM" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="DISCARD" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="RECV" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="SEND" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "param"
})
@XmlRootElement(name = "ACTION")
public class ACTION {

    @XmlElement(name = "PARAM")
    protected List<PARAM> param;
    @XmlAttribute(name = "DISCARD")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String discard;
    @XmlAttribute(name = "RECV")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String recv;
    @XmlAttribute(name = "SEND")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String send;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String value;

    /**
     * Gets the value of the param property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the param property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPARAM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PARAM }
     * 
     * 
     */
    public List<PARAM> getPARAM() {
        if (param == null) {
            param = new ArrayList<PARAM>();
        }
        return this.param;
    }

    /**
     * Gets the value of the discard property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISCARD() {
        return discard;
    }

    /**
     * Sets the value of the discard property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISCARD(String value) {
        this.discard = value;
    }

    /**
     * Gets the value of the recv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECV() {
        return recv;
    }

    /**
     * Sets the value of the recv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECV(String value) {
        this.recv = value;
    }

    /**
     * Gets the value of the send property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEND() {
        return send;
    }

    /**
     * Sets the value of the send property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEND(String value) {
        this.send = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
