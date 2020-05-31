package org.sid.metier;

import org.sid.dao.CompanyRepository;
import org.sid.entities.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyMetierImpl implements ICompanyMetier{

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Company createCompany(Company comp) {
        return companyRepository.save( comp );
    }

    @Override
    public Company updateCompany(Company comp) {
        return companyRepository.save( comp );
    }

    @Override
    public Company findCompanyById(long id) {
        return companyRepository.findById( id );
    }

    @Override
    public void deleteCompany(Company comp) {
        companyRepository.delete(comp);
    }

    @Override
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }


}
