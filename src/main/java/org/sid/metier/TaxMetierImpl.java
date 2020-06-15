package org.sid.metier;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sid.dao.BalanceRepository;
import org.sid.dao.ChartOfAccountsRepository;
import org.sid.dao.DocCompanyRepository;
import org.sid.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Year;
import java.util.*;

@Service
@Transactional
public class TaxMetierImpl implements ITaxMetier {
    @Autowired private DocCompanyRepository docCompanyRepository;
    @Autowired private ChartOfAccountsRepository chartOfAccountsRepository;
    @Autowired private BalanceRepository balanceRepository;

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

    @Override
    public DocCompany getById( Long Id ){
        return docCompanyRepository.findTopByIdDoc( Id );
    }

    @Override
    public boolean addChartOfAccounts(MultipartFile file) {
        boolean isFlag = false ;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx") ) {
            isFlag = readDataFromExcel(file);
        }
        return isFlag ;
    }

    @Override
    public List<ChartOfAccounts> getAllChartOfAccounts() {
        return chartOfAccountsRepository.findAll();
    }

    @Override
    public Balance addBalance(Balance balance) {
        return balanceRepository.save( balance );
    }

    @Override
    public List<Balance> addBalance(MultipartFile file , Company company , Year year) {
        //Create DocCompany
        DocCompany tmp = docCompanyRepository.save( new DocCompany( company , null , new Date() , year));
        //Read data from excel file
        List<Balance> dataBalance = readBalanceFromExcel( file , tmp.getIdDoc() );
        dataBalance.forEach( this::addBalance );
        return dataBalance;
    }

    @Override
    public Balance updateBalance(Balance balance) {
        return null;
    }

    @Override
    public List<Balance> updateBalance(MultipartFile file) {
        return null;
    }

    @Override
    public Balance deleteBalance(Balance balance) {
        return null;
    }

    @Override
    public List<Balance> getBalance(Long ref) {
        return balanceRepository.findByRefBalance( ref );
    }

    private ArrayList<Balance> readBalanceFromExcel( MultipartFile file , Long Ref){
        ArrayList<Balance> tmp = new ArrayList<Balance>();
        Workbook workbook = getWorkBook(file);
        Sheet sheet =  workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        while(rows.hasNext()) {
            Row row = rows.next();

            //Read from file the Cel that we need
            Long compte = Long.parseLong( row.getCell( 0 ).getStringCellValue() );
            String libelle = row.getCell(1).getStringCellValue();
            double solde = row.getCell(6).getNumericCellValue();
            int type;
            if( solde == 0 ){
                solde = row.getCell(7).getNumericCellValue();
                type = 0;
            }
            else type = 1;

            tmp.add( new Balance( Ref , compte , libelle , solde , type ) );
        }
        return tmp;
    }

    private boolean readDataFromExcel(MultipartFile file) {
        Workbook workbook = getWorkBook(file);
        Sheet sheet =  workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        while(rows.hasNext()) {
            Row row = rows.next();

                //Read from file the Cel that we need
                Integer refAccount = (int) row.getCell( 2 ).getNumericCellValue();
                Integer refClass = Integer.parseInt(Integer.toString(refAccount).substring(0, 1));
                String wording = row.getCell( 3 ).getStringCellValue();

            //Check if the information already exist in the DB
            if( chartOfAccountsRepository.findByRefClassAndRefAccount( refClass , refAccount ) == null){
                ChartOfAccounts chartOfAccounts = new ChartOfAccounts();
                chartOfAccounts.setRefClass( refClass );
                chartOfAccounts.setRefAccount( refAccount );
                chartOfAccounts.setWording( wording );

                chartOfAccountsRepository.save( chartOfAccounts );
            }

        }
        return true;
    }

    private Workbook getWorkBook(MultipartFile file) {
        Workbook  workbook = null ;
        String  extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try{
            if(extension.equalsIgnoreCase("xlsx")) {
                workbook  = new XSSFWorkbook(file.getInputStream());
            }else if (extension.equalsIgnoreCase("xls")) {
                workbook  = new HSSFWorkbook(file.getInputStream());
            }
        }catch(Exception e ) {
            e.printStackTrace();
        }
        return workbook;
    }
}
