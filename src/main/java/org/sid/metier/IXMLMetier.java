package org.sid.metier;

import org.sid.entities.Liasse;
import org.sid.entities.PartSocial;
import org.sid.entities.XML;

import java.io.File;

public interface IXMLMetier {

    XML createXMLFile( String FileName );
    void writeInXMLFile( File file );

    void createLiasseXML(String fileName , Liasse liasse);

}
