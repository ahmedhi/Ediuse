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
        List<bilan> bilan = new ArrayList<>();   
        double Abrut = 0 , Aamort = 0 , Anet = 0 ,
        	   Bbrut = 0 , Bamort = 0 , Bnet = 0 ,
        	   Cbrut = 0 , Camort = 0 , Cnet = 0 ,
        	   Dbrut = 0 , Damort = 0 , Dnet = 0 ,
        	   Ebrut = 0 ,              Enet = 0 ,       
        	   Fbrut = 0 , Famort = 0 , Fnet = 0 ,
        	   Gbrut = 0 , Gamort = 0 , Gnet = 0 ,
        	   Hbrut = 0 , Hamort = 0 , Hnet = 0 ,
        	   Ibrut = 0 ,              Inet = 0 ,
        	   TAbrut= 0 , TAamort = 0, TAnet= 0 ,
        	   TotalIbrut       = 0 , TotalIamort      = 0 , TotalInet     = 0 ,
        	   TotalIIbrut      = 0 , TotalIIamort     = 0 , TotalIInet    = 0 ,
        	   TotalIIIbrut     = 0 , TotalIIIamort    = 0 , TotalIIInet   = 0 ,
        	   TotalGeneralbrut = 0 , TotalGeneralamort= 0 ,TotalGeneralnet= 0 ,
        	   AFPbrut = 0 , AFPamort = 0 , AFPnet = 0 ,
        	   ACbrut  = 0 , ACamort  = 0 , ACnet  = 0 ,
        	   APRbrut = 0 , APRamort = 0 , APRnet = 0 ,
        	   BIbrut  = 0 , BIamort  = 0 , BInet  = 0 ,
        	   BBbrut  = 0 , BBamort  = 0 , BBnet  = 0 ,
        	   BFCbrut = 0 , BFCamort = 0 , BFCnet = 0 ,
        	   BAbrut  = 0 , BAamort  = 0 , BAnet  = 0 ,
        	   CTbrut  = 0 , CTamort  = 0 , CTnet  = 0 , 
        	   CCbrut  = 0 , CCamort  = 0 , CCnet  = 0 ,
        	   CIbrut  = 0 , CIamort  = 0 , CInet  = 0 ,
        	   CMTbrut = 0 , CMTamort = 0 , CMTnet = 0 ,
        	   CMbrut  = 0 , CMamort  = 0 , CMnet  = 0 ,
        	   CAbrut  = 0 , CAamort  = 0 , CAnet  = 0 ,
        	   CICbrut = 0 , CICamort = 0 , CICnet = 0 ,
        	   DPbrut  = 0 , DPamort  = 0 , DPnet  = 0 ,
        	   DAbrut  = 0 , DAamort  = 0 , DAnet  = 0 ,
        	   DTbrut  = 0 , DTamort  = 0 , DTnet  = 0 ,
        	   DATbrut = 0 , DATamort = 0 , DATnet = 0 ,
        	   EDbrut  = 0 ,                EDnet  = 0 ,
        	   EAbrut  = 0 ,                EAnet  = 0 ,
        	   FMbrut  = 0 , FMamort  = 0 , FMnet  = 0 ,
        	   FMCbrut = 0 , FMCamort = 0 , FMCnet = 0 ,
        	   FPbrut  = 0 , FPamort  = 0 , FPnet  = 0 ,
        	   FPPbrut = 0 , FPPamort = 0 , FPPnet = 0 ,
        	   FPFbrut = 0 , FPFamort = 0 , FPFnet = 0 ,
        	   GFbrut  = 0 , GFamort  = 0 , GFnet  = 0 ,
        	   GCCbrut = 0 , GCCamort = 0 , GCCnet = 0 ,
        	   GPbrut  = 0 , GPamort  = 0 , GPnet  = 0 ,
        	   GEbrut  = 0 , GEamort  = 0 , GEnet  = 0 ,
        	   GCbrut  = 0 , GCamort  = 0 , GCnet  = 0 ,
        	   GAbrut  = 0 , GAamort  = 0 , GAnet  = 0 ,
        	   GCRbrut = 0 , GCRamort = 0 , GCRnet = 0 ,
        	   IEbrut  = 0 ,                IEnet  = 0 ,
        	   TCVbrut = 0 , TCVamort = 0 , TCVnet = 0 ,
        	   TBbrut  = 0 , TBamort  = 0 , TBnet  = 0 ,
        	   TCRbrut = 0 , TCRamort = 0 , TCRnet = 0 ;
        
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
        		
        }
        
        
        Anet = Abrut - Aamort ; bilan A = new bilan("IMMOBILISATIONS EN NON VALEURS  (A)",Abrut,Aamort,Anet,true);  bilan.add(A);
        AFPnet = AFPbrut - AFPamort ;  bilan AFP = new bilan("Frais préliminaires",AFPbrut,AFPamort,AFPnet,false);  bilan.add(AFP);
        ACnet = ACbrut - ACamort ;   bilan AC = new bilan("Charges à repartir sur plusieurs  exercices",ACbrut,ACamort,ACnet,false); bilan.add(AC);
        APRnet = APRbrut - APRamort ;  bilan APR = new bilan("Primes de remboursement des obligations",APRbrut,APRamort,APRnet,false);  bilan.add(APR);
        
        Bnet = Bbrut - Bamort ; bilan B = new bilan("IMMOBILISATIONS INCORPORELLES  (B)",Bbrut,Bamort,Bnet,true); bilan.add(B);
        BInet = BIbrut - BIamort ;  bilan BI = new bilan("Immobilisation en recherche et développement",BIbrut,BIamort,BInet,false); bilan.add(BI);
        BBnet = BBbrut - BBamort ;  bilan BB = new bilan("Brevets, marques, droits et valeurs similaires",BBbrut,BBamort,BBnet,false);  bilan.add(BB);
        BFCnet = BFCbrut - BFCamort ;  bilan BFC = new bilan("Fonds commercial",BFCbrut,BFCamort,BFCnet,false); bilan.add(BFC);
        BAnet = BAbrut - BAamort ;  bilan BA = new bilan("Autres immobilisations incorporelles",BAbrut,BAamort,BAnet,false); bilan.add(BA);

        Cnet = Cbrut - Camort ;  bilan C = new bilan("IMMOBILISATIONS CORPORELLES (C)",Cbrut,Camort,Cnet,true);  bilan.add(C);
        CTnet = CTbrut - CTamort ;  bilan CT = new bilan("Terrains",CTbrut,CTamort,CTnet,false);  bilan.add(CT);
        CCnet = CCbrut - CCamort ;  bilan CC = new bilan("Constructions",CCbrut,CCamort,CCnet,false);  bilan.add(CC);
        CInet = CIbrut - CIamort ;  bilan CI = new bilan("Installations techniques, matériel et outillage",CIbrut,CIamort,CInet,false);  bilan.add(CI);
        CMTnet = CMTbrut - CMTamort ;  bilan CMT = new bilan("Matériel transport",CMTbrut,CMTamort,CMTnet,false);  bilan.add(CMT);
        CMnet = CMbrut - CMamort ;  bilan CM = new bilan("Mobilier, matériel de bureau et aménagements divers",CMbrut,CMamort,CMnet,false);  bilan.add(CM);
        CAnet = CAbrut - CAamort ;  bilan CA = new bilan("Autres immobilisations corporelles",CAbrut,CAamort,CAnet,false);  bilan.add(CA);
        CICnet = CICbrut - CICamort ;  bilan CIC = new bilan("Immobilisations corporelles en cours",CICbrut,CICamort,CICnet,false);  bilan.add(CIC);

        Dnet = Dbrut - Damort ;  bilan D = new bilan("IMMOBILISATIONS FINANCIERES  (D)",Dbrut,Damort,Dnet,true); bilan.add(D);
        DPnet = DPbrut - DPamort ;  bilan DP = new bilan("Prêts immobilisés",DPbrut,DPamort,DPnet,false);  bilan.add(DP);
        DAnet = DAbrut - DAamort ;  bilan DA = new bilan("Autres créances financières",DAbrut,DAamort,DAnet,false);  bilan.add(DA);
        DTnet = DTbrut - DTamort ;  bilan DT = new bilan("Titres de participation",DTbrut,DTamort,DTnet,false);  bilan.add(DT);
        DATnet = DATbrut - DATamort ;  bilan DAT = new bilan("Autres titres immobilisés",DATbrut,DATamort,DATnet,false);  bilan.add(DAT);

        Enet = Ebrut ; bilan E = new bilan("ECARTS DE CONVERSION -ACTIF  (E)",Ebrut,-999,Enet,true); bilan.add(E);
        EDnet = EDbrut ;  bilan ED = new bilan("Diminution des créances immobilisées",EDbrut,-999,EDnet,false);  bilan.add(ED);
        EAnet = EAbrut ;  bilan EA = new bilan("Augmentation des dettes financières",EAbrut,-999,EAnet,false);  bilan.add(EA);

        TotalIbrut = Abrut +Bbrut+Cbrut+Dbrut+Ebrut ; TotalIamort = Aamort +Bamort +Camort +Damort; TotalInet = TotalIbrut - TotalIamort ;
        bilan totalI = new bilan("TOTAL I (A+B+C+D+E)",TotalIbrut,TotalIamort,TotalInet,true); bilan.add(totalI);
        
        Fnet = Fbrut - Famort ;  bilan F = new bilan("STOCKS (F)",Fbrut,Famort,Fnet,true); bilan.add(F);
        FMnet = FMbrut - FMamort ;  bilan FM = new bilan("Marchandises",FMbrut,FMamort,FMnet,false);  bilan.add(FM);
        FMCnet = FMCbrut - FMCamort ;  bilan FMC = new bilan("Matières et fournitures, consommables",FMCbrut,FMCamort,FMCnet,false);  bilan.add(FMC);
        FPnet = FPbrut - FPamort ;  bilan FP = new bilan("Produits en cours",FPbrut,FPamort,FPnet,false);  bilan.add(FP);
        FPPnet = FPPbrut - FPPamort ;  bilan FPP = new bilan("produits intermédiaires et produits résiduels",FPPbrut,FPPamort,FPPnet,false);  bilan.add(FPP);
        FPFnet = FPFbrut - FPFamort ;  bilan FPF = new bilan("Produits finis",FPFbrut,FPFamort,FPFnet,false);  bilan.add(FPF);
 
        Gnet = Gbrut - Gamort ; bilan G = new bilan("CREANCES DE L'ACTIF CIRCULANT  (G)",Gbrut,Gamort,Gnet,true); bilan.add(G);
        GFnet = GFbrut - GFamort ;  bilan GF = new bilan("Fournis. débiteurs, avances et acomptes",GFbrut,GFamort,GFnet,false);  bilan.add(GF);
        GCCnet = GCCbrut - GCCamort ;  bilan GCC = new bilan("Clients et comptes rattachés",GCCbrut,GCCamort,GCCnet,false);  bilan.add(GCC);
        GPnet = GPbrut - GPamort ;  bilan GP = new bilan("Personnel",GPbrut,GPamort,GPnet,false);  bilan.add(GP);
        GEnet = GEbrut - GEamort ;  bilan GE = new bilan("Etat",GEbrut,GEamort,GEnet,false);  bilan.add(GE);
        GCnet = GCbrut - GCamort ;  bilan GC = new bilan("Comptes d'associés",GCbrut,GCamort,GCnet,false);  bilan.add(GC);
        GAnet = GAbrut - GAamort ;  bilan GA = new bilan("Autres débiteurs",GAbrut,GAamort,GAnet,false);  bilan.add(GA);
        GCRnet = GCRbrut - GCRamort ;  bilan GCR = new bilan("Comptes de régularisation-Actif",GCRbrut,GCRamort,GCRnet,false);  bilan.add(GCR);

        Hnet = Hbrut - Hamort ;  bilan H = new bilan("TITRES VALEURS DE PLACEMENT  (H)",Hbrut,Hamort,Hnet,true); bilan.add(H);
       
        Inet = Ibrut  ; bilan I = new bilan("ECARTS DE CONVERSION-ACTIF  (I)",Ibrut,-999,Inet,true); bilan.add(I);
        IEnet = IEbrut;  bilan IE = new bilan("Comptes de régularisation-Actif",IEbrut,-999,IEnet,false);  bilan.add(IE);

        TotalIIbrut = Fbrut+Gbrut+Hbrut+Ibrut ; TotalIIamort = Famort +Gamort +Hamort ; TotalIInet =  TotalIIbrut -  TotalIIamort ;
        bilan totalII = new bilan("TOTAL II  ( F+G+H+I )",TotalIIbrut,TotalIIamort,TotalIInet,true); bilan.add(totalII);
       
        TAnet = TAbrut - TAamort ; bilan TA = new bilan("TRESORERIE-ACTIF",TAbrut,TAamort,TAnet,true); bilan.add(TA);
        TCVnet = TCVbrut - TCVamort ;  bilan TCV = new bilan("Chéques et valeurs à encaisser",TCVbrut,TCVamort,TCVnet,false);  bilan.add(TCV);
        TBnet = TBbrut - TBamort ;  bilan TB = new bilan(" Banques, TG et CCP",TBbrut,TBamort,TBnet,false);  bilan.add(TB);
        TCRnet = TCRbrut - TCRamort ;  bilan TCR = new bilan(" Caisse, Régie d'avances et accréditifs",TCRbrut,TCRamort,TCRnet,false);  bilan.add(TCR);

        TotalIIIbrut = TAbrut ; TotalIIIamort = TAamort ; TotalIIInet = TotalIIIbrut - TotalIIIamort ;
        bilan totalIII = new bilan("TOTAL III",TotalIIIbrut,TotalIIIamort,TotalIIInet,true);  bilan.add(totalIII);
        
        TotalGeneralbrut = TotalIbrut + TotalIIbrut + TotalIIIbrut ; TotalGeneralamort = TotalIamort + TotalIIamort + TotalIIIamort ;
        TotalGeneralnet = TotalInet + TotalIInet + TotalIIInet;
        bilan TG = new bilan("TOTAL GENERAL I+II+III",TotalGeneralbrut,TotalGeneralamort,TotalGeneralnet,true);  bilan.add(TG);
        
        model.addAttribute("bilan", bilan );

        return "/documents/bilanActif";
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
