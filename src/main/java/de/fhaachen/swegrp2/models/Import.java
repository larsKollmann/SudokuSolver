package de.fhaachen.swegrp2.models;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Import {

    public Import () {

    }

    public int[][] importXML (String path) {
        Document doc = null;
        int[][] arr;
        int size;
        try {
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        size = Integer.parseInt(doc.getDocumentElement().getAttribute("size"));
        arr = new int[size][size];
        NodeList rows = doc.getElementsByTagName("row");
        if (rows.getLength() == size) {
            for (int r = 0; r < size; r++) {
                Element e = (Element) rows.item(r);
                for (int c = 0; c < size; c++) {
                    if (!e.getElementsByTagName("col").item(r).getTextContent().equals(""))
                        arr[c][r] = Integer.parseInt(e.getElementsByTagName("col").item(r).getTextContent());
                    else
                        arr[c][r] = 0;
                }
            }
        }
        return arr;
    }
}
