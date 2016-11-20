package de.fhaachen.swegrp2.models;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.Scanner;
import java.util.Locale;
/*Alle öffentlichen Importfunktionen erhalten einen String mit dem Pfad des zu lesenden Dokuments
* und geben ein 2-Dimensionales Integer Array zurück. Desweiteren werden diverse Exceptions geworfen,
* die beim Aufrufer gefangen werden müssen
* */
public class Import {

    public Import () {
    }

    public int[][] importXML (String path) throws Exception {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        int size = Integer.parseInt(doc.getDocumentElement().getAttribute("size"));
        int[][] arr = new int[size][size];
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

    public int[][] importCSV (String path) throws Exception {
        Scanner scanner = new Scanner(new File(path), "UTF-8");
        scanner.useLocale(Locale.GERMANY);
        int size = Integer.parseInt(scanner.nextLine().split(";")[0]);
        int[][] arr = new int[size][size];
        for (int y = 0; y < size; y++) {
            if (scanner.hasNextLine()) {
                String[] cols = scanner.nextLine().split(";");
                for (int x = 0; x < size; x++)
                    arr[y][x] = Integer.parseInt(cols[x]);
            }
        }
        return arr;
    }
}
