package org.sid.metier;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dao.DocTypeRepository;
import org.sid.entities.DocType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DocTypeMetierImpl implements ITypeDocMetier{
	
	@Autowired
	private DocTypeRepository docTypeRepository ;

	@Override
	public DocType createDoc(DocType doc) {
		return docTypeRepository.save(doc);
	}

	@Override
	public DocType updateDoc(DocType doc) {
		return  docTypeRepository.save(doc);
	}

	@Override
	public void deleteDoc(DocType doc) {
		docTypeRepository.delete(doc);
	}

	@Override
	public DocType findDocById(long id) {
		return null ;
	}

	@Override
	public List<DocType> getAllDocs() {
		return docTypeRepository.findAll();
	}

}
