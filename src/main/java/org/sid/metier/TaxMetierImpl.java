package org.sid.metier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sid.dao.ChartOfAccountsRepository;
import org.sid.dao.DocCompanyRepository;
import org.sid.entities.ChartOfAccounts;
import org.sid.entities.DocCompany;
import org.sid.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class TaxMetierImpl implements ITaxMetier {
    @Autowired private DocCompanyRepository docCompanyRepository;
    @Autowired private ChartOfAccountsRepository chartOfAccountsRepository;

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
