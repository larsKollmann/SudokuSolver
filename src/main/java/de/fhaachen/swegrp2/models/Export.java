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


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by lars on 17.11.16.
 */
public class Export {

	public static void ExportXML(int size, int wert[][], String path) {
		try {

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
					if (wert[i][y] > 0 && wert[i][y] < size)
						col.appendChild(doc.createTextNode(String.valueOf(wert[i][y])));
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
	
	
    public static void ExportCSV(int wert[][]) throws Exception {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < wert.length; i++) {
      for (int j = 0; j < wert.length; j++) {
        builder.append(wert[i][j] + "");
        if (j < wert.length - 1)
          builder.append(",");
      }
      builder.append("\n");
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter("export.csv"));
    writer.write(builder.toString());
    writer.close();
  }

}
