/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.kenexx.model.Apps;
import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class AppList implements Serializable{
    
    
    @Inject
    private AdminService adminService;
    
    List<Apps> appList=new ArrayList<Apps>();
     
    @PostConstruct
    public void init(){
        FacesContext cont = FacesContext.getCurrentInstance();
        //String email = String.valueOf(cont.getExternalContext().getSessionMap().get("email"));
        appList=adminService.getAppsByUser((UserProfile)(cont.getExternalContext().getSessionMap().get("userprofileid")));
    }

    public List<Apps> getAppList() {
        return appList;
    }

    public void setAppList(List<Apps> appList) {
        this.appList = appList;
    }
    
    public boolean totalAppsListForUser(){
        if(appList.size()>= 2)
        {
            return false;
        }
        return true;
    }
    
}
