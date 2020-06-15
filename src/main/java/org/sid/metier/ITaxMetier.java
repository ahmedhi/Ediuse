package org.sid.metier;

import org.sid.entities.Balance;
import org.sid.entities.ChartOfAccounts;
import org.sid.entities.DocCompany;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITaxMetier {

    DocCompany createTax(DocCompany docCompany);
    DocCompany updateTax(DocCompany docCompany);
    void deleteTax( DocCompany docCompany);
    List<DocCompany> getAllTaxes();

    boolean addChartOfAccounts(MultipartFile file);

    List<ChartOfAccounts> getAllChartOfAccounts();

    Balance addBalance(Balance balance );
    List<Balance> addBalance( MultipartFile file );

    Balance updateBalance( Balance balance );
    List<Balance> updateBalance( MultipartFile file );

    Balance deleteBalance( Balance balance );

}
