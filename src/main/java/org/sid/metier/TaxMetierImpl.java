package org.sid.metier;

import org.sid.dao.DocCompanyRepository;
import org.sid.entities.DocCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TaxMetierImpl implements ITaxMetier {
    @Autowired private DocCompanyRepository docCompanyRepository;

    @Override
    public DocCompany createTax(DocCompany docCompany) {
        return docCompanyRepository.save(docCompany);
    }

    @Override
    public DocCompany updateTax(DocCompany docCompany) {
        return docCompanyRepository.save(docCompany);
    }

    @Override
    public void deleteTax(DocCompany docCompany) {
        docCompanyRepository.delete(docCompany);
    }

    @Override
    public List<DocCompany> getAllTaxes() {
        return docCompanyRepository.findAll();
    }
}
