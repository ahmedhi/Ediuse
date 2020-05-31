package org.sid.metier;

import org.sid.entities.DocCompany;

import java.util.List;

public interface ITaxMetier {

    DocCompany createTax(DocCompany docCompany);
    DocCompany updateTax(DocCompany docCompany);
    void deleteTax( DocCompany docCompany);
    List<DocCompany> getAllTaxes();

}
