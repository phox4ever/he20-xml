
import java.io.*;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Parser {
    public static void main(String[] args) {
        Document doc = getDocFromFile("ISO_3166.xml");
        parse(doc);
    }

    public static Document getDocFromFile(String fileName) {
        URL resource = Parser.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            //Parser that produces DOM object trees from XML content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //API to obtain DOM Document instance
            DocumentBuilder builder = null;
            try {
                //Create DocumentBuilder with default configuration
                builder = factory.newDocumentBuilder();

                return builder.parse(new InputSource(new FileReader(resource.getFile())));
            } catch (SAXException | IOException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void parseXML(String xmlStr) {
        //Use method to convert XML string content to XML Document object
        Document doc = convertStringToXMLDocument(xmlStr);
        parse(doc);
    }

    private static void parse(Document doc) {
        Element root = (Element) doc.getDocumentElement();
        Database db = null;
        try {
            db = new Database();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < doc.getElementsByTagName("country").getLength(); i++) {
            Element country = (Element) root.getElementsByTagName("country").item(i);
            ArrayList columns = new ArrayList();
            ArrayList values = new ArrayList();
            for (int j = 0; j < country.getAttributes().getLength(); j++) {
                columns.add(country.getAttributes().item(j).getNodeName());
                values.add(country.getAttributes().item(j).getNodeValue());
                System.out.println(country.getAttributes().item(j).getNodeName() + " = " + country.getAttributes().item(j).getNodeValue());
            }
            db.writeDB("country", columns, values);
            System.out.println();
        }
        try {
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
