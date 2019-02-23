/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.service;

import com.kenexx.model.Apps;
import com.kenexx.model.AppData;
import com.kenexx.model.UserFiles;
import com.kenexx.model.UserProfile;
import com.kenexx.model.Users;

import com.kenexx.model.Plans;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 *
 * @author user
 */
@Named
@Stateless
public class AdminService {

    @PersistenceContext(unitName = "kenexxPU")
    private EntityManager em;

    public void persist(Users u) {
        em.persist(u);
    }

    public void persist(Apps a) {
        em.persist(a);
    }
    
    public void persist(AppData a) {
        em.persist(a);
    }

    public void updateApps(Apps a) {
        em.merge(a);
    }
    
    public void updateAppData(AppData a) {
        em.merge(a);
    }

    public void update(Users u) {
        em.merge(u);
    }

    public void persist(UserFiles u) {
        em.persist(u);
    }

    public void persist(UserProfile u) {
        em.persist(u);
    }
    
    

    public void updateUserProfile(UserProfile u) {
        em.merge(u);
    }

    public List<Users> getAppUser() {
        TypedQuery<Users> query = em.createQuery("select u from Users u", Users.class);
        return (List<Users>) query.getResultList();
    }

    public List<UserFiles> getUserFiles() {
        TypedQuery<UserFiles> query = em.createQuery("select u from UserFiles u", UserFiles.class);
        return (List<UserFiles>) query.getResultList();
    }

    public List<UserFiles> getUserFilesByUser(String email) {
        TypedQuery<UserFiles> query = em.createQuery("select u from UserFiles u where u.email=:email", UserFiles.class);
        query.setParameter("email", email);
        return (List<UserFiles>) query.getResultList();
    }

    public Users findUserbyEmail(String email) {
        //System.out.println(email);
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.emailAddress = :email");
        query.setParameter("email", email);
        List<Users> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;

    }
    
    public List<Users> getAllAccountsListForUsers(UserProfile userProfileId) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.userprofileid = :USERPROFILEID");
        query.setParameter("USERPROFILEID", userProfileId);
        return (List<Users>) query.getResultList();
    }

    public List<String> getFIlesByEmail(String email) {
        Query query = em.createQuery("SELECT u.files FROM UserFiles u WHERE u.user = :email");
        query.setParameter("email", email);
        return (List<String>) query.getResultList();
    }

    public List<Apps> getApps() {
        TypedQuery<Apps> query = em.createQuery("select a from Apps a", Apps.class);
        return (List<Apps>) query.getResultList();
    }

    public List<Apps> getAppsByEmail(String email) {
        TypedQuery<Apps> query = em.createQuery("select a from Apps a where a.email=:email", Apps.class);
        query.setParameter("email", email);
        return (List<Apps>) query.getResultList();
    }
    
    public List<Apps> getAppsByEmailForUser(String email, UserProfile up) {
        TypedQuery<Apps> query = em.createQuery("select a from Apps a where a.email=:email and a.profileid=:profileid", Apps.class);
        query.setParameter("email", email);
        query.setParameter("profileid", up);
        return (List<Apps>) query.getResultList();
    }
    
    public List<Apps> getAppsByUser(UserProfile up) {
        TypedQuery<Apps> query = em.createQuery("select a from Apps a where a.profileid=:profileid", Apps.class);
        query.setParameter("profileid", up);
        return (List<Apps>) query.getResultList();
    }

    public void deleteApp(Apps a) throws Exception {
        if (!em.contains(a)) {
            a = em.merge(a);
        }
        em.remove(a);
    }
    
    public void deleteAppData(AppData a) throws Exception {
        if (!em.contains(a)) {
            a = em.merge(a);
        }
        em.remove(a);
    }
    
    public void deleteUser(Users a) throws Exception {
        if (!em.contains(a)) {
            a = em.merge(a);
        }
        em.remove(a);
    }
    
    public Plans findPlansById(Long id) {
        return em.find(Plans.class, id);
    }
    
    public UserFiles findUserFilesById(String id) {
        Query query = em.createQuery("SELECT u FROM UserFiles u WHERE u.fileID = :name");
        query.setParameter("name", id);
        List<UserFiles> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;
    }
    
    public UserProfile findUserProfileById(Long id) {
        return em.find(UserProfile.class, id);
    }

    public UserFiles findFileIDbyFileName(String name) {

        Query query = em.createQuery("SELECT u FROM UserFiles u WHERE u.name = :name");
        query.setParameter("name", name);
        List<UserFiles> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;

    }
    
    public UserFiles findFileNamebyFileId(String fileid) {

        Query query = em.createQuery("SELECT u FROM UserFiles u WHERE u.fileID = :fileid");
        query.setParameter("fileid", fileid);
        List<UserFiles> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;

    }

    public UserProfile findUserProfilebyEmail(String email) {

        Query query = em.createQuery("SELECT u FROM UserProfile u WHERE u.email = :email");
        query.setParameter("email", email);
        List<UserProfile> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;

    }
    
    public UserProfile findUserProfilebyForgetPasswordCode(String code) {

        Query query = em.createQuery("SELECT u FROM UserProfile u WHERE u.forgetPasswordCode = :forgetPasswordCode AND u.forgetPasswordRequest = :forgetPasswordRequest");
        query.setParameter("forgetPasswordCode", code);
        query.setParameter("forgetPasswordRequest", "1");
        List<UserProfile> u = query.getResultList();
        if (u.size() > 0) {
            return u.get(0);
        }
        return null;

    }

    public UserProfile loginWithEmail(String email, String password) {
        TypedQuery<UserProfile> query = em.createQuery("select a from UserProfile a where a.email=:email and a.password=:password", UserProfile.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<UserProfile> a = query.getResultList();
        if (a.size() > 0) {
            return a.get(0);
        }
        else{
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error logging in", "Invalid Credentials"));
         return null;
        }
        
    }
    
    public UserProfile loginWithApi(String email, String password) {
        TypedQuery<UserProfile> query = em.createQuery("select a from UserProfile a where a.email=:email and a.password=:password", UserProfile.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<UserProfile> a = query.getResultList();
        if (a.size() > 0) {
            return a.get(0);
        }
        else{
             return null;
        }
        
    }
    
    public List<Plans> getAllPlans(){
      TypedQuery<Plans> query = em.createQuery("select p from Plans p", Plans.class);
      return (List<Plans>) query.getResultList();  
    }
    
    public List<Plans> getAllActivePlans(){
      TypedQuery<Plans> query = em.createQuery("select p from Plans p where p.planStatus=:plansStatus", Plans.class);
      query.setParameter("plansStatus", "1");
      return (List<Plans>) query.getResultList();  
    }
    
    public boolean searchUserByCondition(UserProfile up){
        
        Query query = em.createQuery("SELECT b FROM UserProfile b WHERE b.email  = :EMAIL OR b.phone = :PHONE", UserProfile.class);
        query.setParameter("EMAIL", up.getEmail());
        query.setParameter("PHONE", up.getPhone());
        
        
        List<UserProfile> upn = query.getResultList();
        //logger.info(String.valueOf(mg.size()));
        if (upn.size() > 0) {
            
            return true;
        }
        return false;
    }
    
    
     public boolean searchUserByConditionForEditProfile(UserProfile up){
        
        Query query = em.createQuery("SELECT b FROM UserProfile b WHERE b.phone = :PHONE AND b.id <> :ID", UserProfile.class);
        query.setParameter("ID", up.getId());
        query.setParameter("PHONE", up.getPhone());
        
        
        List<UserProfile> upn = query.getResultList();
        //logger.info(String.valueOf(mg.size()));
        if (upn.size() > 0) {
            
            return true;
        }
        return false;
    }
     
    public boolean searchUserByConditionForChangePasswordProfile(UserProfile up){
        
        Query query = em.createQuery("SELECT b FROM UserProfile b WHERE b.password = :PASSWORD AND b.id = :ID", UserProfile.class);
        query.setParameter("ID", up.getId());
        query.setParameter("PASSWORD", up.getPassword());
        
        
        List<UserProfile> upn = query.getResultList();
        //logger.info(String.valueOf(mg.size()));
        if (upn.size() > 0) {
            
            return true;
        }
        return false;
    }
    
    

}
