package se.sandboge.kanjidicutils;

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
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Convert {
    private static final String GENERIC = "/transform.xsl";
    private static final String GAKUSEI_KANJIS = "/transform_gk.xsl";

    public static void main(String[] args) {
        String sourcePath = "/kanjidic2.xml";
        String transform = GAKUSEI_KANJIS;

        if (args.length == 1 && args[0].equals("generic")) {
            transform = GENERIC;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String text = parseFile(transform, sourcePath, factory);

        if (!transform.equals(GENERIC)) {
            text = checkEntries(text);
            storeData(text);
        }
        //System.out.println(text);
    }

    private static String checkEntries(String text) {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            result.append((checkLine(scanner.nextLine())));
        }
        return result.toString();
    }

    private static String checkLine(String s) {
        String[] splits = s.split("\\$");
        if (splits.length != 8) {
            return "";
        }
        if (splits[6].length() > 31) {
            splits[6] = splits[6].substring(0, 32);
        }
        if (splits[7].length() > 31) {
            splits[7] = splits[7].substring(0, 32);
        }
        String text = "";
        for (String p : splits) {
            text += p + '$';
        }
        return text.substring(0, text.length() - 1) + '\n';
    }

    private static String parseFile(String template, String file, DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(Convert.class.getResourceAsStream(file));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource ss = new StreamSource(Convert.class.getResourceAsStream(template));
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

    private static void storeData(String text) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gakusei?user=&password=");
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO contentschema.kanjis(kanji, id, description, hidden, english, swedish)" +
                            "VALUES (?,?,?,?,?,?)");
            for (String line : text.split(Pattern.quote("\n"))) {
                System.out.print(line);
                String[] items = line.split(Pattern.quote("$"));
                System.out.println(items.length);
                statement.setString(1, items[0]);
                statement.setString(2, items[1]);
                statement.setString(3, items[6]);
                statement.setBoolean(4, false);
                statement.setString(5, items[7]);
                statement.setString(6, items[7]);
                statement.execute();
            }

            Statement resStmt = connection.createStatement();
            ResultSet resultSet = resStmt.executeQuery("SELECT COUNT(*) FROM contentschema.kanjis");
            resultSet.next();
            System.out.println(resultSet.getLong(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
