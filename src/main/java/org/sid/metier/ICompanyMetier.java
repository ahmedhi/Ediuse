package org.sid.metier;

import org.sid.entities.Company;

import java.util.List;

public interface ICompanyMetier {

    Company createCompany(Company comp );
    Company updateCompany( Company comp );
    Company findCompanyById( long id );
    void deleteCompany( Company comp );
    List<Company> getAllCompany();

}
