/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.kenexx.model.UserProfile;
import com.kenexx.model.Plans;
import com.kenexx.service.AdminService;
import com.kenexx.controller.AdminController;
import com.kenexx.view.ViewSupport;
import com.kenexx.web.WebSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


/**
 *
 * @author user
 */
@Named(value = "newUserProfile")
//@ManagedBean(name = "newUserProfile")  
@ViewScoped
public class NewUserProfile implements Serializable{

    private List<Plans> plansName = new ArrayList();
    
    UserProfile userProfile;
    
    String confirmPassword;
    
    @Inject
    private AdminService adminService;
    
    @Inject
    private WebSession webSession;
    
    @Inject
    private AdminController adminController;
    
    public static final String SALT = "kennex-salt";
    
    private static Logger logger = Logger.getLogger("com.kenexx");
    
    @PostConstruct
    public void init(){
        
        try {
          //logger.info("Hiiii");
          plansName = adminService.getAllActivePlans();
          userProfile = webSession.getUserprofile();
          //logger.info(userProfile.getPlanId().toString());
        }
        catch (Exception ex) {
            Logger.getLogger(NewUserProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Plans> getPlansName() {
        return plansName;
    }

    public void setPlansName(List<Plans> plansName) {
        this.plansName = plansName;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
        
    public String newUserSave() throws Exception {
        
        FacesContext context = FacesContext.getCurrentInstance();
        try {
          if(!this.confirmPassword.equals(this.userProfile.getPassword())){
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! confirm p         assword should be equal to password", ""));
                   return "signUp"; 
          }
          //webSession.setUserprofile_1(this.userProfile);
        //logger.info(this.userProfile.getPlans().getId().toString());
        //userProfile.setPlans(userProfile.getPlans().getId());
          String saltedPassword = SALT + this.userProfile.getPassword();
          String hashedPassword = ViewSupport.generateHash(saltedPassword);
          this.userProfile.setPassword(hashedPassword);
          return adminController.saveNewUser(this.userProfile);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error saving", e.getMessage()));
            return null;
        }
    }
    
    public String newUserCancel() throws Exception {
        this.setUserProfile(null);
        return "login";
    }
    
}
