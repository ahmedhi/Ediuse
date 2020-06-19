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
import org.springframework.web.bind.annotation.*;
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
            return "admin/tax";
        }
        List<Balance> tmp = taxMetier.addBalance( tax.getFile() );
        long id = tax.getCompany().getIdCompany();
        Company tmpCompany = companyMetier.findCompanyById( id );
        List<Balance> tmp = taxMetier.addBalance( tax.getFile() , tmpCompany , tax.getYearDoc());
        List<Bilan> bilanActif = taxMetier.generateBilanActif(tmp);
        List<Bilan> bilanPassif = taxMetier.generateBilanActif(tmp);
        model.addAttribute("bilanActif", bilanActif );
        model.addAttribute("bilanPassif", bilanPassif );
        //From origin/Features/Task2 return "/documents/bilan";
        return "redirect:/admin/tax";
    }

    @GetMapping("/tax/details/{id}")
    public String detailsTax( @PathVariable long id , Model model){
        model.addAttribute("Tax", taxMetier.getById(id) );
        model.addAttribute("balanceArray", taxMetier.getBalance(id));
        return "admin/taxesDetails";
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
