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
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
@Path("/appsList")
@ManagedBean
@Dependent
@Stateless
public class AppsListApi {

    @PersistenceContext(unitName = "kenexxPU")
    private EntityManager em;

    @Inject
    AdminService adminService;

    @GET
    @Produces(MediaType.TEXT_XML)
    public String getAppsList() {

        String resource = "<?xml version='1.0' ?>"
                + "<hello>Its an API test </hello> ";
        return resource;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAppsListJson(@FormParam("profileid") Long profileId) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        
        
        
            UserProfile userProfile = adminService.findUserProfileById(profileId);
            List<Apps> apps = adminService.getAppsByUser(userProfile);
            for(Apps app:apps){

                obj.put("appName", app.getAppName());
                obj.put("email", app.getEmail());
                obj.put("profileid", app.getProfileid().getId());
                obj.put("id", app.getId());
                obj.put("file", app.getSheet());
                obj.put("sheet", app.getTabs()); 
                obj.put("platform", app.getPlatform());  
                obj.put("fileid", app.getFileid()); 
                arr.put(obj);

                obj = new JSONObject();
            }
        return arr.toString();
    }

}
