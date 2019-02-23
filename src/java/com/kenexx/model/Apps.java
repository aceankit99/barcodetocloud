/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity

@Table(name = "apps")
public class Apps implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPS_SEQ")
    @SequenceGenerator(name = "APPS_SEQ", sequenceName = "APPS_SEQ", allocationSize = 50)
    @Column(name = "ID")
    private Long id;  
    
    @Column(name = "AppName")
    private  String appName;
    
    @Column(name = "ScannedItem")
    private  String scannedItem;
    
    @Column(name = "useOfBarcode")
    private  String useOfBarcode;
    
    @Column(name = "Platform")
    private  String platform;
    
    @Column(name="FileID")
    private String sheet;
    
    @Column(name="file_id")
    private String fileid;
    
    @Column(name="SheetID")
    private String tabs;

   @Column(name="UserEmail")
    private String email;
   
   @JoinColumn(name = "profileid", referencedColumnName = "user_id")
   @OneToOne(targetEntity=com.kenexx.model.UserProfile.class)
   private UserProfile profileid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getScannedItem() {
        return scannedItem;
    }

    public void setScannedItem(String scannedItem) {
        this.scannedItem = scannedItem;
    }

    public String getUseOfBarcode() {
        return useOfBarcode;
    }

    public void setUseOfBarcode(String useOfBarcode) {
        this.useOfBarcode = useOfBarcode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfile getProfileid() {
        return profileid;
    }

    public void setProfile_id(UserProfile profileid) {
        this.profileid = profileid;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }
    
    
}
