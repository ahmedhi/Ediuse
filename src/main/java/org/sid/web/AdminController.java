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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @RequestMapping(value="generateBilan", produces="application/pdf")
    public ResponseEntity generateBilan(){
        InputStreamResource ressource = userMetier.generateBilan();
        if(ressource != null ) {
            return  ResponseEntity.ok().body(ressource);
        }
        else {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @RequestMapping(value="generateCpc", produces="application/pdf")
    public ResponseEntity generateCpc(){
        InputStreamResource ressource = userMetier.generateCpc();
        if(ressource != null ) {
            return  ResponseEntity.ok().body(ressource);
        }
        else {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @RequestMapping(value="generateEtatRepCap", produces="application/pdf")
    public ResponseEntity generateEtatRepCap(){
        InputStreamResource ressource = userMetier.generateEtatRepCapital();
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

        for (PartSocial allpart : Allparts) {
            if (allpart.getCompany().getIdCompany() == idCompany)
                parts.add(allpart);
        }

        for (PartSocial part : parts) {
            lastEx += part.getExercicePrec();
            ExActuel += part.getExerciceActuel();
            Souscrit += part.getMontantCapitalSouscrit();
            Appele += part.getMontantCapitalAppele();
            libere += part.getMontantCapitalLibere();
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

    @PostMapping("/partSocial/add")
    public String addPart(@Valid PartSocial part , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "redirect:/admin/profilCompany/" + idCompany;
        }
        this.partMetier.addCapitalSocial(part);
        return "redirect:/admin/profilCompany/"+idCompany; 
    }

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
        model.addAttribute("taxes", taxMetier.getAllTaxes() );
        model.addAttribute("companies", companyMetier.getAllCompany() );
        return "/admin/taxesList";
    }
    

    @PostMapping("/tax/add")
    public String addTax(@Valid DocCompany tax , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/tax";
        }
        long id = tax.getCompany().getIdCompany();
        Company tmpCompany = companyMetier.findCompanyById( id );
        taxMetier.addBalance( tax.getFile() , tmpCompany , tax.getYearDoc());

        return "redirect:/admin/tax";
    }

    @GetMapping("/tax/details/{id}")
    public String detailsTax( @PathVariable long id , Model model){
        model.addAttribute("Tax", taxMetier.getById(id) );
        model.addAttribute("balanceArray", taxMetier.getBalance(id));
        return "admin/taxesDetails";
    }

    @RequestMapping("/tax/details/xml/{id}")
    public void downloadPDFResource( HttpServletRequest request, HttpServletResponse response, @PathVariable long id )
    {
        String fileName = taxMetier.generateXML( id , "testxml");
        Path file = Paths.get("tmp/", fileName);
        if (Files.exists(file))
        {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping("tax/bilan/{id}")
    public String bilanTax( @PathVariable long id , Model model){
        List<Balance> tmp = taxMetier.getBalance( id );

        List<Bilan> bilanActif = taxMetier.generateBilanActif(tmp);
        List<Bilan> bilanPassif = taxMetier.generateBilanActif(tmp);

        model.addAttribute("bilanActif", bilanActif );
        model.addAttribute("bilanPassif", bilanPassif );
        return "/documents/bilan";
    }

    @GetMapping("tax/cpc/{id}")
    public String cpcTax( @PathVariable long id , Model model ){
        List<Balance> tmp = taxMetier.getBalance( id );

        model.addAttribute( "cpc" , taxMetier.generateCPC( tmp ));
        return "/documents/cpc";
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
