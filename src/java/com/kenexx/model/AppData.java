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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity

@Table(name = "appdata")
public class AppData implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPDATA_SEQ")
    @SequenceGenerator(name = "APPDATA_SEQ", sequenceName = "APPDATA_SEQ", allocationSize = 50)
    @Column(name = "appdataid")
    private Long id;  
    
    @Column(name = "accountemail")
    private  String accountEmail;
    
    @Column(name = "barcodedata")
    private  String barcodeData;
    
    @Column(name = "appid")
    private  String appId;
    
    @Column(name = "approw")
    private  String appRow;
    
    @Column(name="appcolumn")
    private String appColumn;
    
    @Column(name="appsheet")
    private String appSheet;
    
    @Column(name="appfileid")
    private String appFileId;

   @Column(name="applongitude")
    private Double appLongitude;
   
   @Column(name="applatitude")
    private Double appLatitude;
   
   @JoinColumn(name = "profileid", referencedColumnName = "user_id")
   @ManyToOne(targetEntity=com.kenexx.model.UserProfile.class)
   private UserProfile profileid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppRow() {
        return appRow;
    }

    public void setAppRow(String appRow) {
        this.appRow = appRow;
    }

    public String getAppColumn() {
        return appColumn;
    }

    public void setAppColumn(String appColumn) {
        this.appColumn = appColumn;
    }

    public String getAppSheet() {
        return appSheet;
    }

    public void setAppSheet(String appSheet) {
        this.appSheet = appSheet;
    }

    public String getAppFileId() {
        return appFileId;
    }

    public void setAppFileId(String appFileId) {
        this.appFileId = appFileId;
    }

    public Double getAppLongitude() {
        return appLongitude;
    }

    public void setAppLongitude(Double appLongitude) {
        this.appLongitude = appLongitude;
    }

    public Double getAppLatitude() {
        return appLatitude;
    }

    public void setAppLatitude(Double appLatitude) {
        this.appLatitude = appLatitude;
    }

    

    public UserProfile getProfileid() {
        return profileid;
    }

    public void setProfileid(UserProfile profileid) {
        this.profileid = profileid;
    }
}
