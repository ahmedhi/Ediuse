package org.sid.entities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XML {

    private File file;
    private String path;
    private DocumentBuilder builder;

    public XML(String fileName) throws ParserConfigurationException, IOException {
        File dir = new File("tmp/");
        dir.mkdirs();
        File tmp = new File(dir, fileName + ".xml");
        tmp.createNewFile();
        this.file = tmp;
        this.path = "src/main/resources/" + fileName;
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DocumentBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }

}
