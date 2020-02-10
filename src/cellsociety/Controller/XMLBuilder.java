package cellsociety.Controller;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLBuilder {

    public void buildXML(String simulationName, String simWidth, String simHeight, String numCellType0, String stateCellType0, String cellType0,
                          String numCellType1, String stateCellType1, String cellType1, String defaultVal) {
     try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // root elements
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("config");
      doc.appendChild(rootElement);

      // set attribute to root
      Attr attr = doc.createAttribute("config");
      attr.setValue("config");
      rootElement.setAttributeNode(attr);

      // title elements
      Element title = doc.createElement("title");
      title.appendChild(doc.createTextNode("CS308 Sim"));
      rootElement.appendChild(title);

      // author elements
      Element author = doc.createElement("author");
      author.appendChild(doc.createTextNode("Team 19"));
      rootElement.appendChild(author);

      // simulation elements
      Element simulation = doc.createElement("simulation");
      simulation.appendChild(doc.createTextNode(simulationName));
      rootElement.appendChild(simulation);

      // width elements
      Element width = doc.createElement("width");
      width.appendChild(doc.createTextNode(simWidth));
      rootElement.appendChild(width);

       // height elements
       Element height = doc.createElement("height");
       height.appendChild(doc.createTextNode(simHeight));
       rootElement.appendChild(height);

       // numcelltype0 elements
       Element numcelltype0 = doc.createElement("numcelltype0");
       numcelltype0.appendChild(doc.createTextNode(numCellType0));
       rootElement.appendChild(numcelltype0);

       // statecelltype0 elements
       Element statecelltype0 = doc.createElement("statecelltype0");
       statecelltype0.appendChild(doc.createTextNode(stateCellType0));
       rootElement.appendChild(statecelltype0);

       // statecelltype0 elements
       Element celltype0 = doc.createElement("celltype0");
       celltype0.appendChild(doc.createTextNode(cellType0));
       rootElement.appendChild(celltype0);

       // numcelltype1 elements
       Element numcelltype1 = doc.createElement("numcelltype1");
       numcelltype1.appendChild(doc.createTextNode(numCellType1));
       rootElement.appendChild(numcelltype1);

       // statecelltype1 elements
       Element statecelltype1 = doc.createElement("statecelltype1");
       statecelltype1.appendChild(doc.createTextNode(stateCellType1));
       rootElement.appendChild(statecelltype1);

       // statecelltype1 elements
       Element celltype1 = doc.createElement("celltype1");
       celltype1.appendChild(doc.createTextNode(cellType1));
       rootElement.appendChild(celltype1);

      // default elements
      Element defaultNum = doc.createElement("default");
      defaultNum.appendChild(doc.createTextNode(defaultVal));
      rootElement.appendChild(defaultNum);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File("./Resources/savedConfig.xml"));

      // Output to console for testinga
      // StreamResult result = new StreamResult(System.out);

      transformer.transform(source, result);

      System.out.println("File saved!");

      } catch (ParserConfigurationException | TransformerException pce) {
         throw new XMLException("Failure to save XML file");
      }
    }
}
