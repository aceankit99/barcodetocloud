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
import static com.kenexx.view.NewUserProfile.SALT;
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
@Named(value = "resetPassword")
//@ManagedBean(name = "newUserProfile")  
@ViewScoped
public class ResetPassword implements Serializable{

    UserProfile userProfile;
    
    String confirmPassword;;
    
    @Inject
    private AdminService adminService;
    
    @Inject
    private AdminController adminController;
    
    @Inject
    private WebSession webSession;
    
    private static Logger logger = Logger.getLogger("com.kenexx");
    
    public static final String SALT = "kennex-salt";
    
    @PostConstruct
    public void init(){
       userProfile = webSession.getUserprofile();
       //logger.info(userProfile.getEmail());
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
        
    public String resetPasswordCheck() throws Exception {
        
        
        FacesContext context = FacesContext.getCurrentInstance();
        try {
          if(!this.confirmPassword.equals(this.userProfile.getPassword())){
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! confirm password should be equal to password", ""));
                    
          }
          //webSession.setUserprofile_1(this.userProfile);
        //logger.info(this.userProfile.getPlans().getId().toString());
        //userProfile.setPlans(userProfile.getPlans().getId());
          String saltedPassword = SALT + this.userProfile.getPassword();
          String hashedPassword = ViewSupport.generateHash(saltedPassword);
          this.userProfile.setPassword(hashedPassword);
          this.userProfile.setForgetPasswordCode("");
          this.userProfile.setForgetPasswordRequest("0");
          return adminController.updateUserProfileForResetPassword(this.userProfile);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error saving", e.getMessage()));
            return null;
        }
    }
    
    public boolean isCodeCorrect() {
        if(this.userProfile!=null){
            return true;
        }
        else {
            return false;
        }
    }
    
}
