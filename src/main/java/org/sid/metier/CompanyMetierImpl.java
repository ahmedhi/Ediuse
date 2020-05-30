package org.sid.metier;

import org.sid.dao.EntrepriseRepository;
import org.sid.entities.Entreprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyMetierImpl implements ICompanyMetier{

    @Autowired EntrepriseRepository entrepriseRepository;

    @Override
    public Entreprise createCompany(Entreprise comp) {
        return entrepriseRepository.save( comp );
    }

    @Override
    public Entreprise updateCompany(Entreprise comp) {
        return entrepriseRepository.save( comp );
    }

    @Override
    public void deleteCompany(Long id) {
    }

    @Override
    public List<Entreprise> getAllCompany() {
        return entrepriseRepository.findAll();
    }
}
