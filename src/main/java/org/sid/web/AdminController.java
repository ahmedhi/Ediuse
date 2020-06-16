package org.sid.web;

import org.sid.entities.*;
import org.sid.metier.CompanyMetierImpl;
import org.sid.metier.TaxMetierImpl;
import org.sid.metier.DocTypeMetierImpl;
import org.sid.metier.UserMetierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminController {

    @Autowired private UserMetierImpl userMetier;
    @Autowired private CompanyMetierImpl companyMetier;
    @Autowired private DocTypeMetierImpl typeDocMetier;
    @Autowired private TaxMetierImpl taxMetier;
    @Autowired private ServletContext context ;

    
    @RequestMapping("users")
    public String users( Model model) {
       model.addAttribute("users", userMetier.getAllUsers() );
        return "/admin/usersList";
    }

    @RequestMapping(value="generateModeleT1", produces="application/pdf")
      public ResponseEntity createT1(Model model){
    	//Map<String,Object> data = null ;
        List<User> users = userMetier.getAllUsers() ;
        model.addAttribute("users", userMetier.getAllUsers() );
        //return "modeleT1";
        //Map<String , Object> data;
    	InputStreamResource ressource = userMetier.generateT1(users);
    	if(ressource != null ) {
    		return  ResponseEntity.ok().body(ressource);
    	}
    	else {
    		return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
    	}
    }

	@RequestMapping("docs")
    public String docs( Model model) {
		model.addAttribute("docs", typeDocMetier.getAllDocs() );
        return "/admin/docsList";
    }

    @PostMapping("/user/add")
    public String addUser(@Valid User user , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/usersList";
        }
        this.userMetier.createUser( user );
        return "redirect:/admin/users";
    }
    @PostMapping("/upload")
    public String upload(@ModelAttribute User user , RedirectAttributes redirectAttributes ) {
    	boolean isFlag = taxMetier.addChartOfAccounts(user.getFile());
    	if(isFlag) {
    		redirectAttributes.addAttribute("succesmessage","File upload Successfully");
    	}else {
    		redirectAttributes.addAttribute("errormessage","File upload not done,  Please try again again !");
    	}
		return "redirect:/admin/tax";	
    }
    
    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("Utilisateur") User user){
        this.userMetier.updateUser( user);
        return "redirect:/admin/users";
    }
    
    @PostMapping("/user/delete")
    public String deleteUser(@ModelAttribute("Utilisateur") User user){
        this.userMetier.deleteUser(user);
        return "redirect:/admin/users";
    }
    
    @PostMapping("/doc/add")
    public String addUser(@Valid DocType doc , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/usersList";
        }
        this.typeDocMetier.createDoc( doc );
        return "redirect:/admin/docs";
    }
    
    @PostMapping("/doc/update")
    public String updateDoc(@ModelAttribute("Type_Doc") DocType doc){
        this.typeDocMetier.updateDoc( doc );
        return "redirect:/admin/docs";
    }

    @PostMapping("/doc/delete")
    public String deleteDoc(@ModelAttribute("Type_Doc") DocType doc){
        this.typeDocMetier.deleteDoc(doc);
        return "redirect:/admin/docs";
    }
    
    @RequestMapping("companies")
    public String companies( Model model ) {
        model.addAttribute("companies", companyMetier.getAllCompany() );
        return "/admin/companiesList";
    }

    @PostMapping("/company/add")
    public String addCompany(@Valid Company cmp , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/companies";
        }
        this.companyMetier.createCompany( cmp );
        return "redirect:/admin/companies";
    }

    @PostMapping("/company/update")
    public String updateCompany(@ModelAttribute("Company") Company cmp){
        this.companyMetier.updateCompany( cmp );
        return "redirect:/admin/companies";
    }

    @RequestMapping("/company/delete")
    public String deleteProduct(@ModelAttribute("Company") Company cmp) {
        companyMetier.deleteCompany(cmp);
        return "redirect:/admin/companies";
    }

    @RequestMapping("/tax")
    public String tax( Model model ) {
       /* model.addAttribute("taxes", taxMetier.getAllTaxes() );
        model.addAttribute("companies", companyMetier.getAllCompany() );*/
        return "/admin/taxesList";
    }

    @PostMapping("/tax/add")
    public String addTax(@Valid DocCompany tax , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/usersList";
        }
        List<Balance> tmp = taxMetier.addBalance( tax.getFile() );
        List<Bilan> bilanActif = new ArrayList<>();   
        List<Bilan> bilanPassif = new ArrayList<>();  
        double Abrut = 0 , Aamort = 0 , Anet = 0 , Bbrut = 0 , Bamort  = 0 , Bnet = 0 ,
        	   Cbrut = 0 , Camort = 0 , Cnet = 0 , Dbrut = 0 , Damort  = 0 , Dnet = 0 ,
        	   Ebrut = 0 ,              Enet = 0 , Fbrut = 0 , Famort  = 0 , Fnet = 0 ,      
        	   Gbrut = 0 , Gamort = 0 , Gnet = 0 , Hbrut = 0 , Hamort  = 0 , Hnet = 0 ,
        	   Ibrut = 0 ,              Inet = 0 , TAbrut= 0 , TAamort = 0 , TAnet= 0 ,
        	   TotalIbrut       = 0 , TotalIamort      = 0 , TotalInet     = 0 ,
        	   TotalIIbrut      = 0 , TotalIIamort     = 0 , TotalIInet    = 0 ,
        	   TotalIIIbrut     = 0 , TotalIIIamort    = 0 , TotalIIInet   = 0 ,
        	   TotalGeneralbrut = 0 , TotalGeneralamort= 0 ,TotalGeneralnet= 0 ,
        	   AFPbrut = 0 , AFPamort = 0 , AFPnet = 0 , ACbrut  = 0 , ACamort  = 0 , ACnet  = 0 ,
        	   APRbrut = 0 , APRamort = 0 , APRnet = 0 , BIbrut  = 0 , BIamort  = 0 , BInet  = 0 ,
        	   BBbrut  = 0 , BBamort  = 0 , BBnet  = 0 , BFCbrut = 0 , BFCamort = 0 , BFCnet = 0 ,
        	   BAbrut  = 0 , BAamort  = 0 , BAnet  = 0 , CTbrut  = 0 , CTamort  = 0 , CTnet  = 0 , 
        	   CCbrut  = 0 , CCamort  = 0 , CCnet  = 0 , CIbrut  = 0 , CIamort  = 0 , CInet  = 0 ,
        	   CMTbrut = 0 , CMTamort = 0 , CMTnet = 0 , CMbrut  = 0 , CMamort  = 0 , CMnet  = 0 ,
        	   CAbrut  = 0 , CAamort  = 0 , CAnet  = 0 , CICbrut = 0 , CICamort = 0 , CICnet = 0 ,
        	   DPbrut  = 0 , DPamort  = 0 , DPnet  = 0 , DAbrut  = 0 , DAamort  = 0 , DAnet  = 0 ,
        	   DTbrut  = 0 , DTamort  = 0 , DTnet  = 0 , DATbrut = 0 , DATamort = 0 , DATnet = 0 ,
        	   EDbrut  = 0 ,                EDnet  = 0 , EAbrut  = 0 ,                EAnet  = 0 ,
        	   FMbrut  = 0 , FMamort  = 0 , FMnet  = 0 , FMCbrut = 0 , FMCamort = 0 , FMCnet = 0 ,
        	   FPbrut  = 0 , FPamort  = 0 , FPnet  = 0 , FPPbrut = 0 , FPPamort = 0 , FPPnet = 0 ,
        	   FPFbrut = 0 , FPFamort = 0 , FPFnet = 0 , GFbrut  = 0 , GFamort  = 0 , GFnet  = 0 ,
        	   GCCbrut = 0 , GCCamort = 0 , GCCnet = 0 , GPbrut  = 0 , GPamort  = 0 , GPnet  = 0 ,
        	   GEbrut  = 0 , GEamort  = 0 , GEnet  = 0 , GCbrut  = 0 , GCamort  = 0 , GCnet  = 0 ,
        	   GAbrut  = 0 , GAamort  = 0 , GAnet  = 0 , GCRbrut = 0 , GCRamort = 0 , GCRnet = 0 ,
        	   IEbrut  = 0 ,                IEnet  = 0 , TCVbrut = 0 , TCVamort = 0 , TCVnet = 0 ,
        	   TBbrut  = 0 , TBamort  = 0 , TBnet  = 0 , TCRbrut = 0 , TCRamort = 0 , TCRnet = 0 ;
        double ACS = 0 , AM  = 0 , ACA = 0 , AP  = 0 , AER = 0 , ARL = 0 , AAR = 0 , ARN = 0 , ARI = 0 , ARE = 0 , BS  = 0 ,
        	   BP  = 0 , CEO = 0 , CAF = 0 , DPR = 0 , DPC = 0 , EAC = 0 , EDF = 0 , FFC = 0 , FCA = 0 , FPD = 0 , FOS = 0 ,
        	   FE  = 0 , FC  = 0 , FAC = 0 , FCR = 0 , GAP = 0 , HEC = 0 , ICE = 0 , ICT = 0 , IB  = 0 ,
        	   TotalCP = 0 , TotalCPA  = 0 , TotalDF  = 0 , TotalPDR = 0 , TotalEC = 0 , TotalDPC  = 0 , TotalTP = 0 ,
        	   TotalI  = 0 , TotalII   = 0 , TotalIII = 0 , TotalGeneral = 0 ;	   
        
        //Company tmpCompany = companyMetier.findCompanyById((Long) model.getAttribute( "idCompany" ));
        //tax.setCompany( tmpCompany );
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
        		if(val.charAt(0) == '5' &&  val.charAt(1) == '1' &&  val.charAt(2) == '1') {IB += tmp.get(i).getSoldeBalance();}


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

        model.addAttribute("bilanActif", bilanActif );
        model.addAttribute("bilanPassif", bilanPassif );

        return "/documents/bilan";
    }


    
    @PostMapping("/tax/update")
    public String updateTax(@ModelAttribute("Tax") DocCompany tax){
        this.taxMetier.updateTax( tax );
        return "redirect:/admin/tax";
    }

    @PostMapping("/tax/delete")
    public String deleteTax(@ModelAttribute("Tax") DocCompany tax){
        this.taxMetier.deleteTax( tax );
        return "redirect:/admin/tax";
    }

    @RequestMapping("/chartOfAccounts")
    public String chartOfAccounts( Model model ) {
        model.addAttribute("chartOfAccounts", taxMetier.getAllChartOfAccounts() );
        return "/admin/chartOfAccountsList";
    }

    @PostMapping("/chartOfAccounts/add")
    public String addchartOfAccounts(@ModelAttribute("Tax") ChartOfAccounts chartOfAccounts , RedirectAttributes redirectAttributes ) {
        boolean isFlag = taxMetier.addChartOfAccounts(chartOfAccounts.getFile());
        if(isFlag) {
            redirectAttributes.addAttribute("succesmessage","File upload Successfully");
        }else {
            redirectAttributes.addAttribute("errormessage","File upload not done,  Please try again again !");
        }
        return "redirect:/admin/tax";
    }

    @PostMapping("/chartOfAccounts/update")
    public String updatechartOfAccounts(@ModelAttribute("Tax") ChartOfAccounts tax){
        //this.taxMetier.updateTax( tax );
        return "redirect:/admin/tax";
    }

    @PostMapping("/chartOfAccounts/delete")
    public String deletechartOfAccounts(@ModelAttribute("Tax") ChartOfAccounts tax){
        //this.taxMetier.deleteTax( tax );
        return "redirect:/admin/tax";
    }
   
}
