/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.kenexx.model.Users;
import com.kenexx.service.AdminService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.kenexx.model.UserProfile;
import com.kenexx.web.WebSession;
import javax.faces.application.FacesMessage;
/**
 *
 * @author user
 */
@Named
@ViewScoped
public class UserProfilePage implements Serializable {
   
    @Inject
    private AdminService adminService;
    
    
     
    Users userobj=new Users();
    UserProfile userProfile=new UserProfile();
    UserProfile newuserProfile=new UserProfile();
    String email;
    String newPassword;
    public static final String SALT = "kennex-salt";
    
    
    FacesContext context = FacesContext.getCurrentInstance();
    
    @PostConstruct
    public void init(){
        FacesContext cont = FacesContext.getCurrentInstance();
       
//        email = String.valueOf(cont.getExternalContext().getSessionMap().get("email"));
//        userProfileId = Long.valueOf(cont.getExternalContext().getSessionMap().get("email"));
//        String username=String.valueOf(cont.getExternalContext().getSessionMap().get("user"));
        userProfile=(UserProfile)(cont.getExternalContext().getSessionMap().get("userprofileid"));
        newuserProfile=(UserProfile)(cont.getExternalContext().getSessionMap().get("userprofileid"));
       
    }
    
    public String updateUserProfile(UserProfile user){
        FacesContext context = FacesContext.getCurrentInstance();
        if(!adminService.searchUserByConditionForEditProfile(user)){
            adminService.updateUserProfile(user);
            context.getExternalContext().getSessionMap().put("userprofileid", user);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Updated Successfully", ""));
        }
        else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! Entered Phone is already exists", ""));
        }
        return "userProfile";
    }
    
    public String updateUserProfilePassword(UserProfile user){
        
        FacesContext context = FacesContext.getCurrentInstance();
        String saltedPassword = SALT + this.newuserProfile.getPassword();
        String hashedPassword = ViewSupport.generateHash(saltedPassword);
        this.newuserProfile.setPassword(hashedPassword);
        
         if(!adminService.searchUserByConditionForChangePasswordProfile(this.newuserProfile)){ 
           context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! old password is wrong", ""));  
         }
         else {
          saltedPassword = SALT + this.getNewPassword();
          hashedPassword = ViewSupport.generateHash(saltedPassword);
          this.newuserProfile.setPassword(hashedPassword);
          adminService.updateUserProfile(this.newuserProfile);
          context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully changed your password", ""));
         }
        return "userProfile";
    }
    
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
   
    public boolean initProfileModal(){

        if(userProfile==null){
            return true;
        }
        return false;
    }

    public UserProfile getNewuserProfile() {
        return newuserProfile;
    }

    public void setNewuserProfile(UserProfile newuserProfile) {
        this.newuserProfile = newuserProfile;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
  
}
