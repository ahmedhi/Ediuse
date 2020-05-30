package org.sid.metier;

import org.sid.entities.Entreprise;

import java.util.List;

public interface ICompanyMetier {

    Entreprise createCompany( Entreprise comp );
    Entreprise updateCompany( Entreprise comp );
    void deleteCompany( Long id );
    List<Entreprise> getAllCompany();

}
