package org.internship.system.files;

import org.internship.system.exceptions.FileLoadException;
import org.internship.system.exceptions.FileSaveException;
import org.internship.system.staff.Worker;
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
    private static final String WORKER_FIRST_NAME = "name";
    private static final String WORKER_LAST_NAME = "lastName";
    private static final String WORKER_PATRONYMIC = "patronymic";

    private static final String WORKER_ATTRIBUTE = "id";


    //Создание тегов для тега "worker".
    private Node createWorkerNodeForXml(Document doc, String name, String value){
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }


    //Создание объекта работника в xml.
    private Node createWorkerForXml(Document doc, String id, String lastName, String firstName, String patronymic){
        Element worker = doc.createElement(WORKER_ELEMENT);
        worker.setAttribute(WORKER_ATTRIBUTE, id);

        worker.appendChild(createWorkerNodeForXml(doc, WORKER_ID, id));
        worker.appendChild(createWorkerNodeForXml(doc, WORKER_LAST_NAME, lastName));
        worker.appendChild(createWorkerNodeForXml(doc, WORKER_FIRST_NAME, firstName));
        worker.appendChild(createWorkerNodeForXml(doc, WORKER_PATRONYMIC, patronymic));

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
                String lastName = worker.getLastName();
                String firstName = worker.getFirstName();
                String patronymic = worker.getPatronymic();
                rootElement.appendChild(createWorkerForXml(doc, id, lastName, firstName, patronymic));
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
        String lastName;
        String firstName;
        String patronymic;
        int id;
        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            lastName = element.getElementsByTagName(WORKER_LAST_NAME).item(0).getTextContent();
            firstName = element.getElementsByTagName(WORKER_FIRST_NAME).item(0).getTextContent();
            patronymic = element.getElementsByTagName(WORKER_PATRONYMIC).item(0).getTextContent();
            id = Integer.parseInt(element.getElementsByTagName(WORKER_ID).item(0).getTextContent());
            return new Worker(id, lastName, firstName, patronymic);
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
