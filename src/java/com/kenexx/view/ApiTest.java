/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.kenexx.model.Users;
import com.kenexx.model.AppData;
import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Path("/test")
@ManagedBean
@Dependent
@Stateless
public class ApiTest {

    @PersistenceContext(unitName = "kenexxPU")
    private EntityManager em;

    @Inject
    AdminService adminService;

    private String code;
    GoogleCredential credential;
    private final String SECRET_id = "y-xlxc0iyHZcmdaj8vfLOBRe";
    private final String Client_id = "426617506714-o0tre34l8tji9ch4m8rmjd4kksgqdveg.apps.googleusercontent.com";
    private final String REDIRECT_URI = "http://barcode.constacloud.com:8080/kenexx/dashboard.xhtml";

    @GET
    @Produces(MediaType.TEXT_XML)
    public String sayHello() {

        String resource = "<?xml version='1.0' ?>"
                + "<hello>Its an API test </hello> ";
        return resource;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String sayHelloHTML(@FormParam("profileid") Long profileId, @FormParam("email") String userEmail, @FormParam("barcode") String barcode, @FormParam("appID") String appID, @FormParam("row") String row, @FormParam("column") String column, @FormParam("sheet") String sheet, @FormParam("fileid") String fileId, @FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude) {
        
        System.out.println(barcode + "_" + appID + "_" + column + "_" + row + "_" + sheet);
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Users user = adminService.findUserbyEmail(userEmail);
        UserProfile userProfile = adminService.findUserProfileById(profileId);
        AppData appData = new AppData();
        
        try { 
           
            
            //credential=new GoogleCredential().setAccessToken(user.getCode());
            credential = new GoogleCredential.Builder()
                    .setClientSecrets(Client_id, SECRET_id)
                    .setJsonFactory(jsonFactory).setTransport(httpTransport).build()
                    .setRefreshToken(user.getRefreshToken());
            String cellID = sheet + "!" + column + row;
            Sheets service = new Sheets.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("JavaGmailLogin")
                    .build();

            List<List<Object>> li = Arrays.asList(
                    Arrays.asList(barcode));

            ValueRange requestBody = new ValueRange();
            requestBody.setValues(li);
            Sheets.Spreadsheets.Values.Update request = service.spreadsheets().values().update(fileId, cellID, requestBody);
            request.setValueInputOption("RAW");
            UpdateValuesResponse resp = request.execute();
            //System.out.println(request);
            //System.out.println(resp);
            appData.setAccountEmail(userEmail);
            appData.setProfileid(userProfile);
            appData.setBarcodeData(barcode);
            appData.setAppId(appID);
            appData.setAppRow(row);
            appData.setAppColumn(column);
            appData.setAppSheet(sheet);
            appData.setAppFileId(fileId);
            appData.setAppLongitude(longitude);
            appData.setAppLatitude(latitude);
            adminService.persist(appData);
            

        } catch (IOException ex) {
            System.out.println("kasdkashdkhas");
            Logger.getLogger(ApiTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String resource = profileId + "_" + userEmail + "_" + barcode + "_" + appID + "_" + column + "_" + row + "_" + sheet + "_" + fileId + "_" + latitude + "_" + longitude;
        return resource;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}


