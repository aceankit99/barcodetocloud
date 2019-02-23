/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.api;

/**
 *
 * @author user
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.kenexx.model.Apps;
import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import com.kenexx.view.ViewSupport;
import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author user
 */
@Path("/login")
@Stateless
public class LoginApi {

    
    @PersistenceContext(unitName = "kenexxPU")
    private EntityManager em;

    @Inject
    AdminService adminService;
    
    public static final String SALT = "kennex-salt";

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginAuthentication(@FormParam("email") String email, @FormParam("password") String password) throws JSONException {
        
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        
        String saltedPassword = SALT + password;
        String hashedPassword = ViewSupport.generateHash(saltedPassword);
        UserProfile userProfile = adminService.loginWithApi(email, hashedPassword);
        if(userProfile!=null){
            obj.put("profileid", userProfile.getId());
            obj.put("name", userProfile.getUserName());
            obj.put("address", userProfile.getAddress());
            obj.put("gender", userProfile.getGender());
            obj.put("phone", userProfile.getPhone());
            obj.put("email", userProfile.getEmail());
            obj.put("plan", userProfile.getPlans().getPlanName());
            obj.put("status", "success");
            obj.put("message", "Login Successully");
            //obj.put("password", hashedPassword);
        }
        else {
           obj.put("profileid", "");
           obj.put("name", "");
           obj.put("address", "");
           obj.put("gender", "");
           obj.put("phone", "");
           obj.put("email", "");
           obj.put("plan", "");
           obj.put("status", "error");
           obj.put("message", "Entered User Id or Password is wrong");
           //obj.put("password", hashedPassword);
        }
        
        arr.put(obj); 
        return arr.toString();
    }

}
