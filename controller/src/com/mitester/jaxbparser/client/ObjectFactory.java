//
// This file was com.mitester.jaxbparser.client by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.02.21 at 02:33:54 AM EST 
//


package com.mitester.jaxbparser.client;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * com.mitester.jaxbparser.client in the com.mitester.jaxbparser.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mitester.jaxbparser.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WAIT }
     * 
     */
    public WAIT createWAIT() {
        return new WAIT();
    }

    /**
     * Create an instance of {@link PARAM }
     * 
     */
    public PARAM createPARAM() {
        return new PARAM();
    }

    /**
     * Create an instance of {@link ACTION }
     * 
     */
    public ACTION createACTION() {
        return new ACTION();
    }

    /**
     * Create an instance of {@link TEST }
     * 
     */
    public TEST createTEST() {
        return new TEST();
    }

    /**
     * Create an instance of {@link TESTFLOW }
     * 
     */
    public TESTFLOW createTESTFLOW() {
        return new TESTFLOW();
    }

}
