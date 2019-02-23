/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.web;

import com.kenexx.model.Apps;
import com.kenexx.model.Plans;
import com.kenexx.model.UserProfile;
import com.kenexx.model.Users;
import com.kenexx.model.UserFiles;
import com.kenexx.view.ForgetPassword;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author user
 */
@Named
@SessionScoped
public class WebSession implements Serializable {
    
    UserFiles files=new UserFiles();
    Apps apps=new Apps();
    Plans plans;
    UserProfile userprofile;
    private Users users;
    private String fileID;
    private String loggedInUserEmail;
    private String selectedGmailAccount;
    
    
    
    ForgetPassword forgetPassword;

    public Apps getApps() {
        return apps;
    }

    public void setApps(Apps apps) {
        this.apps = apps;
    }
    public UserFiles getFiles() {
        return files;
    }

    public void setFiles(UserFiles files) {
        this.files = files;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String loggedInUserEmail) {
        this.loggedInUserEmail = loggedInUserEmail;
    }
    
    public Plans getPlans() {
        return plans;
    }

    public void setPlans(Plans plans) {
        this.plans = plans;
    }

    public UserProfile getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(UserProfile userprofile) {
        this.userprofile = userprofile;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getSelectedGmailAccount() {
        return selectedGmailAccount;
    }

    public void setSelectedGmailAccount(String selectedGmailAccount) {
        this.selectedGmailAccount = selectedGmailAccount;
    }

    public ForgetPassword getForgetPassword() {
        return forgetPassword;
    }

    public void setForgetPassword(ForgetPassword forgetPassword) {
        this.forgetPassword = forgetPassword;
    }

    

    
    
    
}
