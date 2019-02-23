/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.model;


import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity

@Table(name = "app_users")
public class Users implements  Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 50)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", length = 512)
    private String name;

    @Column(name = "email_address", length = 512)
    private String emailAddress;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "is_admin")
    private boolean admin;
    
    @Column(name = "imageurl")
    private String url;
    
    @Column(name = "files")
    private List<String> files;
    
    @Column(name = "accesstoken")
    private String accessToken;
    
    @Column(name = "refreshtoken")
    private String refreshToken;
    
    @JoinColumn(name = "userprofile_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserProfile userprofileid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getCode() {
        return accessToken;
    }

    public void setCode(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }   

    public UserProfile getUserprofileid() {
        return userprofileid;
    }

    public void setUserprofileid(UserProfile userprofileid) {
        this.userprofileid = userprofileid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    
}
