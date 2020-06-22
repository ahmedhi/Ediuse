package org.sid.web;

import org.sid.entities.*;
import org.sid.metier.CompanyMetierImpl;
import org.sid.metier.TaxMetierImpl;
import org.sid.metier.DocTypeMetierImpl;
import org.sid.metier.PartSocialMetierImpl;
import org.sid.metier.UserMetierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired private PartSocialMetierImpl partMetier;
    @Autowired private ServletContext context ;

    public double capital ;
    public int idCompany ; 
    public int countAssociete ;
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
    
    @RequestMapping("profilCompany/{id}")
    public String profilCompany(Model model , @PathVariable("id") int id ) {
    	List<Double> total = new ArrayList() ;
    	List<Integer>count = new ArrayList();
    	double lastEx = 0 , ExActuel = 0 , Souscrit = 0 , Appele = 0 , libere = 0 ;
    	idCompany = id ; 
        List<PartSocial> Allparts = partMetier.getAllParts();
        List<PartSocial> parts = new ArrayList();

        for(int i = 0 ; i< Allparts.size(); i++) {
        	if(Allparts.get(i).getIdCompany() == idCompany)
        		parts.add(Allparts.get(i));
        }

        for(int i = 0 ; i < parts.size(); i++) {
        	lastEx += parts.get(i).getExercicePrec();
        	ExActuel += parts.get(i).getExerciceActuel();
        	Souscrit += parts.get(i).getMontantCapitalSouscrit();
        	Appele += parts.get(i).getMontantCapitalAppele();
        	libere += parts.get(i).getMontantCapitalLibere();
        }
        countAssociete = parts.size();
        total.add(lastEx );  total.add(ExActuel); total.add(Souscrit);
        total.add(Appele);  total.add(libere); total.add(capital) ;
        count.add(countAssociete);
        count.add(idCompany);
        model.addAttribute("parts",parts );
        model.addAttribute("total",total);
        model.addAttribute("count",count);
        model.addAttribute("companies", companyMetier.findCompanyById(id));
    	 return "/admin/profilCompany";
    }
   /* @RequestMapping("partSocial")
    public String partSocial( Model model ) {
    	List<Double> total = new ArrayList() ;
    	double lastEx = 0 , ExActuel = 0 , Souscrit = 0 , Appele = 0 , libere = 0 ;
        List<PartSocial> parts = partMetier.getAllParts();
        for(int i = 0 ; i < parts.size(); i++) {
        	lastEx += parts.get(i).getExercicePrec();
        	ExActuel += parts.get(i).getExerciceActuel();
        	Souscrit += parts.get(i).getMontantCapitalSouscrit();
        	Appele += parts.get(i).getMontantCapitalAppele();
        	libere += parts.get(i).getMontantCapitalLibere();
        }
        total.add(lastEx );  total.add(ExActuel); total.add(Souscrit);
        total.add(Appele);  total.add(libere); total.add(capital) ;
        model.addAttribute("parts",parts );
        model.addAttribute("total",total);
        return "/documents/partSocial";
    }
   */
    @PostMapping("/partSocial/add")
    public String addPart(@Valid PartSocial part , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "redirect:/admin/profilCompany/"+idCompany;
        }
        this.partMetier.addCapitalSocial(part);
        return "redirect:/admin/profilCompany/"+idCompany; 
    }
   /* @PostMapping("/partSocial/add")
    public String addPart(@ModelAttribute("PartCapitalSocial") PartCapitalSocial part){
        this.partMetier.addCapitalSocial(part);
        return "redirect:/admin/partSocial";
    }
    */
    @PostMapping("/partSocial/update")
    public String updatePart(@ModelAttribute("PartCapitalSocial") PartSocial part ){
        this.partMetier.updateCapitalSocial(part);
        return "redirect:/admin/profilCompany/"+idCompany; 
    }


    @PostMapping("/partSocial/addCapital")
    public String addCapital(@Valid PartSocial part , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "redirect:/admin/profilCompany/"+idCompany;
        }
        capital = part.getPartSocial();
        return "redirect:/admin/profilCompany/"+idCompany;
    }
    
    @PostMapping("/partSocial/delete")
    public String deletePart(@ModelAttribute("PartCapitalSocial") PartSocial part){
        this.partMetier.deletePart(part);
        return "redirect:/admin/profilCompany/"+idCompany;
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
       
        /*Company tmpCompany = companyMetier.findCompanyById((Long) model.getAttribute( "idCompany" ));
        tax.setCompany( tmpCompany );*/
        List<Bilan> bilanActif = taxMetier.generateBilanActif(tmp);
        List<Bilan> bilanPassif = taxMetier.generateBilanActif(tmp);
        //List<Bilan> cpc = taxMetier.generateBilanActif(tmp);

        model.addAttribute("bilanActif", bilanActif );
        model.addAttribute("bilanPassif", bilanPassif );
        //model.addAttribute("cpc", cpc );
        // return "/documents/cpc";

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
