/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.controller;

import com.sendgrid.*;
import java.io.IOException;

import com.kenexx.model.Apps;
import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import com.kenexx.service.EmailService;
import com.kenexx.view.ForgetPassword;
import com.kenexx.web.WebSession;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


/**
 *
 * @author user
 */
@Named
@RequestScoped
public class AdminController implements Serializable {

    @Inject
    private AdminService adminService;
    
    @Inject
    private EmailService emailService;

    @Inject
    private WebSession webSession;
    
    private static Logger logger = Logger.getLogger("com.kennex.controller");
    
    public String addApps(Apps a) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //String email = String.valueOf(context.getExternalContext().getSessionMap().get("email"));
            //a.setEmail(email);
            a.setProfile_id((UserProfile)(context.getExternalContext().getSessionMap().get("userprofileid")));
            adminService.persist(a);
        } catch (Exception e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error saving", e.getMessage()));
            return null;
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "App saved", ""));

        return "appList.xhtml?faces-redirect=true";
    }

    public String updateApps(Apps a) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            adminService.updateApps(a);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error saving", e.getMessage()));
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Apps Updated", ""));

        return "appList.xhtml?faces-redirect=true";
    }

    public String newApp(Apps a) throws Exception {
        webSession.setApps(new Apps());
        return "createApp";
    }

    public String editApps(Apps a) throws Exception {

        webSession.setApps(a);
        return "createApp";
    }
    
    public String deleteApp() throws Exception {

        adminService.deleteApp(webSession.getApps());
        return "appList";
    }
    
    public String newUser() throws Exception {
       //logger.info("New User");
        webSession.setUserprofile(new UserProfile());
        return "signUp.xhtml";
    }
    
    public String forgetPassword() throws Exception {
       //logger.info("New User");
        webSession.setForgetPassword(new ForgetPassword());
        return "forgetPassword.xhtml";
    }
    
    public String deleteUser() throws Exception {
       //logger.info("New User");
        adminService.deleteUser(webSession.getUsers());
        return "dashboard";
    }
    
    
    public String saveNewUser(UserProfile up){
       
        FacesContext context = FacesContext.getCurrentInstance();
        try {
          if(adminService.searchUserByCondition(up)){
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! Entered Email or Phone is already exists", ""));
                   return "signUp"; 
          }
          adminService.persist(up);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error saving", e.getMessage()));
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New User Saved Successfully", ""));
        return "login";
    }
    
    public String updateUserProfileForForgetPassword(UserProfile up){
       
        FacesContext context = FacesContext.getCurrentInstance();
        try {
                
                
                emailService.sendForgetEmailService(up);
//                SendGrid sg = new SendGrid("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");
//                Request request1 = new Request();
//                request1.setMethod(Method.POST);
//                request1.setEndpoint("mail/send");
//                request1.setBody(mail.build());
//                Response response1 = sg.api(request1);
                
          
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error Occured", e.getMessage()));
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check your mail we have sent you the mail regarding forget password request", ""));
        return "forgetPassword";
    }
    
    public String updateUserProfileForResetPassword(UserProfile up){
       
        FacesContext context = FacesContext.getCurrentInstance();
        try {
                
                
                adminService.updateUserProfile(up);
//                SendGrid sg = new SendGrid("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");
//                Request request1 = new Request();
//                request1.setMethod(Method.POST);
//                request1.setEndpoint("mail/send");
//                request1.setBody(mail.build());
//                Response response1 = sg.api(request1);
                
          
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error Occured", e.getMessage()));
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully resetted your password", ""));
        return "login";
    }
    
    public String getExcelSheetsByAccount(String email){
        webSession.setSelectedGmailAccount(email);
        return "excelSheetsForAccounts";
    }
}
