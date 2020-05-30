package org.sid.metier;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dao.TypeDocRepository;
import org.sid.entities.Type_Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TypeDocMetierImpl implements ITypeDocMetier{
	
	@Autowired
	private TypeDocRepository typeDocRepository ;

	@Override
	public Type_Doc createDoc(Type_Doc doc) {
		return typeDocRepository.save(doc);
	}

	@Override
	public Type_Doc updateDoc(Type_Doc doc) {
		return  typeDocRepository.save(doc);
	}

	@Override
	public void deleteDoc(Type_Doc doc) {
		typeDocRepository.delete(doc);
	}

	@Override
	public Type_Doc findDocById(long id) {
		//return typeDocRepository
		return null ;
	}

	@Override
	public List<Type_Doc> getAllDocs() {
		return typeDocRepository.findAll();
	}

}
