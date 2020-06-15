package org.sid.metier;

import java.util.List;

import org.sid.entities.Balance;
import org.sid.entities.DocType;



public interface ITypeDocMetier {
	
	DocType createDoc(DocType doc);
	DocType updateDoc (DocType doc);
	void deleteDoc(DocType doc);
	DocType findDocById(long id);
	List<DocType> getAllDocs();


}
