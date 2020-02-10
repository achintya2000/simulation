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

    private Element myRootElement;

    public void buildXML(String simulationName, String simWidth, String simHeight, String numCellType0, String stateCellType0, String cellType0,
                          String numCellType1, String stateCellType1, String cellType1, String defaultVal) {
     try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("config");
      myRootElement = rootElement;
      doc.appendChild(myRootElement);
      setConfigElements(simulationName, simWidth, simHeight, doc);
      setCellType0Elements(numCellType0, stateCellType0, cellType0, doc, "numcelltype0", "statecelltype0", "celltype0");
      setCellType1Elements(numCellType1, stateCellType1, cellType1, doc, "numcelltype1", "statecelltype1", "celltype1");
      Element defaultNum = doc.createElement("default");
      defaultNum.appendChild(doc.createTextNode(defaultVal));
      myRootElement.appendChild(defaultNum);
      transformDoc(doc);
      System.out.println("File saved!");
      } catch (ParserConfigurationException | TransformerException pce) {
         throw new XMLException("Failure to save XML file");
      }
    }

 private void transformDoc(Document doc) throws TransformerException {
  TransformerFactory transformerFactory = TransformerFactory.newInstance();
  Transformer transformer = transformerFactory.newTransformer();
  DOMSource source = new DOMSource(doc);
  StreamResult result = new StreamResult(new File("./Resources/savedConfig.xml"));
  transformer.transform(source, result);
 }

 private void setCellType1Elements(String numCellType1, String stateCellType1, String cellType1, Document doc, String numcelltype12, String statecelltype12, String celltype12) {
  // numcelltype1 elements
  Element numcelltype1 = doc.createElement(numcelltype12);
  numcelltype1.appendChild(doc.createTextNode(numCellType1));
  myRootElement.appendChild(numcelltype1);

  // statecelltype1 elements
  Element statecelltype1 = doc.createElement(statecelltype12);
  statecelltype1.appendChild(doc.createTextNode(stateCellType1));
  myRootElement.appendChild(statecelltype1);

  // statecelltype1 elements
  Element celltype1 = doc.createElement(celltype12);
  celltype1.appendChild(doc.createTextNode(cellType1));
  myRootElement.appendChild(celltype1);
 }

 private void setCellType0Elements(String numCellType0, String stateCellType0, String cellType0, Document doc, String numcelltype02, String statecelltype02, String celltype02) {
  // numcelltype0 elements
  Element numcelltype0 = doc.createElement(numcelltype02);
  numcelltype0.appendChild(doc.createTextNode(numCellType0));
  myRootElement.appendChild(numcelltype0);

  // statecelltype0 elements
  Element statecelltype0 = doc.createElement(statecelltype02);
  statecelltype0.appendChild(doc.createTextNode(stateCellType0));
  myRootElement.appendChild(statecelltype0);

  // statecelltype0 elements
  Element celltype0 = doc.createElement(celltype02);
  celltype0.appendChild(doc.createTextNode(cellType0));
  myRootElement.appendChild(celltype0);
 }

 private void setConfigElements(String simulationName, String simWidth, String simHeight, Document doc) {
  // set attribute to root
  Attr attr = doc.createAttribute("config");
  attr.setValue("config");
  myRootElement.setAttributeNode(attr);

  // title elements
  Element title = doc.createElement("title");
  title.appendChild(doc.createTextNode("CS308 Sim"));
  myRootElement.appendChild(title);

  // author elements
  Element author = doc.createElement("author");
  author.appendChild(doc.createTextNode("Team 19"));
  myRootElement.appendChild(author);

  // simulation elements
  Element simulation = doc.createElement("simulation");
  simulation.appendChild(doc.createTextNode(simulationName));
  myRootElement.appendChild(simulation);

  // width elements
  Element width = doc.createElement("width");
  width.appendChild(doc.createTextNode(simWidth));
  myRootElement.appendChild(width);

  // height elements
  Element height = doc.createElement("height");
  height.appendChild(doc.createTextNode(simHeight));
  myRootElement.appendChild(height);
 }
}
