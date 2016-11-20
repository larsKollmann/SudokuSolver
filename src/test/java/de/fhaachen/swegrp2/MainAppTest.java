package de.fhaachen.swegrp2;

import static org.junit.Assert.assertEquals;
import de.fhaachen.swegrp2.models.Import;
import org.junit.Test;



class MyClass {
    int multiply(int a, int b) {
        return a*b;
    }
}

public class MainAppTest {

    @Test
    public void sollteXMLimportieren() {
        try {
            Import xml = new Import();
            int[][] arr = xml.importXML("src/test/java/test.xml");
            for (int y = 0; y < arr.length; y++) {
                for (int x = 0; x < arr.length; x++) {
                    System.out.print(arr[y][x]);
                }
                System.out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void sollteCSVimportieren () {
        try {
            Import csv = new Import();
            int arr[][] = csv.importCSV("src/test/java/test.csv");
            for (int y = 0; y < arr.length; y++) {
                for (int x = 0; x < arr.length; x++) {
                    System.out.print(arr[y][x]);
                }
                System.out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
