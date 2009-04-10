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
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{}ACTION"/>
 *         &lt;element ref="{}WAIT"/>
 *       &lt;/choice>
 *       &lt;attribute name="DESCRIPTION" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="TEST-SHEET" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="TEST-ID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="TEST-SUITE" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="TYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "actionOrWAIT"
})
@XmlRootElement(name = "TEST")
public class TEST {

    @XmlElements({
        @XmlElement(name = "WAIT", type = WAIT.class),
        @XmlElement(name = "ACTION", type = ACTION.class)
    })
    protected List<Object> actionOrWAIT;
    @XmlAttribute(name = "DESCRIPTION", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String description;
    @XmlAttribute(name = "TEST-SHEET")
    @XmlSchemaType(name = "anySimpleType")
    protected String testsheet;
    @XmlAttribute(name = "TEST-ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String testid;
    @XmlAttribute(name = "TEST-SUITE")
    @XmlSchemaType(name = "anySimpleType")
    protected String testsuite;
    @XmlAttribute(name = "TYPE", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String type;

    /**
     * Gets the value of the actionOrWAIT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionOrWAIT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getACTIONOrWAIT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WAIT }
     * {@link ACTION }
     * 
     * 
     */
    public List<Object> getACTIONOrWAIT() {
        if (actionOrWAIT == null) {
            actionOrWAIT = new ArrayList<Object>();
        }
        return this.actionOrWAIT;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTION() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTION(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the testsheet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTSHEET() {
        return testsheet;
    }

    /**
     * Sets the value of the testsheet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTSHEET(String value) {
        this.testsheet = value;
    }

    /**
     * Gets the value of the testid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTID() {
        return testid;
    }

    /**
     * Sets the value of the testid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTID(String value) {
        this.testid = value;
    }

    /**
     * Gets the value of the testsuite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTESTSUITE() {
        return testsuite;
    }

    /**
     * Sets the value of the testsuite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTESTSUITE(String value) {
        this.testsuite = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPE(String value) {
        this.type = value;
    }

}
