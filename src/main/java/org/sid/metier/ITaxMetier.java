package org.sid.metier;

import org.sid.entities.Balance;
import org.sid.entities.Bilan;
import org.sid.entities.ChartOfAccounts;
import org.sid.entities.Company;
import org.sid.entities.DocCompany;
import org.springframework.web.multipart.MultipartFile;

import java.time.Year;
import java.util.List;

public interface ITaxMetier {

    DocCompany createTax(DocCompany docCompany);
    DocCompany updateTax(DocCompany docCompany);
    void deleteTax( DocCompany docCompany);
    DocCompany getById( Long Id );
    List<DocCompany> getAllTaxes();

    boolean addChartOfAccounts(MultipartFile file);

    List<ChartOfAccounts> getAllChartOfAccounts();

    Balance addBalance(Balance balance );
    List<Balance> addBalance(MultipartFile file , Company company , Year year);

    Balance updateBalance( Balance balance );
    List<Balance> updateBalance( MultipartFile file );

    Balance deleteBalance( Balance balance );
    
    List<Bilan> generateBilanActif(List<Balance> balance) ;
    List<Bilan> generateBilanPassif(List<Balance> balance) ;
    List<Bilan> generateCPC(List<Balance> balance) ;


    List<Balance> getBalance(Long ref );

}
