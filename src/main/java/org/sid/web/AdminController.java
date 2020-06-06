package org.sid.web;

import org.sid.entities.Company;
import org.sid.entities.DocCompany;
import org.sid.entities.DocType;
import org.sid.entities.User;
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
    public ResponseEntity createT1(){
    	//Map<String,Object> data = null ;
        List<User> users = userMetier.getAllUsers() ;
    	 //Map<String , Object> data;
    	InputStreamResource ressource = userMetier.generateT1();
    	if(ressource != null ) {
    		return  ResponseEntity.ok().body(ressource);
    	}
    	else {
    		return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
    	}
    }
    @RequestMapping(value="createPdf", produces="application/pdf")
    public void createPdf() {
       userMetier.createpdf();      
       System.out.println("creation avec succes");
    }

    @RequestMapping("generatePdf")
    public void generatePdf(HttpServletRequest request , HttpServletResponse response) {
        List<User> users = userMetier.getAllUsers() ;

        boolean isFlag = userMetier.generatePdf(users , context , request , response);
        if(isFlag) {
    		String fullPath = request.getServletContext().getRealPath("/resources/tmp/"+"users"+".pdf");
    		filedownload(fullPath,response , "users.pdf");
    	}     
    }

    private void filedownload(String fullPath, HttpServletResponse response, String fileName) {
    	File file = new File(fullPath);
    	final int BUFFER_SIZE = 4096 ;
    	if(file.exists()) {
    		try{
    			FileInputStream inputStream = new FileInputStream (file);
    			String mimeType = context.getMimeType(fullPath);
    			response.setContentType(mimeType);
    			response.setHeader("content-disposition","attachement;filename="+fileName);
    			OutputStream outputStream  = response.getOutputStream();
    			byte[] buffer = new byte[BUFFER_SIZE] ;
    			int byteRead = -1 ;
    			while((byteRead = inputStream.read(buffer)) != -1 ){
    				outputStream.write(buffer, 0 , byteRead);
    			}
    			inputStream.close() ;
    			outputStream.close();
    			file.delete() ;
    		}catch(Exception e ) {
    			e.printStackTrace()	;
    		}
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
    	boolean isFlag = userMetier.saveDataFromUpload(user.getFile());
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
        model.addAttribute("taxes", taxMetier.getAllTaxes() );
        model.addAttribute("companies", companyMetier.getAllCompany() );
        return "/admin/taxesList";
    }

    @PostMapping("/tax/add")
    public String addTax(@Valid DocCompany tax , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/usersList";
        }

        Company tmpCompany = companyMetier.findCompanyById((Long) model.getAttribute( "idCompany" ));
        tax.setCompany( tmpCompany );
        this.taxMetier.createTax( tax );
        return "redirect:/admin/docs";
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
   
}
