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
@Named(value = "forgetPassword")
//@ManagedBean(name = "newUserProfile")  
@ViewScoped
public class ForgetPassword implements Serializable{

    UserProfile userProfile;
    
    String forgetPasswordValue;
    
    @Inject
    private AdminService adminService;
    
    @Inject
    private AdminController adminController;
    
    private static Logger logger = Logger.getLogger("com.kenexx");
    
    public static final String SALT = "kennex-salt";
    
    @PostConstruct
    public void init(){
        
    }   

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
        
    public String getForgetPasswordValue() {
        return forgetPasswordValue;
    }

    public void setForgetPasswordValue(String forgetPasswordValue) {
        this.forgetPasswordValue = forgetPasswordValue;
    }
        
    public String forgetPasswordCheckAndEmail() throws Exception {
        
        FacesContext context = FacesContext.getCurrentInstance();
        try {
          this.userProfile = adminService.findUserProfilebyEmail(forgetPasswordValue);
          if(this.userProfile==null){
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! this email doesn't exists", ""));
                   return "forgetPassword"; 
          } 
          String saltedForgetPasswordCode = SALT + this.forgetPasswordValue;
          String hashedForgetPasswordCode = ViewSupport.generateHash(saltedForgetPasswordCode);
          this.userProfile.setForgetPasswordCode(hashedForgetPasswordCode);
          this.userProfile.setForgetPasswordRequest("1");
          //System.out.println(this.userProfile.getEmail());
          return adminController.updateUserProfileForForgetPassword(this.userProfile);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error occured", e.getMessage()));
            return null;
        }
    }
    
    public String forgetpasswordCancel() throws Exception {
       this.setForgetPasswordValue(null);
        return "login";
    }
    
}
