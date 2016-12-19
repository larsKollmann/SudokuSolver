package de.fhaachen.swegrp2.models;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static de.fhaachen.swegrp2.controllers.SudokuController.isSizeSupported;

/**<p><b>Titel:</b> Export</p>
 * <p><b>Beschreibung:</b> Bietet Methoden zum export gegebener Sudokus im 2D int Array Format
 * mit gegeben Dateipfad in diverse XML, CSV, JSON und PDF.</p>
 */
public class Export {
    public Export () {}

    /**
     * Exportiert ein gegebenes Sudoku ins XML-Format
     * @param arr Das Sudoku im 2D int Array Format
     * @param path Der Dateipfad zu dem exportiert werden soll
     * @throws Exception Warnung vor fehlerhaften Sudoku Array
     */
    public void exportXML(int arr[][], String path) throws Exception {
        try {
            int size = arr.length;
            if (!isSizeSupported(size))
                throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Sudoku");
            doc.appendChild(rootElement);
            Attr attr = doc.createAttribute("size");
            attr.setValue(String.valueOf(size));
            rootElement.setAttributeNode(attr);

            for (int i = 0; i < size; i++) {
                // row elements
                Element row = doc.createElement("row");
                rootElement.appendChild(row);

                for (int y = 0; y < size; y++) {
                    // column elements
                    Element col = doc.createElement("col");
                    if (arr[i][y] > 0 && arr[i][y] < size)
                        col.appendChild(doc.createTextNode(String.valueOf(arr[i][y])));
                    row.appendChild(col);
                }

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * Exportiert ein gegebenes Sudoku ins CSV-Format
     * @param arr Das Sudoku im 2D int Array Format
     * @param path Der Dateipfad zu dem exportiert werden soll
     * @throws Exception Warnung vor fehlerhaften Sudoku Array sowie IOExcepton
     */
    public void exportCSV(int arr[][], String path) throws Exception {
        int size = arr.length;
        if (!isSizeSupported(size))
            throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(arr[i][j] + "");
                if (j < size - 1)
                    builder.append(",");
            }
            builder.append("\n");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(builder.toString());
        writer.close();
    }

    /**
     * Exportiert ein gegebenes Sudoku ins JSON-Format
     * @param arr Das Sudoku im 2D int Array Format
     * @param path Der Dateipfad zu dem exportiert werden soll
     * @throws Exception Warnung vor fehlerhaften Sudoku Array sowie IOException
     */
    public void exportJSON(int arr[][], String path) throws Exception {
        int size = arr.length;
        if (!isSizeSupported(size))
            throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");

        StringBuilder builder = new StringBuilder();
        builder.append("{\r\n");
        builder.append("  \"size\": " + size + ",\r\n");
        builder.append("  \"sudoku\": [\r\n");

        for (int i = 0; i < size; i++) {
            builder.append("    ");
            for (int j = 0; j < size; j++) {
                builder.append(arr[i][j]);
                if (!(i == size - 1 && j == arr.length - 1)) {
                    builder.append(",");
                }
            }
            builder.append("\r\n");
        }
        builder.append("  ]\r\n");
        builder.append("}");

        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(builder.toString());
        writer.close();
    }

}
