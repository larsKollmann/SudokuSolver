package de.fhaachen.swegrp2.models;

import java.io.*;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.ExceptionSuite.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.util.Scanner;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static de.fhaachen.swegrp2.controllers.SudokuController.isSizeSupported;

/**
 * Alle Öffentlichen Importfunktionen erhalten ein File Object welches ihnen vom Controller, durch die GUI,
 * durch einen FileChooser übergeben wird. Das Importierte Sudoku wird gespeichert,
 * damit der Controller auf dieses zurueckgreifen kann.
 * Desweiteren werden diverse Exceptions geworfen,
 * die beim Aufrufer gefangen werden müssen.
 *
 * TODO: Excpetions erweitern, anständig definieren und implementieren
 */
public class Import {
    //Interne Funktionen
    private int[][] convertJSONArrayTo2DIntArray(JSONArray jsonArray, int dimensions) {
        int arr[][] = new int[dimensions][dimensions];
        for (int i = 0, col = 0, row = 0; i < dimensions * dimensions; i++) {
            if (col == dimensions) {
                col = 0;
                row++;
            }
            arr[row][col] = Integer.parseInt(jsonArray.get(i).toString());
            col++;
        }

        return arr;
    }

    public Import() {}

    public SudokuField importXML(String path) throws Exception {
        File inputFile = new File(path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        int size = Integer.parseInt(doc.getDocumentElement().getAttribute("size"));
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Nicht unterstützte Groeße!");
        SudokuField field = new SudokuField(size);
        //field.SetIsSysGen(Boolean.parseBoolean(doc.getDocumentElement().getAttribute("sysgen")));

        NodeList rows = doc.getElementsByTagName("row");
        if (rows.getLength() != size) throw new FaultyFormatException("Anzahl Zeilen stimmt mit Arraygroeße nicht ueberein!");

        for (int r = 0; r < size; r++) {
            NodeList cols = ((Element)rows.item(r)).getElementsByTagName("col");
            if (cols.getLength() != size) throw new FaultyFormatException("Anzahl Spalten stimmt mit Arraygroeße nicht ueberein!");

            for (int c = 0; c < size; c++) {
                if (cols.item(c).getTextContent().equals(""))
                    field.setFieldValue(r, c, 0);
                else {
                    field.setFieldValue(r, c, Integer.parseInt(cols.item(c).getTextContent()));
                    //Hat das Feld ein sysgen Attribut = true?
                    /*if(field.getIsSysGen()) {
                        Node sys = cols.item(c).getAttributes().getNamedItem("sys");
                        if (sys != null)
                            field.setFieldSysGen(r, c, Boolean.parseBoolean(sys.getNodeValue()));
                    }*/
                }
            }
        }

        return field;
    }

    public SudokuField importCSV(String path) throws Exception {
        Scanner scanner = new Scanner(new File(path), "UTF-8");
        scanner.useLocale(Locale.GERMANY);


        String[] header = scanner.nextLine().split(";");
        int size = Integer.parseInt(header[0].replaceAll("\\D+",""));
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Nicht unterstützte Groeße!");
        int field[][] = new int[size][size];
        //field.SetIsSysGen(header[1].equals("sysgen"));

        for (int y = 0; y < size; y++) {
            if (scanner.hasNextLine()) {
                String[] cols = scanner.nextLine().split(";");
                if (cols.length > size) throw new FaultyFormatException("Anzahl Spalten stimmt mit Arraygroeße nicht ueberein!");
                for (int x = 0; x < size; x++) {
                    /*if(cols[x].contains("s")){
                        cols[x] = cols[x].replace("s","");
                        field.setFieldSysGen(y, x, true);
                    }*/
                    if (x < cols.length && !cols[x].equals(""))
<<<<<<< HEAD
                        field.setFieldValue(y, x, Integer.parseInt(cols[x]));
=======
                        field[y][x] = Integer.parseInt(cols[x]);
>>>>>>> origin/import_dev
                }
            }
        }
        if (scanner.hasNextLine()) throw new FaultyFormatException("Es sind zu viele Zeilen beschrieben!");

        return new SudokuField(field);
    }

    public SudokuField importJSON(String path) throws Exception {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(path));
        JSONObject jsonObject = (JSONObject) obj;

        int size = Integer.parseInt(jsonObject.get("size").toString());
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Fehlerhafte Größe");

        JSONArray sudokuJSONArray = (JSONArray) jsonObject.get("sudoku");
        if (sudokuJSONArray == null) throw new EmptyArrayException("Übergebenes Sudoku leer");
        if (sudokuJSONArray.size() != size*size) throw new FaultyFormatException("Sudokugröße entspricht nicht der angegebenen Größe");

        int arr[][] = convertJSONArrayTo2DIntArray(sudokuJSONArray, size);

        return new SudokuField(arr);
    }
}
