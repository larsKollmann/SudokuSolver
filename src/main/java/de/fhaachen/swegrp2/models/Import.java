package de.fhaachen.swegrp2.models;

import java.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.util.Scanner;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Alle Öffentlichen Importfunktionen erhalten einen String mit dem Pfad des zu lesenden Dokuments
 * und geben ein 2-Dimensionales Integer Array zurück. Desweiteren werden diverse Exceptions geworfen,
 * die beim Aufrufer gefangen werden müssen
 */
public class Import {

    public Import() { }

    public int[][] importXML(String path) throws Exception {
        File inputFile = new File(path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        int size = Integer.parseInt(doc.getDocumentElement().getAttribute("size"));
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Nicht unterstützte Groeße!");
        int[][] arr = new int[size][size];

        NodeList rows = doc.getElementsByTagName("row");
        if (rows.getLength() != size) throw new FaultyFormatException("Anzahl Zeilen stimmt mit Arraygroeße nicht ueberein!");

        for (int r = 0; r < size; r++) {
            NodeList cols = ((Element)rows.item(r)).getElementsByTagName("col");
            if (cols.getLength() != size) throw new FaultyFormatException("Anzahl Spalten stimmt mit Arraygroeße nicht ueberein!");

            for (int c = 0; c < size; c++) {
                if (!cols.item(r).getTextContent().equals(""))
                    arr[c][r] = Integer.parseInt(cols.item(r).getTextContent());
                else
                    arr[c][r] = 0;
            }
        }

        return arr;
    }

    public int[][] importCSV(String path) throws Exception {
        Scanner scanner = new Scanner(new File(path), "UTF-8");
        scanner.useLocale(Locale.GERMANY);

        int size = Integer.parseInt(scanner.nextLine().split(";")[0].replaceAll("\\D+",""));
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Nicht unterstützte Groeße!");
        int[][] arr = new int[size][size];

        for (int y = 0; y < size; y++) {
            if (scanner.hasNextLine()) {
                String[] cols = scanner.nextLine().split(";");
                if (cols.length > size) throw new FaultyFormatException("Anzahl Spalten stimmt mit Arraygroeße nicht ueberein!");
                for (int x = 0; x < size; x++) {
                    if (x < cols.length && !cols[x].equals(""))
                        arr[y][x] = Integer.parseInt(cols[x]);
                    else
                        arr[y][x] = 0;
                }
            }
            else {
                throw new FaultyFormatException("Es sind zu wenige Zeilen beschrieben!");
            }
        }
        if (scanner.hasNextLine()) throw new FaultyFormatException("Es sind zu viele Zeilen beschrieben!");

        return arr;
    }

    public int[][] importJSON(String path) throws Exception {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(path));
        JSONObject jsonObject = (JSONObject) obj;

        int size = Integer.parseInt(jsonObject.get("size").toString());
        if (!isSizeSupported(size)) throw new SizeNotSupportedException("Fehlerhafte Größe");

        JSONArray sudokuJSONArray = (JSONArray) jsonObject.get("sudoku");
        if (sudokuJSONArray == null) throw new EmptyArrayException("Übergebenes Sudoku leer");
        if (sudokuJSONArray.size() != size*size) throw new SizeNotSupportedException("Sudokugröße entspricht nicht der angegebenen Größe");

        int arr[][] = convertJSONArrayTo2DIntArray(sudokuJSONArray, size);

        return arr;
    }

    public int[][] convertJSONArrayTo2DIntArray(JSONArray jsonArray, int dimensions) {
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

    public boolean isSizeSupported(int size) {
        return (size == 9 || size == 16 || size == 25 || size == 36);
    }

    class SizeNotSupportedException extends Exception {
        SizeNotSupportedException(String err) {
            super(err);
        }
    }

    class FaultyFormatException extends Exception {
        FaultyFormatException (String err) {
            super(err);
        }
    }

    class EmptyArrayException extends Exception {
        EmptyArrayException(String err) {
            super(err);
        }
    }
}
