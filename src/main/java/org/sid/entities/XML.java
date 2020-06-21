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

    public void getLiasse(Liasse liasse){

        try{
            Document doc;
            /**
             * Create a new file if not exist
             */
            if(!file.exists()){
                Files.createDirectories(file.toPath().getParent());
                Files.createFile(file.toPath());
            }
            doc = createLiasse(liasse);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }catch (TransformerException | IOException e){
            e.printStackTrace();
        }
    }

    private Document createLiasse( Liasse liasse ){
        Document doc = builder.newDocument();

        Element liasseSuite = doc.createElement("Liasse");

        //modele
        Element modeleSuite = doc.createElement("modele");
        Element tmp = doc.createElement( "id");
        tmp.setTextContent( String.valueOf(liasse.getId()) );
        modeleSuite.appendChild( tmp );

        //resultatFiscal
        Element resultatFiscalSuite = doc.createElement("resultatFiscal");
        tmp = doc.createElement("identifiantFiscal");
        tmp.setTextContent( liasse.getIF() );
        resultatFiscalSuite.appendChild( tmp );
        tmp = doc.createElement("exerciceFiscalDu");
        tmp.setTextContent( liasse.getStartDate() );
        resultatFiscalSuite.appendChild( tmp );
        tmp = doc.createElement( "exerciceFiscalAu" );
        tmp.setTextContent( liasse.getEndDate() );
        resultatFiscalSuite.appendChild( tmp );

        //Groupe Valeurs Tableau
        Element grpValue = doc.createElement("groupeValeursTableau");
        Element valuesTab = doc.createElement("ValeursTableau");
        //tableau info
        tmp = doc.createElement("tableau");
        Element valueTab = doc.createElement("id");
        valueTab.setTextContent("NOT DEFINE");
        tmp.appendChild( valueTab );
        //groupeValeurs
        tmp = doc.createElement("groupeValeurs");


        //Add init xml
        doc.appendChild(liasseSuite);
        liasseSuite.appendChild(modeleSuite); //add model
        liasseSuite.appendChild( resultatFiscalSuite ); //add resultatFisacal
        return doc;
    }

}
