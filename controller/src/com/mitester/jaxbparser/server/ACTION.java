//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.01 at 04:25:59 PM IST 
//


package com.mitester.jaxbparser.server;

import java.math.BigInteger;
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
 *       &lt;all>
 *         &lt;element ref="{}ADD_CRCR" minOccurs="0"/>
 *         &lt;element ref="{}ADD_CRLF" minOccurs="0"/>
 *         &lt;element ref="{}ADD_LFLF" minOccurs="0"/>
 *         &lt;element ref="{}ADD_MSG" minOccurs="0"/>
 *         &lt;element ref="{}DEL_CR" minOccurs="0"/>
 *         &lt;element ref="{}DEL_MSG" minOccurs="0"/>
 *         &lt;element ref="{}EMPTY_MSG" minOccurs="0"/>
 *         &lt;element ref="{}DEL_CRLF" minOccurs="0"/>
 *         &lt;element ref="{}DEL_LF" minOccurs="0"/>
 *         &lt;element ref="{}DUP_MSG" minOccurs="0"/>
 *         &lt;element ref="{}MEDIA" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="RECV" type="{}recvEnumType" />
 *       &lt;attribute name="SEND" type="{}sendEnumType" />
 *       &lt;attribute name="count" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="dialog" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ACTION")
public class ACTION {

    @XmlElement(name = "ADD_CRCR")
    protected ADDCRCR addcrcr;
    @XmlElement(name = "ADD_CRLF")
    protected ADDCRLF addcrlf;
    @XmlElement(name = "ADD_LFLF")
    protected ADDLFLF addlflf;
    @XmlElement(name = "ADD_MSG")
    protected ADDMSG addmsg;
    @XmlElement(name = "DEL_CR")
    protected DELCR delcr;
    @XmlElement(name = "DEL_MSG")
    protected DELMSG delmsg;
    @XmlElement(name = "EMPTY_MSG")
    protected EMPTYMSG emptymsg;
    @XmlElement(name = "DEL_CRLF")
    protected DELCRLF delcrlf;
    @XmlElement(name = "DEL_LF")
    protected DELLF dellf;
    @XmlElement(name = "DUP_MSG")
    protected DUPMSG dupmsg;
    @XmlElement(name = "MEDIA")
    protected MEDIA media;
    @XmlAttribute(name = "RECV")
    protected RecvEnumType recv;
    @XmlAttribute(name = "SEND")
    protected SendEnumType send;
    @XmlAttribute
    protected BigInteger count;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String dialog;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String value;

    /**
     * Gets the value of the addcrcr property.
     * 
     * @return
     *     possible object is
     *     {@link ADDCRCR }
     *     
     */
    public ADDCRCR getADDCRCR() {
        return addcrcr;
    }

    /**
     * Sets the value of the addcrcr property.
     * 
     * @param value
     *     allowed object is
     *     {@link ADDCRCR }
     *     
     */
    public void setADDCRCR(ADDCRCR value) {
        this.addcrcr = value;
    }

    /**
     * Gets the value of the addcrlf property.
     * 
     * @return
     *     possible object is
     *     {@link ADDCRLF }
     *     
     */
    public ADDCRLF getADDCRLF() {
        return addcrlf;
    }

    /**
     * Sets the value of the addcrlf property.
     * 
     * @param value
     *     allowed object is
     *     {@link ADDCRLF }
     *     
     */
    public void setADDCRLF(ADDCRLF value) {
        this.addcrlf = value;
    }

    /**
     * Gets the value of the addlflf property.
     * 
     * @return
     *     possible object is
     *     {@link ADDLFLF }
     *     
     */
    public ADDLFLF getADDLFLF() {
        return addlflf;
    }

    /**
     * Sets the value of the addlflf property.
     * 
     * @param value
     *     allowed object is
     *     {@link ADDLFLF }
     *     
     */
    public void setADDLFLF(ADDLFLF value) {
        this.addlflf = value;
    }

    /**
     * Gets the value of the addmsg property.
     * 
     * @return
     *     possible object is
     *     {@link ADDMSG }
     *     
     */
    public ADDMSG getADDMSG() {
        return addmsg;
    }

    /**
     * Sets the value of the addmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link ADDMSG }
     *     
     */
    public void setADDMSG(ADDMSG value) {
        this.addmsg = value;
    }

    /**
     * Gets the value of the delcr property.
     * 
     * @return
     *     possible object is
     *     {@link DELCR }
     *     
     */
    public DELCR getDELCR() {
        return delcr;
    }

    /**
     * Sets the value of the delcr property.
     * 
     * @param value
     *     allowed object is
     *     {@link DELCR }
     *     
     */
    public void setDELCR(DELCR value) {
        this.delcr = value;
    }

    /**
     * Gets the value of the delmsg property.
     * 
     * @return
     *     possible object is
     *     {@link DELMSG }
     *     
     */
    public DELMSG getDELMSG() {
        return delmsg;
    }

    /**
     * Sets the value of the delmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link DELMSG }
     *     
     */
    public void setDELMSG(DELMSG value) {
        this.delmsg = value;
    }

    /**
     * Gets the value of the emptymsg property.
     * 
     * @return
     *     possible object is
     *     {@link EMPTYMSG }
     *     
     */
    public EMPTYMSG getEMPTYMSG() {
        return emptymsg;
    }

    /**
     * Sets the value of the emptymsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link EMPTYMSG }
     *     
     */
    public void setEMPTYMSG(EMPTYMSG value) {
        this.emptymsg = value;
    }

    /**
     * Gets the value of the delcrlf property.
     * 
     * @return
     *     possible object is
     *     {@link DELCRLF }
     *     
     */
    public DELCRLF getDELCRLF() {
        return delcrlf;
    }

    /**
     * Sets the value of the delcrlf property.
     * 
     * @param value
     *     allowed object is
     *     {@link DELCRLF }
     *     
     */
    public void setDELCRLF(DELCRLF value) {
        this.delcrlf = value;
    }

    /**
     * Gets the value of the dellf property.
     * 
     * @return
     *     possible object is
     *     {@link DELLF }
     *     
     */
    public DELLF getDELLF() {
        return dellf;
    }

    /**
     * Sets the value of the dellf property.
     * 
     * @param value
     *     allowed object is
     *     {@link DELLF }
     *     
     */
    public void setDELLF(DELLF value) {
        this.dellf = value;
    }

    /**
     * Gets the value of the dupmsg property.
     * 
     * @return
     *     possible object is
     *     {@link DUPMSG }
     *     
     */
    public DUPMSG getDUPMSG() {
        return dupmsg;
    }

    /**
     * Sets the value of the dupmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link DUPMSG }
     *     
     */
    public void setDUPMSG(DUPMSG value) {
        this.dupmsg = value;
    }

    /**
     * Gets the value of the media property.
     * 
     * @return
     *     possible object is
     *     {@link MEDIA }
     *     
     */
    public MEDIA getMEDIA() {
        return media;
    }

    /**
     * Sets the value of the media property.
     * 
     * @param value
     *     allowed object is
     *     {@link MEDIA }
     *     
     */
    public void setMEDIA(MEDIA value) {
        this.media = value;
    }

    /**
     * Gets the value of the recv property.
     * 
     * @return
     *     possible object is
     *     {@link RecvEnumType }
     *     
     */
    public RecvEnumType getRECV() {
        return recv;
    }

    /**
     * Sets the value of the recv property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecvEnumType }
     *     
     */
    public void setRECV(RecvEnumType value) {
        this.recv = value;
    }

    /**
     * Gets the value of the send property.
     * 
     * @return
     *     possible object is
     *     {@link SendEnumType }
     *     
     */
    public SendEnumType getSEND() {
        return send;
    }

    /**
     * Sets the value of the send property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendEnumType }
     *     
     */
    public void setSEND(SendEnumType value) {
        this.send = value;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

    /**
     * Gets the value of the dialog property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialog() {
        return dialog;
    }

    /**
     * Sets the value of the dialog property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialog(String value) {
        this.dialog = value;
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
