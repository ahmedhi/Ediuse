package org.sid.metier;

import org.sid.entities.Balance;
import org.sid.entities.Bilan;
import org.sid.entities.Liasse;
import org.sid.entities.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XMLMetierImpl implements IXMLMetier{

    XML xml;

    @Override
    public void createLiasseXML( String fileName , Liasse liasse) {
        try {
            xml = new XML( fileName );
            Document doc;
            File file = xml.getFile();
            /**
             * Create a new file if not exist
             */
            if(!file.exists()){
                Files.createDirectories(file.toPath().getParent());
                Files.createFile(file.toPath());
            }
            doc = createLiasse( xml , liasse);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private Document createLiasse( XML xml , Liasse liasse ){
        Document doc = xml.getBuilder().newDocument();

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
            //BILAN ACTIF
            Element valuesTab = doc.createElement("ValeursTableau");
                //tableau info
                tmp = doc.createElement("tableau");
                Element valueTab = doc.createElement("id");
                valueTab.setTextContent("BILAN ACTIF");
                tmp.appendChild( valueTab );
            valuesTab.appendChild( tmp );
                //groupeValeurs
                tmp = doc.createElement("groupeValeurs");
                    //ValeurCellule
                    for(Bilan balance : liasse.getBilanActif()){
                        // create a new valeurCellule component
                        for( int i = 0 ; i < 3 ; i++ ) {
                            Element valeurCellule = doc.createElement("ValeurCellule");
                                //create a new sub-component for cellule
                                valueTab = doc.createElement("cellule");
                                    /**
                                     * Add EDI code
                                     * Actuellement nous avons pas la table des codes EDI
                                     * Nous avons décidé en tant qu'équipe dev AskBri
                                     * d'insérer le libellé de la ligne et son type dans le code
                                     */
                                    //Add a sub-component of EdiCode
                                    Element subElement = doc.createElement("codeEdi");
                                    switch ( i ){
                                        case 0: {
                                            subElement.setTextContent(balance.getLibelle() + " Brut");
                                            break;
                                        }
                                        case 1: {
                                            subElement.setTextContent(balance.getLibelle() + " Amortissement");
                                            break;
                                        }
                                        case 2: {
                                            subElement.setTextContent(balance.getLibelle() + " Net");
                                            break;
                                        }
                                    }
                                // Add codeEdi to cellule
                                valueTab.appendChild(subElement);
                            //Add cellule to valeurCellule
                            valeurCellule.appendChild(valueTab);
                                //Add new sub-component for valeur
                                valueTab = doc.createElement("valeur");
                                //Set value base on if it's brut amortissement or net component
                                switch ( i ){
                                    case 0: {
                                        valueTab.setTextContent(String.valueOf(balance.getBrut()));
                                        break;
                                    }
                                    case 1: {
                                        valueTab.setTextContent(String.valueOf(balance.getAmort()));
                                        break;
                                    }
                                    case 2: {
                                        valueTab.setTextContent(String.valueOf(balance.getNet()));
                                        break;
                                    }
                                }
                            //Add Valeur to valeurCellule
                            valeurCellule.appendChild(valueTab);

                            //Add all component to groupeValeurs
                            tmp.appendChild(valeurCellule);
                        }
                    }
            valuesTab.appendChild( tmp );

            /*----------------------------------------------------------------------------*/

            //BILAN PASSIF
            valuesTab = doc.createElement("ValeursTableau");
                //tableau info
                tmp = doc.createElement("tableau");
                valueTab = doc.createElement("id");
                valueTab.setTextContent("BILAN PASSIF");
                tmp.appendChild( valueTab );
            valuesTab.appendChild( tmp );
                //groupeValeurs
                tmp = doc.createElement("groupeValeurs");
                //ValeurCellule
                    for(Bilan balance : liasse.getBilanPassif()){
                        // create a new valeurCellule component
                            Element valeurCellule = doc.createElement("ValeurCellule");
                                //create a new sub-component for cellule
                                valueTab = doc.createElement("cellule");
                                    /**
                                     * Add EDI code
                                     * Actuellement nous avons pas la table des codes EDI
                                     * Nous avons décidé en tant qu'équipe dev AskBri
                                     * d'insérer le libellé de la ligne et son type dans le code
                                     */
                                    //Add a sub-component of EdiCode
                                    Element subElement = doc.createElement("codeEdi");
                                    subElement.setTextContent(balance.getLibelle());
                                // Add codeEdi to cellule
                                valueTab.appendChild(subElement);
                                //Add cellule to valeurCellule
                            valeurCellule.appendChild(valueTab);

                                //Add new sub-component for valeur
                                valueTab = doc.createElement("valeur");
                                valueTab.setTextContent(String.valueOf(balance.getBrut()));
                            //Add Valeur to valeurCellule
                            valeurCellule.appendChild(valueTab);

                            //Add all component to groupeValeurs
                            tmp.appendChild(valeurCellule);
            }
            valuesTab.appendChild( tmp );

            /*---------------------------------------------------------*/

            //BILAN ACTIF
            valuesTab = doc.createElement("ValeursTableau");
                //tableau info
                tmp = doc.createElement("tableau");
                valueTab = doc.createElement("id");
                valueTab.setTextContent("BILAN ACTIF");
                tmp.appendChild( valueTab );
            valuesTab.appendChild( tmp );
                //groupeValeurs
                tmp = doc.createElement("groupeValeurs");
                //ValeurCellule
                for(Bilan balance : liasse.getCpc()){
                // create a new valeurCellule component
                for( int i = 0 ; i < 3 ; i++ ) {
                    Element valeurCellule = doc.createElement("ValeurCellule");
                    //create a new sub-component for cellule
                    valueTab = doc.createElement("cellule");
                    /**
                     * Add EDI code
                     * Actuellement nous avons pas la table des codes EDI
                     * Nous avons décidé en tant qu'équipe dev AskBri
                     * d'insérer le libellé de la ligne et son type dans le code
                     */
                    //Add a sub-component of EdiCode
                    Element subElement = doc.createElement("codeEdi");
                    switch ( i ){
                        case 0: {
                            subElement.setTextContent(balance.getLibelle() + " Propre à l'exercice");
                            break;
                        }
                        case 1: {
                            subElement.setTextContent(balance.getLibelle() + " Exercice precedent");
                            break;
                        }
                        case 2: {
                            subElement.setTextContent(balance.getLibelle() + " Total de l'exercice");
                            break;
                        }
                    }
                    // Add codeEdi to cellule
                    valueTab.appendChild(subElement);
                    //Add cellule to valeurCellule
                    valeurCellule.appendChild(valueTab);
                    //Add new sub-component for valeur
                    valueTab = doc.createElement("valeur");
                    //Set value base on if it's brut amortissement or net component
                    switch ( i ){
                        case 0: {
                            valueTab.setTextContent(String.valueOf(balance.getBrut()));
                            break;
                        }
                        case 1: {
                            valueTab.setTextContent(String.valueOf(balance.getAmort()));
                            break;
                        }
                        case 2: {
                            valueTab.setTextContent(String.valueOf(balance.getNet()));
                            break;
                        }
                    }
                    //Add Valeur to valeurCellule
                    valeurCellule.appendChild(valueTab);

                    //Add all component to groupeValeurs
                    tmp.appendChild(valeurCellule);
                }
            }
            valuesTab.appendChild( tmp );

        grpValue.appendChild( valuesTab );


        //Add init xml
        doc.appendChild(liasseSuite);
        liasseSuite.appendChild(modeleSuite); //add model
        liasseSuite.appendChild( resultatFiscalSuite ); //add resultatFiscal
        liasseSuite.appendChild( grpValue ); //add groupeValeursTableau
        return doc;
    }
}
