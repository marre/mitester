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
 *         &lt;element ref="{}LINE"/>
 *         &lt;element ref="{}HEADERS"/>
 *         &lt;element ref="{}CONTENT"/>
 *         &lt;element ref="{}APPEND"/>
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
    "line",
    "headers",
    "content",
    "append"
})
@XmlRootElement(name = "ORDER_AS")
public class ORDERAS {

    @XmlElement(name = "LINE", required = true)
    protected LINE line;
    @XmlElement(name = "HEADERS", required = true)
    protected HEADERS headers;
    @XmlElement(name = "CONTENT", required = true)
    protected CONTENT content;
    @XmlElement(name = "APPEND", required = true)
    protected APPEND append;

    /**
     * Gets the value of the line property.
     * 
     * @return
     *     possible object is
     *     {@link LINE }
     *     
     */
    public LINE getLINE() {
        return line;
    }

    /**
     * Sets the value of the line property.
     * 
     * @param value
     *     allowed object is
     *     {@link LINE }
     *     
     */
    public void setLINE(LINE value) {
        this.line = value;
    }

    /**
     * Gets the value of the headers property.
     * 
     * @return
     *     possible object is
     *     {@link HEADERS }
     *     
     */
    public HEADERS getHEADERS() {
        return headers;
    }

    /**
     * Sets the value of the headers property.
     * 
     * @param value
     *     allowed object is
     *     {@link HEADERS }
     *     
     */
    public void setHEADERS(HEADERS value) {
        this.headers = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link CONTENT }
     *     
     */
    public CONTENT getCONTENT() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONTENT }
     *     
     */
    public void setCONTENT(CONTENT value) {
        this.content = value;
    }

    /**
     * Gets the value of the append property.
     * 
     * @return
     *     possible object is
     *     {@link APPEND }
     *     
     */
    public APPEND getAPPEND() {
        return append;
    }

    /**
     * Sets the value of the append property.
     * 
     * @param value
     *     allowed object is
     *     {@link APPEND }
     *     
     */
    public void setAPPEND(APPEND value) {
        this.append = value;
    }

}