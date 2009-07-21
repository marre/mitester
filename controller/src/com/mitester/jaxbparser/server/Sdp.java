//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.16 at 06:32:49 AM EDT 
//


package com.mitester.jaxbparser.server;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="attach" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="attach-again" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="braces" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="count" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="fold" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="format" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="left-braces" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="left-quotes" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="left-space" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="quotes" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="right-braces" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="right-quotes" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="right-space" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="space" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "sdp")
public class Sdp {

    @XmlAttribute
    protected BigInteger attach;
    @XmlAttribute(name = "attach-again")
    protected BigInteger attachAgain;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String braces;
    @XmlAttribute
    protected BigInteger count;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String fold;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String format;
    @XmlAttribute(name = "left-braces")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String leftBraces;
    @XmlAttribute(name = "left-quotes")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String leftQuotes;
    @XmlAttribute(name = "left-space")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String leftSpace;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String quotes;
    @XmlAttribute(name = "right-braces")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rightBraces;
    @XmlAttribute(name = "right-quotes")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rightQuotes;
    @XmlAttribute(name = "right-space")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rightSpace;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String space;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String value;

    /**
     * Gets the value of the attach property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAttach() {
        return attach;
    }

    /**
     * Sets the value of the attach property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAttach(BigInteger value) {
        this.attach = value;
    }

    /**
     * Gets the value of the attachAgain property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAttachAgain() {
        return attachAgain;
    }

    /**
     * Sets the value of the attachAgain property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAttachAgain(BigInteger value) {
        this.attachAgain = value;
    }

    /**
     * Gets the value of the braces property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBraces() {
        return braces;
    }

    /**
     * Sets the value of the braces property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBraces(String value) {
        this.braces = value;
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
     * Gets the value of the fold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFold() {
        return fold;
    }

    /**
     * Sets the value of the fold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFold(String value) {
        this.fold = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the leftBraces property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeftBraces() {
        return leftBraces;
    }

    /**
     * Sets the value of the leftBraces property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeftBraces(String value) {
        this.leftBraces = value;
    }

    /**
     * Gets the value of the leftQuotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeftQuotes() {
        return leftQuotes;
    }

    /**
     * Sets the value of the leftQuotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeftQuotes(String value) {
        this.leftQuotes = value;
    }

    /**
     * Gets the value of the leftSpace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeftSpace() {
        return leftSpace;
    }

    /**
     * Sets the value of the leftSpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeftSpace(String value) {
        this.leftSpace = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the quotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuotes() {
        return quotes;
    }

    /**
     * Sets the value of the quotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuotes(String value) {
        this.quotes = value;
    }

    /**
     * Gets the value of the rightBraces property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightBraces() {
        return rightBraces;
    }

    /**
     * Sets the value of the rightBraces property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightBraces(String value) {
        this.rightBraces = value;
    }

    /**
     * Gets the value of the rightQuotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightQuotes() {
        return rightQuotes;
    }

    /**
     * Sets the value of the rightQuotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightQuotes(String value) {
        this.rightQuotes = value;
    }

    /**
     * Gets the value of the rightSpace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightSpace() {
        return rightSpace;
    }

    /**
     * Sets the value of the rightSpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightSpace(String value) {
        this.rightSpace = value;
    }

    /**
     * Gets the value of the space property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpace() {
        return space;
    }

    /**
     * Sets the value of the space property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpace(String value) {
        this.space = value;
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
