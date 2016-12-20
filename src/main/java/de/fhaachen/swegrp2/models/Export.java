package de.fhaachen.swegrp2.models;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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

import de.fhaachen.swegrp2.controllers.SudokuField;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static de.fhaachen.swegrp2.controllers.SudokuController.isSizeSupported;

/**
 * <p><b>Titel:</b> Export</p>
 * <p><b>Beschreibung:</b> Bietet Methoden zum export gegebener Sudokus im 2D int Array Format
 * mit gegeben Dateipfad in XML, CSV, JSON und PDF.</p>
 */
public class Export {
    public Export() {
    }

    /**
     * Exportiert ein gegebenes SudokuField als XML in einen gegebenen Dateipfad.
     *
     * @param field Das zu exportierende Sudoku
     * @param path  Der Dateipfad.
     * @throws Exception Lese-& Schreibefehler sowie fehlerhaftes SudokuField.
     */
    public void exportXML(SudokuField field, String path) throws Exception {
        int size = field.getSize();
        if (!isSizeSupported(size)) throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");
        int arr[][] = field.getSudokuField();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("sudoku");
        doc.appendChild(rootElement);

        Attr attr = doc.createAttribute("size");
        attr.setValue(String.valueOf(size));
        rootElement.setAttributeNode(attr);

        for (int y = 0; y < size; y++) {
            // row elements
            Element row = doc.createElement("row");
            rootElement.appendChild(row);
            for (int x = 0; x < size; x++) {
                // column elements
                Element col = doc.createElement("col");
                if (arr[y][x] > 0 && arr[y][x] < size)
                    col.appendChild(doc.createTextNode(String.valueOf(arr[y][x])));
                row.appendChild(col);
            }
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
    }

    /**
     * Exportiert ein gegebenes SudokuField als XML in einen gegebenen Dateipfad.
     *
     * @param field Das zu exportierende Sudoku
     * @param path  Der Dateipfad.
     * @throws Exception Lese-& Schreibefehler sowie fehlerhaftes SudokuField.
     */
    public void exportCSV(SudokuField field, String path) throws Exception {
        int arr[][] = field.getSudokuField();
        int size = field.getSize();
        if (!isSizeSupported(size)) throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");

        StringBuilder builder = new StringBuilder();

        builder.append(size + ";\n");
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                builder.append(arr[y][x] + "");
                if (x < size - 1)
                    builder.append(";");
            }
            builder.append("\n");
        }

        exportToDisk(builder, path);
    }

    /**
     * Exportiert ein gegebenes SudokuField als JSON in einen gegebenen Dateipfad.
     *
     * @param field Das zu exportierende Sudoku
     * @param path  Der Dateipfad.
     * @throws Exception Lese-& Schreibefehler sowie fehlerhaftes SudokuField.
     */
    public void exportJSON(SudokuField field, String path) throws Exception {
        int arr[][] = field.getSudokuField();
        int size = field.getSize();
        if (!isSizeSupported(size)) throw new ExceptionSuite.SizeNotSupportedException("Nicht unterstützte Groeße!");

        StringBuilder builder = new StringBuilder();
        builder.append("{\r\n");
        builder.append("  \"size\": " + size + ",\r\n");
        builder.append("  \"sudoku\": [\r\n");

        for (int y = 0; y < size; y++) {
            builder.append("    ");
            for (int x = 0; x < size; x++) {
                builder.append(arr[y][x]);
                if (!(y == size - 1 && x == size - 1)) {
                    builder.append(",");
                }
            }
            builder.append("\r\n");
        }
        builder.append("  ]\r\n");
        builder.append("}");

        exportToDisk(builder, path);
    }

    private void exportToDisk(StringBuilder field, String path) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(field.toString());
        writer.close();
    }

    private void exportSnapshotAsPNG(String path, WritableImage png) throws IOException {
        WritableImage image = png;
        File pngFile = new File(path);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", pngFile);
    }

    private void insertPNGintoPDF(BufferedImage png, PDDocument pdf) throws Exception {
        PDPage page = new PDPage();
        pdf.addPage(page);
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(pdf, png);
        PDPageContentStream contentStream = new PDPageContentStream(pdf, page, true, false);
        contentStream.drawImage(pdImageXObject, 50, 150, 500, 500);
        contentStream.close();
    }
    public void exportPDF(String path, WritableImage png) {

        File pngFile = new File("temp.png");
        File pdfFile = new File(path);

        try {
            exportSnapshotAsPNG(pngFile.getPath(), png);
            BufferedImage bufferedImagePng = ImageIO.read(pngFile);
            PDDocument pdf = new PDDocument();
            insertPNGintoPDF(bufferedImagePng, pdf);
            pdf.save(pdfFile.getPath());
            pdf.close();
            pngFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
