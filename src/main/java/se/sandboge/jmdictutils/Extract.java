package se.sandboge.jmdictutils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * IMPORTANT!
 *  RUN WITH -Djdk.xml.entityExpansionLimit=0
 *
 * Use with jmdict (http://edrdg.org/jmdict/j_jmdict.html)
 */
public class Extract {
    private static final String GENERIC = "/jtransform.xsl";

    public static void main(String[] args) {
        String sourcePath = args[0];
        String transform = GENERIC;

        if (args.length == 2 && args[1].equals("generic")) {
            transform = GENERIC;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String text = parseFile(transform, sourcePath, factory);

        System.out.println(text);
    }

    private static String parseFile(String template, String file, DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(file));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource ss = new StreamSource(Extract.class.getResourceAsStream(template));
            Transformer transformer = transformerFactory.newTransformer(ss);

            DOMSource domSource = new DOMSource(document);
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(domSource, result);
            return stringWriter.toString();

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
