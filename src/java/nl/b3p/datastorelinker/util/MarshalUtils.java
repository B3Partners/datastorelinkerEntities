/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.b3p.datastorelinker.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import net.sourceforge.stripes.util.Log;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Erik van de Pol
 */
public class MarshalUtils {
    private final static Log log = Log.getInstance(MarshalUtils.class);

    private final static String JAXB_ELEMENTS_PACKAGE = "nl.b3p.datastorelinker.entity";

    public static String marshalProcess(nl.b3p.datastorelinker.entity.Process process) throws JAXBException {
        return marshal(process, null);
    }

    public static String marshalProcess(nl.b3p.datastorelinker.entity.Process process, Schema schema) throws JAXBException {
        return marshal(process, schema);
    }

    public static String marshal(Object object) throws JAXBException {
        return marshal(object, null);
    }

    public static String marshal(Object object, Schema schema) throws JAXBException {
        /*ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(MarshalUtils.class.getClassLoader());

        log.debug("savedClassLoader: " + savedClassLoader.toString());
        log.debug("this.getClass().getClassLoader(): " + MarshalUtils.class.getClassLoader().toString());
*/
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_ELEMENTS_PACKAGE);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.setProperty("jaxb.formatted.output", true);

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static JAXBElement unmarshal(org.w3c.dom.Document xmlDocument, Class clazz) throws JAXBException {
        return unmarshal(xmlDocument, clazz, null);
    }

    public static JAXBElement unmarshal(org.w3c.dom.Document xmlDocument, Class clazz, Schema schema) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_ELEMENTS_PACKAGE);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);

        return unmarshaller.unmarshal(xmlDocument, clazz);
    }

    public static JAXBElement unmarshal(Document xmlDocument, Class clazz) throws JAXBException, JDOMException {
        return unmarshal(xmlDocument, clazz, null);
    }

    public static JAXBElement unmarshal(Document xmlDocument, Class clazz, Schema schema) throws JAXBException, JDOMException {
        // transform to w3c dom to be able to use jaxb to unmarshal.
        DOMOutputter domOutputter = new DOMOutputter();
        org.w3c.dom.Document w3cDomDoc = domOutputter.output(xmlDocument);

        return unmarshal(w3cDomDoc, clazz, schema);
    }

    public static nl.b3p.datastorelinker.entity.Process unmarshalProcess(Document xmlDocument) throws JAXBException, JDOMException {
        return unmarshalProcess(xmlDocument, null);
    }

    public static nl.b3p.datastorelinker.entity.Process unmarshalProcess(Document xmlDocument, Schema schema) throws JAXBException, JDOMException {
        JAXBElement<nl.b3p.datastorelinker.entity.Process> jaxbProcess =
                unmarshal(xmlDocument, nl.b3p.datastorelinker.entity.Process.class, schema);
        return jaxbProcess.getValue();
    }

    public static nl.b3p.datastorelinker.entity.Process unmarshalProcess(String xmlDocument) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        return unmarshalProcess(xmlDocument, null);
    }

    public static nl.b3p.datastorelinker.entity.Process unmarshalProcess(String xmlDocument, Schema schema) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        /*ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(MarshalUtils.class.getClassLoader());

        log.debug("savedClassLoader: " + savedClassLoader.toString());
        log.debug("this.getClass().getClassLoader(): " + MarshalUtils.class.getClassLoader().toString());
*/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        org.w3c.dom.Document w3cXmlDocument =
                documentBuilder.parse(new InputSource(new StringReader(xmlDocument)));

        JAXBElement<nl.b3p.datastorelinker.entity.Process> jaxbProcess =
                unmarshal(w3cXmlDocument, nl.b3p.datastorelinker.entity.Process.class, schema);
        return jaxbProcess.getValue();
    }

}
