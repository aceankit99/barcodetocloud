/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import com.kenexx.view.ViewSupport;
import com.kenexx.web.WebSession;
import java.io.Serializable;
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
@Named
@ViewScoped
public class Login implements Serializable {

    @Inject
    private AdminService adminService;
    @Inject
    private WebSession webSession;
    private String username;
    private String password;
    private UserProfile userProfile;
    public static final String SALT = "kennex-salt";
    
    @PostConstruct
    public void init() {
        FacesContext cont = FacesContext.getCurrentInstance();
        if(String.valueOf(cont.getExternalContext().getSessionMap().get("email"))!=null){
        //FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/dashboard.xhtml?faces-redirect=true");  
        }
        else {
            //FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/login.xhtml");  
        }
         
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login(String username, String password) {
        
        String saltedPassword = SALT + password;
        String hashedPassword = ViewSupport.generateHash(saltedPassword);
        webSession.setLoggedInUserEmail(username);
        userProfile = adminService.loginWithEmail(username, hashedPassword);
       FacesContext context = FacesContext.getCurrentInstance();
        if (userProfile!=null) {
           
            context.getExternalContext().getSessionMap().put("email", username);
            context.getExternalContext().getSessionMap().put("userprofileid", userProfile);
            return "dashboard";
        } else {
            
            return "null";
           
        }
    }

    public String GoogleAuth() {
        String url = "https://accounts.google.com/o/oauth2/auth?redirect_uri=http://barcode.constacloud.com:8080/kenexx/dashboard.xhtml&response_type=code&client_id=426617506714-o0tre34l8tji9ch4m8rmjd4kksgqdveg.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/drive+https://www.googleapis.com/auth/drive.file+https://www.googleapis.com/auth/userinfo.email&prompt=consent&access_type=offline";
       
        return url;
    }

}
