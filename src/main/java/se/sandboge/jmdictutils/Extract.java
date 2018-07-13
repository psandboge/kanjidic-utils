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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * IMPORTANT!
 *  RUN WITH -Djdk.xml.entityExpansionLimit=0
 *
 * Use with jmdict (http://edrdg.org/jmdict/j_jmdict.html)
 */
public class Extract {
    private static final String JTRANSFORM＿SV = "/jtransform＿sv.xsl";

    public static void main(String[] args) {
        String sourcePath = args[0];
        String transform = JTRANSFORM＿SV;

        if (args.length == 2 && args[1].equals("generic")) {
            transform = JTRANSFORM＿SV;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String text = parseFile(transform, sourcePath, factory);

        List<Row> rows = splitText(text);
        for (Row row :
                rows) {
            System.out.println(row);
        }
    }

    private static List<Row> splitText(String text) {
        List<Row> rows = new ArrayList<>();
        String[] rowStrings = text.split(Pattern.quote("\n"));
        for (String rowString :
                rowStrings) {
            List<Row> row = splitRow(rowString);
            rows.addAll(row);
        }
        return rows;
    }

    private static List<Row> splitRow(String rowString) {
        String[] items = rowString.split(Pattern.quote("€"));
        List<Row> row = new ArrayList<>();
        if (items.length < 4) {
            System.out.println("Line err: " + rowString);
            return row;
        } else if (items.length > 4) {
            int index = 0;
            for(int i = 0; i < 4; i++) {
                index = rowString.indexOf('€', index) + 1;
            }
            index --;
            System.out.println(rowString + ", " + index);
            row.addAll(splitRow(rowString.substring(index + 1)));
        }
        List<String> en = splitWords(items[3]);
        List<String> sv = splitWords(items[2]);
        List<String> writing = splitWords(items[0]);
        List<String> reading = splitWords(items[1]);
        row.add(new Row(reading, writing, sv, en));
        return row;
    }

    private static List<String> splitWords(String item) {
        String[] words = item.split(Pattern.quote(","));
        return Arrays.asList(words);
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

    private static class Row {
        List<String> reading;
        List<String> writing;
        List<String> sv;
        List<String> en;

        public Row(List<String> reading, List<String> writing, List<String> sv, List<String> en) {
            this.reading = reading;
            this.writing = writing;
            this.sv = sv;
            this.en = en;
        }

        @Override
        public String toString() {
            String result = "w:";
            result += asString(writing);
            result += ";r:";
            result += asString(reading);
            result += ";s:";
            result += asString(sv);
            result += ";e:";
            result += asString(en);
            return result;
        }

    }

    static private String asString(List<String> reading) {
        StringBuilder sb = new StringBuilder();
        for (String s :
                reading) {
            sb.append(s);
        }
        return sb.toString();
    }
}
