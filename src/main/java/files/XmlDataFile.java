package files;

import db.DatabaseManagement;
import exceptions.FileLoadException;
import exceptions.FileSaveException;
import staff.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class XmlDataFile implements DataFile {
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder documentBuilder;


    //Имена всех тегов xml-файлов.
    private static final String ROOT_ELEMENT = "Staff";
    private static final String WORKER_ELEMENT = "worker";
    private static final String WORKER_ID = "id";
    private static final String WORKER_NAME = "name";

    private static final String WORKER_ATTRIBUTE = "id";


    //Создание тегов для тега "worker".
    private Node createWorkerNodeForXml(Document doc, String name, String value){
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }


    //Создание объекта работника в xml.
    private Node createWorkerForXml(Document doc, String id, String name){
        Element worker = doc.createElement(WORKER_ELEMENT);
        worker.setAttribute(WORKER_ATTRIBUTE, id);

        worker.appendChild(createWorkerNodeForXml(doc, WORKER_ID, id));
        worker.appendChild(createWorkerNodeForXml(doc, WORKER_NAME, name));

        return worker;
    }


    @Override
    public void saveToFile(String path, Set<Worker> allWorker) throws FileSaveException {
        File fileXml = new File(path);
        try{
            documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element rootElement = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(rootElement);

            for(Worker worker: allWorker){
                String id = Integer.toString(worker.getId());
                String name = worker.getName();
                rootElement.appendChild(createWorkerForXml(doc, id, name));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(fileXml);
            transformer.transform(source, file);

        } catch (ParserConfigurationException | TransformerException e) {
            throw new FileSaveException("Ошибка сохранения XML файла");
        }
    }


    //Создание исходного объекта Worker из объекта xml
    private Worker buildWorker(Node node) throws FileLoadException {
        String name;
        int id;
        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            name = element.getElementsByTagName(WORKER_NAME).item(0).getTextContent();
            id = Integer.parseInt(element.getElementsByTagName(WORKER_ID).item(0).getTextContent());
            return new Worker(id, name);
        }
        throw new FileLoadException("Неправильная запись xml файла");
    }

    @Override
    public Set<Worker> loadFormFile(String path) throws FileLoadException {
        File fileXml = new File(path);
        Set<Worker> allWorker = new LinkedHashSet<Worker>();
        try{
            documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(fileXml);

            NodeList nodeList = doc.getElementsByTagName(WORKER_ELEMENT);
            for(int i = 0; i < nodeList.getLength(); i++){
                allWorker.add(buildWorker(nodeList.item(i)));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new FileLoadException("Ошибка загрузки XML");
        }
        return allWorker;
    }
}
