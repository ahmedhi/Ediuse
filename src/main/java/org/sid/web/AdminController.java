package org.sid.web;

import org.sid.dao.EntrepriseRepository;
import org.sid.dao.TypeDocRepository;
import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Entreprise;
import org.sid.entities.Utilisateur;
import org.sid.metier.CompanyMetierImpl;
import org.sid.metier.UtilisateurMetierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired private UtilisateurMetierImpl utilisateurMetier;
    @Autowired private CompanyMetierImpl companyMetier;

    @RequestMapping("users")
    public String users( Model model) {
        model.addAttribute("users", utilisateurMetier.getAllUsers() );
        return "/admin/usersList";
    }

    @RequestMapping("docs")
    public String docs() {
        return "/admin/docsList";
    }

    @PostMapping("/user/add")
    public String addUser(@Valid Utilisateur user , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/usersList";
        }
        this.utilisateurMetier.createUser( user );
        return "redirect:/admin/users";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("Utilisateur") Utilisateur user){
        this.utilisateurMetier.updateUser( user );

        return "redirect:/admin/users";
    }

    @RequestMapping("companies")
    public String companies( Model model ) {
        model.addAttribute("companies", companyMetier.getAllCompany() );
        return "/admin/companiesList";
    }

    @PostMapping("/company/add")
    public String addCompany(@Valid Entreprise cmp , BindingResult result , Model model ){
        if( result.hasErrors() ){
            return "admin/companies";
        }
        this.companyMetier.createCompany( cmp );
        return "redirect:/admin/companies";
    }

    @PostMapping("/company/update")
    public String updateCompany(@ModelAttribute("Company") Entreprise cmp){
        this.companyMetier.updateCompany( cmp );

        return "redirect:/admin/companies";
    }

    @RequestMapping("/company/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        ///TODO : Fix delete function in CompanyMetierImpl
        companyMetier.deleteCompany(id);
        return "redirect:/";
    }
}
