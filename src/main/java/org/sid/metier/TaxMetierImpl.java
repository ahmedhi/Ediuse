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
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.Year;
import java.util.*;

@Service
@Transactional
public class TaxMetierImpl implements ITaxMetier {
	// Repository
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

	@Override
	public List<Bilan> generateBilanActif(List<Balance> tmp) {
		
	        List<Bilan> bilanActif = new ArrayList<>();   
	        double Abrut  = 0 , Aamort = 0 , Anet  = 0 , Bbrut = 0 , Bamort = 0 , Bnet   = 0 , Cbrut  = 0 , Camort  = 0 , Cnet = 0 , Dbrut  = 0 ,
	        	   Damort = 0 , Dnet   = 0 , Ebrut = 0 , Enet  = 0 , Fbrut  = 0 , Famort = 0 , Fnet   = 0 , TAnet   = 0 , Inet = 0 , TAbrut = 0 , 
	        	   Gbrut  = 0 , Gamort = 0 , Gnet  = 0 , Hbrut = 0 , Hamort = 0 , Hnet   = 0 , Ibrut  = 0 , TAamort = 0 ,
	        	   TotalIbrut   = 0 , TotalIamort  = 0 , TotalInet   = 0 , TotalIIbrut       = 0 , TotalIIamort     = 0 , TotalIInet    = 0 ,
	        	   TotalIIIbrut = 0 , TotalIIIamort= 0 , TotalIIInet = 0 ,  TotalGeneralbrut = 0 , TotalGeneralamort= 0 ,TotalGeneralnet= 0 ,
	        	   AFPbrut = 0 , AFPamort = 0 , AFPnet = 0 , ACbrut  = 0 , ACamort  = 0 , ACnet  = 0 , APRbrut = 0 , APRamort = 0 , APRnet = 0 ,
	        	   BIbrut  = 0 , BIamort  = 0 , BInet  = 0 , BBbrut  = 0 , BBamort  = 0 , BBnet  = 0 , BFCbrut = 0 , BFCamort = 0 , BFCnet = 0 ,
	        	   BAbrut  = 0 , BAamort  = 0 , BAnet  = 0 , CTbrut  = 0 , CTamort  = 0 , CTnet  = 0 , CCbrut  = 0 , CCamort  = 0 , CCnet  = 0 ,
	        	   CIbrut  = 0 , CIamort  = 0 , CInet  = 0 , CMTbrut = 0 , CMTamort = 0 , CMTnet = 0 , CMbrut  = 0 , CMamort  = 0 , CMnet  = 0 ,
	        	   CAbrut  = 0 , CAamort  = 0 , CAnet  = 0 , CICbrut = 0 , CICamort = 0 , CICnet = 0 , DPbrut  = 0 , DPamort  = 0 , DPnet  = 0 ,
	        	   DAbrut  = 0 , DAamort  = 0 , DAnet  = 0 , DTbrut  = 0 , DTamort  = 0 , DTnet  = 0 , DATbrut = 0 , DATamort = 0 , DATnet = 0 ,
	        	   EDbrut  = 0 ,                EDnet  = 0 , EAbrut  = 0 ,                EAnet  = 0 , FMbrut  = 0 , FMamort  = 0 , FMnet  = 0 ,
	        	   FMCbrut = 0 , FMCamort = 0 , FMCnet = 0 , FPbrut  = 0 , FPamort  = 0 , FPnet  = 0 , FPPbrut = 0 , FPPamort = 0 , FPPnet = 0 ,
	        	   FPFbrut = 0 , FPFamort = 0 , FPFnet = 0 , GFbrut  = 0 , GFamort  = 0 , GFnet  = 0 , GCCbrut = 0 , GCCamort = 0 , GCCnet = 0 ,
	        	   GPbrut  = 0 , GPamort  = 0 , GPnet  = 0 , GEbrut  = 0 , GEamort  = 0 , GEnet  = 0 , GCbrut  = 0 , GCamort  = 0 , GCnet  = 0 ,
	        	   GAbrut  = 0 , GAamort  = 0 , GAnet  = 0 , GCRbrut = 0 , GCRamort = 0 , GCRnet = 0 , IEbrut  = 0 ,                IEnet  = 0 ,
	        	   TCVbrut = 0 , TCVamort = 0 , TCVnet = 0 , TBbrut  = 0 , TBamort  = 0 , TBnet  = 0 , TCRbrut = 0 , TCRamort = 0 , TCRnet = 0 ;
	        	  
	        for(int i = 0 ; i< tmp.size() ; i++) {
	        	String val =  tmp.get(i).getCompteBalance().toString();
	        	
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '1') {Abrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {AFPbrut += tmp.get(i).getSoldeBalance();}
	           		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '1') {AFPamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2') {ACbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '2') {ACamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '1' &&  val.charAt(2) == '3') {APRbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '3') {APRamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '1') {Aamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '2') {Bbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '2') {Bamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '2' &&  val.charAt(2) == '1') {BIbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '2'  &&  val.charAt(3) == '1') {BIamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '2' &&  val.charAt(2) == '2') {BBbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '2'  &&  val.charAt(3) == '2') {BBamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '2' &&  val.charAt(2) == '3') {BFCbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '2'  &&  val.charAt(3) == '3') {BFCamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '2' &&  val.charAt(2) == '8') {BAbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '2'  &&  val.charAt(3) == '8') {BAamort += tmp.get(i).getSoldeBalance();}

	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3') {Cbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3') {Camort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '1') {CTbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '1') {CTamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '2') {CCbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '2') {CCamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '3') {CIbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '3') {CIamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '4') {CMTbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '4') {CMTamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '5') {CMbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '5') {CMamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '6') {CAbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '6') {CAamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '3' &&  val.charAt(2) == '8') {CICbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '8' &&  val.charAt(2) == '3'  &&  val.charAt(3) == '8') {CICamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '2' &&  (val.charAt(1) == '4' ||val.charAt(1) == '5') ) {Dbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '9' && ( val.charAt(2) == '4' ||val.charAt(2) == '5') ) {Damort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '4' &&  val.charAt(2) == '1') {DPbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '1') {DPamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '4' &&  val.charAt(2) == '8') {DAbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '8') {DAamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1') {DTbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '9' &&  val.charAt(2) == '5'  &&  val.charAt(3) == '1') {DTamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '5' &&  val.charAt(2) == '8') {DATbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '9' &&  val.charAt(2) == '5'  &&  val.charAt(3) == '8') {DATamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '2' && val.charAt(1) == '7') {Ebrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '7' &&  val.charAt(2) == '1') {EDbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '2' &&  val.charAt(1) == '7' &&  val.charAt(2) == '2') {EAbrut+= tmp.get(i).getSoldeBalance();}

	        		if(val.charAt(0) == '3' && val.charAt(1) == '1') { Fbrut += tmp.get(i).getSoldeBalance(); }
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1') { Famort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {FMbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '1') {FMamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2') {FMCbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '2') {FMCamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '1' &&  val.charAt(2) == '3') {FPbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '3') {FPamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4') {FPPbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '4') {FPPamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '1' &&  val.charAt(2) == '5') {FPFbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '5') {FPFamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '3' && val.charAt(1) == '4') {Gbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4') {Gamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '1') {GFbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '1') {GFamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '2') {GCCbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '2') {GCCamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '3') {GPbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '3') {GPamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '5') {GEbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '5') {GEamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '6') {GCbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '6') {GCamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '8') {GAbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '8') {GAamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '4' &&  val.charAt(2) == '9') {GCRbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '4'  &&  val.charAt(3) == '9') {GCRamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '3' && val.charAt(1) == '5') {Hbrut += tmp.get(i).getSoldeBalance(); }
	        		if(val.charAt(0) == '3' &&  val.charAt(1) == '9' &&  val.charAt(2) == '5') {Hamort += tmp.get(i).getSoldeBalance();}
	        		
	        		if(val.charAt(0) == '3' && val.charAt(1) == '7') {Ibrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '3' && val.charAt(1) == '7') {IEbrut+= tmp.get(i).getSoldeBalance();}

	        		if(val.charAt(0) == '5' && val.charAt(1) == '1') {TAbrut += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1') {TAamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {TCVbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '1') {TCVamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4') {TBbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '4') {TBamort += tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6') {TCRbrut+= tmp.get(i).getSoldeBalance();}
	        		if(val.charAt(0) == '5' &&  val.charAt(1) == '9' &&  val.charAt(2) == '1'  &&  val.charAt(3) == '1') {TCRamort += tmp.get(i).getSoldeBalance();}

	        }
	        
	        
	        Anet = Abrut - Aamort ; Bilan A = new Bilan("IMMOBILISATIONS EN NON VALEURS  (A)",Abrut,Aamort,Anet,true);  bilanActif.add(A);
	        AFPnet = AFPbrut - AFPamort ;  Bilan AFP = new Bilan("Frais préliminaires",AFPbrut,AFPamort,AFPnet,false);  bilanActif.add(AFP);
	        ACnet = ACbrut - ACamort ;   Bilan AC = new Bilan("Charges à repartir sur plusieurs  exercices",ACbrut,ACamort,ACnet,false); bilanActif.add(AC);
	        APRnet = APRbrut - APRamort ;  Bilan APR = new Bilan("Primes de remboursement des obligations",APRbrut,APRamort,APRnet,false); bilanActif.add(APR);
	        
	        Bnet = Bbrut - Bamort ; Bilan B = new Bilan("IMMOBILISATIONS INCORPORELLES  (B)",Bbrut,Bamort,Bnet,true); bilanActif.add(B);
	        BInet = BIbrut - BIamort ;  Bilan BI = new Bilan("Immobilisation en recherche et développement",BIbrut,BIamort,BInet,false); bilanActif.add(BI);
	        BBnet = BBbrut - BBamort ;  Bilan BB = new Bilan("Brevets, marques, droits et valeurs similaires",BBbrut,BBamort,BBnet,false);bilanActif.add(BB);
	        BFCnet = BFCbrut - BFCamort ;  Bilan BFC = new Bilan("Fonds commercial",BFCbrut,BFCamort,BFCnet,false); bilanActif.add(BFC);
	        BAnet = BAbrut - BAamort ;  Bilan BA = new Bilan("Autres immobilisations incorporelles",BAbrut,BAamort,BAnet,false); bilanActif.add(BA);

	        Cnet = Cbrut - Camort ;  Bilan C = new Bilan("IMMOBILISATIONS CORPORELLES (C)",Cbrut,Camort,Cnet,true);  bilanActif.add(C);
	        CTnet = CTbrut - CTamort ;  Bilan CT = new Bilan("Terrains",CTbrut,CTamort,CTnet,false);  bilanActif.add(CT);
	        CCnet = CCbrut - CCamort ;  Bilan CC = new Bilan("Constructions",CCbrut,CCamort,CCnet,false);  bilanActif.add(CC);
	        CInet = CIbrut - CIamort ;  Bilan CI = new Bilan("Installations techniques, matériel et outillage",CIbrut,CIamort,CInet,false);  bilanActif.add(CI);
	        CMTnet = CMTbrut - CMTamort ;  Bilan CMT = new Bilan("Matériel transport",CMTbrut,CMTamort,CMTnet,false);  bilanActif.add(CMT);
	        CMnet = CMbrut - CMamort ;  Bilan CM = new Bilan("Mobilier, matériel de bureau et aménagements divers",CMbrut,CMamort,CMnet,false);  bilanActif.add(CM);
	        CAnet = CAbrut - CAamort ;  Bilan CA = new Bilan("Autres immobilisations corporelles",CAbrut,CAamort,CAnet,false);  bilanActif.add(CA);
	        CICnet = CICbrut - CICamort ;  Bilan CIC = new Bilan("Immobilisations corporelles en cours",CICbrut,CICamort,CICnet,false);  bilanActif.add(CIC);

	        Dnet = Dbrut - Damort ;  Bilan D = new Bilan("IMMOBILISATIONS FINANCIERES  (D)",Dbrut,Damort,Dnet,true); bilanActif.add(D);
	        DPnet = DPbrut - DPamort ;  Bilan DP = new Bilan("Prêts immobilisés",DPbrut,DPamort,DPnet,false);  bilanActif.add(DP);
	        DAnet = DAbrut - DAamort ;  Bilan DA = new Bilan("Autres créances financières",DAbrut,DAamort,DAnet,false);  bilanActif.add(DA);
	        DTnet = DTbrut - DTamort ;  Bilan DT = new Bilan("Titres de participation",DTbrut,DTamort,DTnet,false);  bilanActif.add(DT);
	        DATnet = DATbrut - DATamort ;  Bilan DAT = new Bilan("Autres titres immobilisés",DATbrut,DATamort,DATnet,false);  bilanActif.add(DAT);

	        Enet = Ebrut ; Bilan E = new Bilan("ECARTS DE CONVERSION -ACTIF  (E)",Ebrut,-999,Enet,true); bilanActif.add(E);
	        EDnet = EDbrut ;  Bilan ED = new Bilan("Diminution des créances immobilisées",EDbrut,-999,EDnet,false);  bilanActif.add(ED);
	        EAnet = EAbrut ;  Bilan EA = new Bilan("Augmentation des dettes financières",EAbrut,-999,EAnet,false);  bilanActif.add(EA);

	        TotalIbrut = Abrut +Bbrut+Cbrut+Dbrut+Ebrut ; TotalIamort = Aamort +Bamort +Camort +Damort; TotalInet = TotalIbrut - TotalIamort ;
	        Bilan totalI = new Bilan("TOTAL I (A+B+C+D+E)",TotalIbrut,TotalIamort,TotalInet,true); bilanActif.add(totalI);
	        
	        Fnet = Fbrut - Famort ;  Bilan F = new Bilan("STOCKS (F)",Fbrut,Famort,Fnet,true); bilanActif.add(F);
	        FMnet = FMbrut - FMamort ;  Bilan FM = new Bilan("Marchandises",FMbrut,FMamort,FMnet,false);  bilanActif.add(FM);
	        FMCnet = FMCbrut - FMCamort ;  Bilan FMC = new Bilan("Matières et fournitures, consommables",FMCbrut,FMCamort,FMCnet,false);  bilanActif.add(FMC);
	        FPnet = FPbrut - FPamort ;  Bilan FP = new Bilan("Produits en cours",FPbrut,FPamort,FPnet,false);  bilanActif.add(FP);
	        FPPnet = FPPbrut - FPPamort ;  Bilan FPP = new Bilan("produits intermédiaires et produits résiduels",FPPbrut,FPPamort,FPPnet,false);  bilanActif.add(FPP);
	        FPFnet = FPFbrut - FPFamort ;  Bilan FPF = new Bilan("Produits finis",FPFbrut,FPFamort,FPFnet,false);  bilanActif.add(FPF);
	 
	        Gnet = Gbrut - Gamort ; Bilan G = new Bilan("CREANCES DE L'ACTIF CIRCULANT  (G)",Gbrut,Gamort,Gnet,true); bilanActif.add(G);
	        GFnet = GFbrut - GFamort ;  Bilan GF = new Bilan("Fournis. débiteurs, avances et acomptes",GFbrut,GFamort,GFnet,false);  bilanActif.add(GF);
	        GCCnet = GCCbrut - GCCamort ;  Bilan GCC = new Bilan("Clients et comptes rattachés",GCCbrut,GCCamort,GCCnet,false);  bilanActif.add(GCC);
	        GPnet = GPbrut - GPamort ;  Bilan GP = new Bilan("Personnel",GPbrut,GPamort,GPnet,false);  bilanActif.add(GP);
	        GEnet = GEbrut - GEamort ;  Bilan GE = new Bilan("Etat",GEbrut,GEamort,GEnet,false);  bilanActif.add(GE);
	        GCnet = GCbrut - GCamort ;  Bilan GC = new Bilan("Comptes d'associés",GCbrut,GCamort,GCnet,false);  bilanActif.add(GC);
	        GAnet = GAbrut - GAamort ;  Bilan GA = new Bilan("Autres débiteurs",GAbrut,GAamort,GAnet,false);  bilanActif.add(GA);
	        GCRnet = GCRbrut - GCRamort ;  Bilan GCR = new Bilan("Comptes de régularisation-Actif",GCRbrut,GCRamort,GCRnet,false);  bilanActif.add(GCR);

	        Hnet = Hbrut - Hamort ;  Bilan H = new Bilan("TITRES VALEURS DE PLACEMENT  (H)",Hbrut,Hamort,Hnet,true); bilanActif.add(H);
	       
	        Inet = Ibrut  ; Bilan I = new Bilan("ECARTS DE CONVERSION-ACTIF  (I)",Ibrut,-999,Inet,true); bilanActif.add(I);
	        IEnet = IEbrut;  Bilan IE = new Bilan("Comptes de régularisation-Actif",IEbrut,-999,IEnet,false);  bilanActif.add(IE);

	        TotalIIbrut = Fbrut+Gbrut+Hbrut+Ibrut ; TotalIIamort = Famort +Gamort +Hamort ; TotalIInet =  TotalIIbrut -  TotalIIamort ;
	        Bilan totalII = new Bilan("TOTAL II  ( F+G+H+I )",TotalIIbrut,TotalIIamort,TotalIInet,true); bilanActif.add(totalII);
	       
	        TAnet = TAbrut - TAamort ; Bilan TA = new Bilan("TRESORERIE-ACTIF",TAbrut,TAamort,TAnet,true); bilanActif.add(TA);
	        TCVnet = TCVbrut - TCVamort ;  Bilan TCV = new Bilan("Chéques et valeurs à encaisser",TCVbrut,TCVamort,TCVnet,false);bilanActif.add(TCV);
	        TBnet = TBbrut - TBamort ;  Bilan TB = new Bilan(" Banques, TG et CCP",TBbrut,TBamort,TBnet,false);  bilanActif.add(TB);
	        TCRnet = TCRbrut - TCRamort ;  Bilan TCR = new Bilan(" Caisse, Régie d'avances et accréditifs",TCRbrut,TCRamort,TCRnet,false);  bilanActif.add(TCR);

	        TotalIIIbrut = TAbrut ; TotalIIIamort = TAamort ; TotalIIInet = TotalIIIbrut - TotalIIIamort ;
	        Bilan totalIII = new Bilan("TOTAL III",TotalIIIbrut,TotalIIIamort,TotalIIInet,true);  bilanActif.add(totalIII);
	        
	        TotalGeneralbrut = TotalIbrut + TotalIIbrut + TotalIIIbrut ; TotalGeneralamort = TotalIamort + TotalIIamort + TotalIIIamort ;
	        TotalGeneralnet = TotalInet + TotalIInet + TotalIIInet;
	        Bilan TG = new Bilan("TOTAL GENERAL I+II+III",TotalGeneralbrut,TotalGeneralamort,TotalGeneralnet,true);  bilanActif.add(TG);
	        
		return bilanActif;
	}

	@Override
	public List<Bilan> generateCPC(List<Balance> tmp) {
		
        List<Bilan> cpc = new ArrayList<>();   
        double  VM  = 0 , VB   = 0 , VS   = 0 , IP   = 0 , SE   = 0 , AP   = 0 , RE   = 0 , ARM = 0 , ACM  = 0 , ACE  = 0 , IT  = 0 , CP  = 0 , 
        		ACH = 0 , DE   = 0 , PT   = 0 , GC   = 0 , IA   = 0 , RF   = 0 , CFC  = 0 , CFP  = 0 , CFA = 0 , CFD  = 0 , VMP = 0 , VBP = 0 , 
        		SEP = 0 , APP  = 0 , REP  = 0 , ARMP = 0 , ACMP = 0 , ACEP = 0 , ITP  = 0 , CPP = 0 , ACHP = 0 , DEP  = 0 , PTP = 0 , GCP = 0 , 
        		RFP = 0 , CFCP = 0 , CFPP = 0 , CFAP = 0 , CFDP = 0 , PCI  = 0 , SEN  = 0 , RSI = 0 , APN  = 0 , RNC  = 0 , VNA = 0 , SA  = 0 , 
                DNC = 0 , PCIP = 0 , SENP = 0 , RSIP = 0 , APNP = 0 , RNCP = 0 , VNAP = 0 , SAP = 0 , ACNP = 0 , DNCP = 0 , VSP = 0 , IPP = 0 ,
                IAP = 0 , ACN  = 0 , RN   = 0 , RNP  = 0 , ResultatCourantP= 0 , ResultatAvantImpot = 0 ,
                TotalI       = 0 , TotalII     = 0 , RESULTATEXP  = 0 , TotalIV      = 0 , TotalV   = 0 , TotalVIII = 0 ,  ResultatCourant     = 0 ,
                TotalCharge  = 0 , ResultatFin = 0 , TotalIX      = 0 , TotalProd    = 0 , TotalIIP = 0 , TotalIP   = 0 ,  ResultatNonCourantP = 0 ,
                RESULTATEXPP = 0 , TotalVIIIP  = 0 , ResultatNet  = 0 , TotalChargeP = 0 , TotalVP  = 0 , TotalIXP  = 0 ,  ResultatNonCourant  = 0 ,
                ResultatNetP = 0 , TotalProdP  = 0 , ResultatFinP = 0 , TotalIVP     = 0 , ImpotP   = 0 , Impot     = 0 ,  ResultatAvantImpotP = 0 ;
                 
        for(int i = 0 ; i< tmp.size() ; i++) {
        	String val =  tmp.get(i).getCompteBalance().toString();

        	if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1' && val.charAt(3) == '8' ) {VMP += tmp.get(i).getSoldeBalance();}
        	else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {VM += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2' && val.charAt(3) == '8') {VBP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2') {VB += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '3') {VS += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4' && val.charAt(3) == '8') {IPP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4') {IP += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6' && val.charAt(3) == '8') {SEP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6') {SE += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '8' && val.charAt(3) == '8') {APP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '8') {AP += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '9' && val.charAt(3) == '8') {REP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '1' &&  val.charAt(2) == '9') {RE += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1' && val.charAt(3) == '8') {ARMP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {ARM += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2' && val.charAt(3) == '8') {ACMP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2') {ACM += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4' && val.charAt(3) == '8') {ACEP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '3') {ACE += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6' && val.charAt(3) == '8') {ITP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6') {IT += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '7' && val.charAt(3) == '8') {CPP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '7') {CP += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '8' && val.charAt(3) == '8') {ACHP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '8') {ACH += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '9' && val.charAt(3) == '8') {DEP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '1' &&  val.charAt(2) == '9') {DE += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '2' &&  val.charAt(3) == '8') {PTP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '2') {PT += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '3' &&  val.charAt(3) == '8') {GCP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '3') {GC += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '8' &&  val.charAt(3) == '8') {IAP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '8') {IA += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '9' &&  val.charAt(3) == '8') {RFP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '3' &&  val.charAt(2) == '9') {RF += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '1' && val.charAt(3) == '8') {CFCP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '1') {CFC += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '3' && val.charAt(3) == '8') {CFPP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '3') {CFP += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '8' && val.charAt(3) == '8') {CFAP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '8') {CFA += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '9' && val.charAt(3) == '8') {CFDP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '3' &&  val.charAt(2) == '9') {CFD += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1' && val.charAt(3) == '8') {PCIP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1') {PCI += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '6' && val.charAt(3) == '8') {SENP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '6') {SEN += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '7' && val.charAt(3) == '8') {RSIP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '7') {RSI += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '8' && val.charAt(3) == '8') {APNP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '8') {APN += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '9' && val.charAt(3) == '8') {RNCP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '7' &&  val.charAt(1) == '5' &&  val.charAt(2) == '9') {RNC += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1' && val.charAt(3) == '8') {VNAP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1') {VNA += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '6' && val.charAt(3) == '8') {SAP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '6') {SA += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '8' && val.charAt(3) == '8') {ACNP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '8') {ACN += tmp.get(i).getSoldeBalance();}
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '9' && val.charAt(3) == '8') {DNCP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '5' &&  val.charAt(2) == '9') {DNC += tmp.get(i).getSoldeBalance();}
    		
    		if(val.charAt(0) == '6' &&  val.charAt(1) == '7' &&  val.charAt(2) == '0' && val.charAt(3) == '8') {ImpotP += tmp.get(i).getSoldeBalance();}
    		else if(val.charAt(0) == '6' &&  val.charAt(1) == '7' &&  val.charAt(2) == '0') {Impot += tmp.get(i).getSoldeBalance();}



        }
        
        TotalI =  VM + VB - VS + IP + SE + AP + RE ;
        TotalIP =  VMP + VBP - VSP + IPP + SEP + APP + REP ;
        Bilan CPE = new Bilan(" PRODUITS D'EXPLOITATION",TotalI,TotalIP,TotalI + TotalIP,true); cpc.add(CPE);
        Bilan CVM = new Bilan(" Ventes de marchandises (en l'état)",VM,VMP ,VM +VMP,false); cpc.add(CVM);
        Bilan CVB = new Bilan("  Ventes de biens et services produits chiffre d'affaires",VB,VBP,VB + VBP,false); cpc.add(CVB);
        Bilan CVS = new Bilan("  Variation des stocks de produits (1)",VS,VSP,VSP + VS,false); cpc.add(CVS);
        Bilan C = new Bilan("  Immobilisations produites par l'entreprise pour elle-même",IP,IPP,IPP+IP,false); cpc.add(C);
        Bilan CSE = new Bilan("  Subventions d'exploitation",SE,SEP,SEP+ SE,false); cpc.add(CSE);
        Bilan CAP = new Bilan(" Autres produits d'exploitation",AP,APP,APP + AP,false); cpc.add(CAP);
        Bilan CRE = new Bilan(" Reprises d'exploitation; Transsferts de charges",RE,REP,RE + REP,false); cpc.add(CRE);
        Bilan CTotalI = new Bilan("Total I  ",TotalI,TotalIP,TotalIP + TotalI,true); cpc.add(CTotalI);

        TotalII =  ARM + ACM + ACE + IT + CP + ACH + DE ;
        TotalIIP =  ARMP + ACMP + ACEP + ITP + CPP + ACHP + DEP ;
        Bilan CCE = new Bilan("  CHARGES D'EXPLOITATION",TotalII,TotalIIP,TotalIIP + TotalII,true); cpc.add(CCE);
        Bilan CARM = new Bilan("  Achats revendus(2) de marchandises",ARM,ARMP,ARMP+ARM,false); cpc.add(CARM);
        Bilan CACM = new Bilan("   Achats consommés(2) de matières et fournitures",ACM,ACMP ,ACMP+ACM,false); cpc.add(CACM);
        Bilan CACE = new Bilan("  Autres charges externes",ACE,ACEP,ACEP+ACE,false); cpc.add(CACE);
        Bilan CIT = new Bilan("   Impôts et taxes",IT,ITP,ITP+IT,false); cpc.add(CIT);
        Bilan CCP = new Bilan("  Charges de personnel",CP,CPP,CPP+CP,false); cpc.add(CCP);
        Bilan CACH = new Bilan("  Autres charges d'exploitation",ACH,ACHP,ACHP+ACH,false); cpc.add(CACH);
        Bilan CDE = new Bilan("  Dotations d'exploitation",DE,DEP,DEP+DE,false); cpc.add(CDE);
        Bilan CTotalII = new Bilan("Total II  ",TotalII,TotalIIP,TotalIIP+TotalII,true); cpc.add(CTotalII);

        RESULTATEXP = TotalI -TotalII ;
        RESULTATEXPP = TotalIP -TotalIIP ;
        Bilan RESULTATEX = new Bilan("  CHARGES D'EXPLOITATION",RESULTATEXP,RESULTATEXPP,RESULTATEXPP + RESULTATEXP,true); cpc.add(RESULTATEX);
        
        TotalIV =  PT + GC + IA + RF ;
        TotalIVP =  PTP + GCP + IAP + RFP ;
        Bilan CPF = new Bilan("  PRODUITS FINANCIERS",TotalIV,TotalIVP,TotalIVP + TotalIV,true); cpc.add(CPF);
        Bilan CPT = new Bilan("  Produits des titres de partic. et autres titres immobilisés",PT,PTP,PTP+ PT,false); cpc.add(CPT);
        Bilan CGC = new Bilan("  Gains de change",GC,GCP,GCP + GC,false); cpc.add(CGC);
        Bilan CIA = new Bilan(" Interêts et autres produits financiers",IA,IAP ,IAP + IA,false); cpc.add(CIA);
        Bilan CRF = new Bilan("  Reprises financier;  transferts de charges",RF,RFP,RFP + RF,false); cpc.add(CRF);
        Bilan CTotalIV = new Bilan("Total IV  ",TotalIV,TotalIVP,TotalIVP+TotalIV,true); cpc.add(CTotalIV);

        TotalV =  CFC + CFP + CFA + CFD ;
        TotalVP =  CFCP + CFPP + CFAP + CFDP ;
        Bilan CF = new Bilan("  CHARGES  FINANCIERS",TotalV,TotalVP,TotalVP + TotalV,true); cpc.add(CF);
        Bilan CCFC = new Bilan(" Charges d'interêts",CFC,CFCP, CFCP + CFC,false); cpc.add(CCFC);
        Bilan CCFP = new Bilan(" Pertes de change",CFP,CFPP,CFPP+CFP,false); cpc.add(CCFP);
        Bilan CCFA = new Bilan(" Autres charges financières",CFA,CFAP ,CFAP + CFA,false); cpc.add(CCFA);
        Bilan CCFD = new Bilan(" Dotations finacières",CFD,CFDP,CFDP + CFD,false); cpc.add(CCFD);
        Bilan CTotalV = new Bilan("Total V  ",TotalV,TotalVP,TotalVP+ TotalV,true); cpc.add(CTotalV);
        
        ResultatFin =  TotalIV - TotalV ;
        ResultatFinP =  TotalIVP - TotalVP ;
        Bilan ResultatF = new Bilan(" RESULTAT FINANCIER (IV-V)",ResultatFin,ResultatFinP,ResultatFinP + ResultatFin,true); cpc.add(ResultatF);
        
        ResultatCourant =  RESULTATEXP + ResultatFin ;
        ResultatCourantP =  RESULTATEXPP + ResultatFinP ;
        Bilan ResultatC = new Bilan(" RESULTAT COURANT (III+VI) ",ResultatCourant,ResultatCourantP,ResultatCourantP + ResultatCourant,true); cpc.add(ResultatC);

        TotalVIII =  PCI + SEN + RSI + APN + RNC ;
        TotalVIIIP =  PCIP + SENP + RSIP + APNP + RNCP ;
        Bilan PNC = new Bilan("  PRODUITS NON COURANTS",TotalVIII,TotalVIIIP, TotalVIIIP + TotalVIII,true); cpc.add(PNC);
        Bilan PPCI = new Bilan(" Produits des cessions d'immobilisations",PCI,PCIP, PCI + PCIP,false); cpc.add(PPCI);
        Bilan PSEN = new Bilan(" Subventions d'équilibre",SEN,SENP,SEN+SENP,false); cpc.add(PSEN);
        Bilan PRSI = new Bilan(" Reprises sur subventions d'investissement",RSI,RSIP ,RSI + RSIP ,false); cpc.add(PRSI);
        Bilan PAPN = new Bilan("  Autres produits non courants",APN,APNP,APN + APNP,false); cpc.add(PAPN);
        Bilan PRNC = new Bilan("  Autres produits non courants",RNC,RNCP,RNC + RNCP,false); cpc.add(PRNC);
        Bilan CTotalVIII = new Bilan("Total VIII  ",TotalVIII,TotalVIIIP,TotalVIII+ TotalVIIIP,true); cpc.add(CTotalVIII);
        
        TotalIX =  VNA + SA + ACN + DNC ;
        TotalIXP =  VNAP + SAP + ACNP + DNCP ;
        Bilan CNC = new Bilan("  CHARGES NON COURANTES",TotalIX,TotalIXP, TotalIX + TotalIXP,true); cpc.add(CNC);
        Bilan CVNA = new Bilan(" Valeurs nettes d'amortissements des immobilisations cédées",VNA,VNAP, VNA + VNAP,false); cpc.add(CVNA);
        Bilan CSA = new Bilan("  Subventions accordées",SA,SAP,SA+SAP,false); cpc.add(CSA);
        Bilan CACN = new Bilan(" Autres charges non courantes",ACN,ACNP ,ACN + ACNP ,false); cpc.add(CACN);
        Bilan CDNC = new Bilan("  Dotations non courantes aux amortissements et aux provisions",DNC,DNCP,DNC + DNCP,false); cpc.add(CDNC);
        Bilan CTotalIX = new Bilan("Total IX  ",TotalIX,TotalIXP,TotalIX+ TotalIXP,true); cpc.add(CTotalIX);
        
        ResultatNonCourant =  TotalVIII - TotalIX ;
        ResultatNonCourantP =  TotalVIIIP - TotalIXP ;
        Bilan ResultatNC = new Bilan("RESULTAT NON COURANT (VIII-IX)",ResultatNonCourant,ResultatNonCourantP,ResultatNonCourant + ResultatNonCourantP,true); cpc.add(ResultatNC);
        
        ResultatAvantImpot =  ResultatCourant + ResultatNonCourant  ;
        ResultatAvantImpotP =   ResultatCourantP + ResultatNonCourantP  ;
        Bilan ResultatAI = new Bilan("RESULTAT AVANT IMPÔTS (VII+X)",ResultatAvantImpot,ResultatAvantImpotP,ResultatAvantImpot + ResultatAvantImpotP,true); cpc.add(ResultatAI);
       
        Bilan Imp = new Bilan("IMPÔTS SUR LES BENEFICES",Impot,ImpotP,Impot + ImpotP,true); cpc.add(Imp);
        
        ResultatNet =  ResultatAvantImpot - Impot  ;
        ResultatNetP =  ResultatAvantImpotP - ImpotP  ;
        Bilan ResultatN = new Bilan("RESULTAT NET (XI-XII)",ResultatNet,ResultatNetP,ResultatNet + ResultatNetP,true); cpc.add(ResultatN);

        Bilan vide = new Bilan(); cpc.add(vide);
        
        TotalProd =  TotalI + TotalIV + TotalVIII  ;
        TotalProdP = TotalIP + TotalIVP + TotalVIIIP  ;
        Bilan Totalp = new Bilan("TOTAL DES PRODUITS (I+IV+VIII)",TotalProd,TotalProdP,TotalProd + TotalProdP,true); cpc.add(Totalp);
        
        TotalCharge=  TotalII + TotalV + TotalIX + ResultatNet ;
        TotalChargeP = TotalIIP + TotalVP + TotalIXP + ResultatNetP ;
        Bilan Totalc = new Bilan("TOTAL DES CHARGES (II+V+IX+XIII)",TotalCharge,TotalChargeP,TotalCharge + TotalChargeP,true); cpc.add(Totalc);
        
        RN =  TotalProd - TotalCharge  ;
        RNP = TotalProdP - TotalChargeP ;
        Bilan resultatnet = new Bilan("RESULTAT NET (total des produits-total des charges)",RN,RNP,RN + RNP,true); cpc.add(resultatnet);
        
       return cpc ;
	}

	@Override
	public List<Bilan> generateBilanPassif(List<Balance> tmp ) {

        List<Bilan> bilanPassif = new ArrayList<>();  
		 double ACS = 0 , AM  = 0 , ACA = 0 , AP  = 0 , AER = 0 , ARL = 0 , AAR = 0 , ARN = 0 , ARI = 0 , ARE = 0 , BS  = 0 ,
	        	   BP  = 0 , CEO = 0 , CAF = 0 , DPR = 0 , DPC = 0 , EAC = 0 , EDF = 0 , FFC = 0 , FCA = 0 , FPD = 0 , FOS = 0 ,
	        	   FE  = 0 , FC  = 0 , FAC = 0 , FCR = 0 , GAP = 0 , HEC = 0 , ICE = 0 , ICT = 0 , IB  = 0 ,
	        	   TotalCP = 0 , TotalCPA  = 0 , TotalDF  = 0 , TotalPDR = 0 , TotalEC = 0 , TotalDPC  = 0 , TotalTP = 0 ,
	        	   TotalI  = 0 , TotalII   = 0 , TotalIII = 0 , TotalGeneral = 0 ;	 
		 
		 for(int i = 0 ; i< tmp.size() ; i++) {
	        	String val =  tmp.get(i).getCompteBalance().toString();

				if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {ACS += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1'  && val.charAt(2) == '9' ) {AM += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '2') {AP += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '3') {AER += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4') {ARL += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '5') {AAR += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '6') {ARN += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '1' &&  val.charAt(2) == '8') {ARI += tmp.get(i).getSoldeBalance();}
		
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '3' &&  val.charAt(2) == '1') {BS += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '3' &&  val.charAt(2) == '5') {BP += tmp.get(i).getSoldeBalance();}
		 		 
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '4' &&  val.charAt(2) == '1') {CEO += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '4' &&  val.charAt(2) == '8') {CAF+= tmp.get(i).getSoldeBalance();}
		 		
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '5' &&  val.charAt(2) == '1') {DPR += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '5' &&  val.charAt(2) == '5') {DPC += tmp.get(i).getSoldeBalance();}
		 		
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '7' &&  val.charAt(2) == '1') {EAC += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '1' &&  val.charAt(1) == '7' &&  val.charAt(2) == '2') {EDF += tmp.get(i).getSoldeBalance();}
		 		
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '1') {FFC += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '2') {FCA += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '3') {FPD += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '4') {FOS += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '5') {FE += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '6') {FC += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '8') {FAC += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '4' &&  val.charAt(2) == '9') {FCR += tmp.get(i).getSoldeBalance();}
		 		
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '5') {GAP += tmp.get(i).getSoldeBalance();}
		
		 		if(val.charAt(0) == '4' &&  val.charAt(1) == '7') {HEC += tmp.get(i).getSoldeBalance();}
		 		
		 		if(val.charAt(0) == '5' &&  val.charAt(1) == '5' &&  val.charAt(2) == '2') {ICE += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '5' &&  val.charAt(1) == '5' &&  val.charAt(2) == '3') {ICT += tmp.get(i).getSoldeBalance();}
		 		if(val.charAt(0) == '5' &&  val.charAt(1) == '1' &&  val.charAt(2) == '4') {IB += tmp.get(i).getSoldeBalance();}
		 }
		  TotalCP = ACS + AM + ACA + AP + AER + ARL + AAR + ARN + ARI + ARE;
	        Bilan CP = new Bilan("CAPITAUX PROPRES (A)",TotalCP,true); bilanPassif.add(CP);
	        Bilan BACS = new Bilan("Capital social ou personnel (1)",ACS,false); bilanPassif.add(BACS);
	        Bilan BAM = new Bilan("Moins : actionnaires, capital souscrit non applé",AM,false); bilanPassif.add(BAM);
	        Bilan BACA = new Bilan("Capital appelé, dont versé",ACA,false); bilanPassif.add(BACA);
	        Bilan BAP = new Bilan("Primes d'émission, de fusion, d'apport",AP,false); bilanPassif.add(BAP);
	        Bilan BAER = new Bilan("Ecart de réévaluation",AER,false); bilanPassif.add(BAER);
	        Bilan BARL = new Bilan("Réserve légale",ARL,false); bilanPassif.add(BARL);
	        Bilan BAAR = new Bilan("Autres Réserves",AAR,false); bilanPassif.add(BAAR);
	        Bilan BARN = new Bilan("Report à nouveau (2)",ARN,false); bilanPassif.add(BARN);
	        Bilan BARI = new Bilan("Résultats nets en instance d'affectation (2)",ARI,false); bilanPassif.add(BARI);
	        Bilan BARE = new Bilan(" Résultat net de l'exercice (2)",ARE,false); bilanPassif.add(BARE);
	        
	        TotalCPA = BS + BP ;
	        Bilan CPA = new Bilan(" CAPITAUX PROPRES ASSIMILES",TotalCPA,true); bilanPassif.add(CPA);
	        Bilan BBS = new Bilan(" Subvensions d'investissement",BS,false); bilanPassif.add(BBS);
	        Bilan BBP = new Bilan(" Provisions réglementées",BP,false); bilanPassif.add(BBP);
	       
	        TotalDF = CEO +CAF ;
	        Bilan DF = new Bilan(" DETTES DE FINANCEMENT",TotalDF,true); bilanPassif.add(DF);
	        Bilan BCEO = new Bilan(" Emprunts obligataires",CEO,false); bilanPassif.add(BCEO);
	        Bilan BCAF = new Bilan(" Autres dettes de financement",CAF,false); bilanPassif.add(BCAF);
	        
	        TotalPDR = DPR + DPC ;
	        Bilan PDR = new Bilan(" PROVISIONS DURABLES POUR RISQUE ET CHARGES",TotalPDR,true); bilanPassif.add(PDR);
	        Bilan BDPR = new Bilan(" Provisions pour risques",DPR,false); bilanPassif.add(BDPR);
	        Bilan BDPC = new Bilan(" Provisions pour charges",DPC,false); bilanPassif.add(BDPC);
	        
	        TotalEC = EAC + EDF ;
	        Bilan ECP = new Bilan(" ECRATS DE CONVERSION - PASSIF",TotalEC,true); bilanPassif.add(ECP);
	        Bilan BEAC = new Bilan(" Augmentation des créances immobilisées",EAC,false); bilanPassif.add(BEAC);
	        Bilan BEDF = new Bilan(" Diminution des dettes de financement",EDF,false); bilanPassif.add(BEDF);
	       
	        TotalI = TotalCP + TotalCPA + TotalDF + TotalPDR + TotalEC;
	        Bilan BTotalI = new Bilan(" Total I ( A + B + C + D + E )",TotalI,true); bilanPassif.add(BTotalI);
	       
	        TotalDPC = FFC + FCA + FPD + FOS + FE + FC + FAC + FCR;
	        Bilan TDPC = new Bilan(" DETTES DU PASSIF CIRCULANT",TotalDPC,true); bilanPassif.add(TDPC);
	        Bilan BFFC = new Bilan(" Fournisseurs et comptes rattachés",FFC,false); bilanPassif.add(BFFC);
	        Bilan BFCA = new Bilan(" Clients créditeurs, avances et acomptes",FCA,false); bilanPassif.add(BFCA);
	        Bilan BFPD = new Bilan(" Personnel",FPD,false); bilanPassif.add(BFPD);
	        Bilan BFOS = new Bilan(" Organismes sociaux",FOS,false); bilanPassif.add(BFOS);
	        Bilan BFE = new Bilan(" Etat",FE,false); bilanPassif.add(BFE);
	        Bilan DFC = new Bilan(" Comptes d'associés",FC,false); bilanPassif.add(DFC);
	        Bilan BFAC = new Bilan(" Autres créanciers",FAC,false); bilanPassif.add(BFAC);
	        Bilan BFCR = new Bilan(" Comptes de régularisation passif",FCR,false); bilanPassif.add(BFCR);

	        Bilan TGAP = new Bilan(" AUTRES PROVISIONS POUR RISQUES ET CHARGES (G)",GAP,true); bilanPassif.add(TGAP);
	        
	        Bilan THEC = new Bilan(" ECARTS DE CONVERSION - PASSIF (Eléments circulants) (H)",HEC,true); bilanPassif.add(THEC);
	        
	        TotalII = TotalDPC + GAP + HEC ;
	        Bilan BTotalII = new Bilan(" Total II (F + G + H )",TotalII,true); bilanPassif.add(BTotalII);
	        
	        TotalTP = ICE + ICT + IB ;
	        Bilan TP = new Bilan(" TRESORERIE - PASSIF (I)",TotalTP,true); bilanPassif.add(TP);
	        Bilan BICE = new Bilan(" Crédit d'escompte",ICE,false); bilanPassif.add(BICE);
	        Bilan BICT = new Bilan(" Crédit de trésorerie",ICT,false); bilanPassif.add(BICT);
	        Bilan BIB = new Bilan(" Banques (soldes créditeurs)",IB,false); bilanPassif.add(BIB);

	        TotalIII = TotalTP ;
	        Bilan BTotalIII = new Bilan(" Total III",TotalIII,true); bilanPassif.add(BTotalIII);
	        
	        TotalGeneral = TotalI + TotalII + TotalIII ;
	        Bilan BTG = new Bilan(" TOTAL GENERAL I + II + III",TotalGeneral,true); bilanPassif.add(BTG);
	        
		return bilanPassif;
	}

	@Override
	public String generateXML( Long ref , String filename ){
		List<Balance> balanceList = this.getBalance( ref );
		DocCompany doc =  docCompanyRepository.findTopByIdDoc( ref );
		Company company = doc.getCompany();
		Liasse liasse = new Liasse( 3 ,
				company.getIfCompany(),
				doc.getYearDoc() + "-01-01",
				doc.getYearDoc() + "-31-12");
		liasse.setBilanActif( this.generateBilanActif( balanceList ) );
		liasse.setBilanPassif( this.generateBilanPassif( balanceList ) );
		liasse.setCpc( this.generateCPC( balanceList ));

		XMLMetierImpl xmlMetier = new XMLMetierImpl();
		xmlMetier.createLiasseXML( filename , liasse );

		return filename + ".xml";
	}
}
