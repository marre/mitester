//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.15 at 01:06:46 PM IST 
//


package com.mitester.jaxbparser.validation;

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
 *         &lt;element ref="{}MANDATORY" maxOccurs="unbounded"/>
 *         &lt;element ref="{}OPTIONAL" maxOccurs="unbounded"/>
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
    "mandatory",
    "optional"
})
@XmlRootElement(name = "VALIDATION")
public class VALIDATION {

    @XmlElement(name = "MANDATORY", required = true)
    protected List<MANDATORY> mandatory;
    @XmlElement(name = "OPTIONAL", required = true)
    protected List<OPTIONAL> optional;

    /**
     * Gets the value of the mandatory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mandatory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMANDATORY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MANDATORY }
     * 
     * 
     */
    public List<MANDATORY> getMANDATORY() {
        if (mandatory == null) {
            mandatory = new ArrayList<MANDATORY>();
        }
        return this.mandatory;
    }

    /**
     * Gets the value of the optional property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the optional property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOPTIONAL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OPTIONAL }
     * 
     * 
     */
    public List<OPTIONAL> getOPTIONAL() {
        if (optional == null) {
            optional = new ArrayList<OPTIONAL>();
        }
        return this.optional;
    }

}