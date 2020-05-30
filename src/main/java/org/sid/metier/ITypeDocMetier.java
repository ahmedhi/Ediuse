package org.sid.metier;

import java.util.List;

import org.sid.entities.Type_Doc;



public interface ITypeDocMetier {
	
	Type_Doc createDoc(Type_Doc doc);
	Type_Doc updateDoc (Type_Doc doc);
	void deleteDoc(Type_Doc doc);
	Type_Doc findDocById(long id);
	List<Type_Doc> getAllDocs();

}
