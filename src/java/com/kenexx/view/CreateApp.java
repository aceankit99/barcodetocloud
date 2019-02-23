/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.kenexx.controller.AdminController;
import com.kenexx.model.Apps;
import com.kenexx.model.UserFiles;
import com.kenexx.model.UserProfile;
import com.kenexx.model.Users;
import com.kenexx.service.AdminService;
import com.kenexx.web.WebSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
public class CreateApp implements Serializable {

    @Inject
    private WebSession webSession;
    @Inject
    private AdminController adminController;
    @Inject
    AdminService adminService;
    List<UserFiles> usfile = new ArrayList();
    HashMap<String, String> usfileList = new HashMap<String, String>();
    private UserFiles uf = new UserFiles();
    private Apps editApps;
    private Users appuser;
    GoogleCredential credential;
    JsonFactory jsonFactory = new JacksonFactory();
    HttpTransport httpTransport = new NetHttpTransport();
    GoogleTokenResponse response;
    Userinfoplus userInfo;
    private final String SECRET_id = "y-xlxc0iyHZcmdaj8vfLOBRe";
    private final String Client_id = "426617506714-o0tre34l8tji9ch4m8rmjd4kksgqdveg.apps.googleusercontent.com";
    List<String> tabsList = new ArrayList();
    List<String> sheetsList = new ArrayList();
    List<String> accountsList = new ArrayList();
    List<Users> allUsers = new ArrayList();
    UserProfile userProfile;
    @PostConstruct
    public void init() {
        FacesContext cont = FacesContext.getCurrentInstance();
        userProfile = (UserProfile)(cont.getExternalContext().getSessionMap().get("userprofileid"));
        allUsers = adminService.getAllAccountsListForUsers(userProfile);
        accountsList.clear();
        for (Users users : allUsers){
            accountsList.add(users.getEmailAddress());  
        }
        //System.out.println(accountsList.get(0));
           
           //System.out.println(usfile.get(0).getName());
         
        editApps = webSession.getApps();
        if(editApps.getEmail()!=null){
            appuser = adminService.findUserbyEmail(editApps.getEmail());
            usfile = adminService.getUserFilesByUser(editApps.getEmail());
        }
        else {
            appuser = adminService.findUserbyEmail(accountsList.get(0));
            usfile = adminService.getUserFilesByUser(accountsList.get(0));
        }
       if(editApps.getId()!=null){
           
           tabsList=getSheetsByFileName(editApps.getFileid());
           //System.out.println(editApps.getSheet());
       }
       else{
       if(usfile.size() > 0){
            tabsList=getSheetsByFileName(usfile.get(0).getFileID());
       }
       else {
           try {
           credential = new GoogleCredential.Builder()
                   .setClientSecrets(Client_id, SECRET_id)
                   .setJsonFactory(jsonFactory).setTransport(httpTransport).build()
                  .setRefreshToken(appuser.getRefreshToken());
            cont.getExternalContext().getSessionMap().put("credential", credential); 
            String str1 = credential.getAccessToken();
             
            Oauth2 oauth2 = new Oauth2.Builder(httpTransport, jsonFactory, credential).setApplicationName("JavaGmailLogin").build();
                        Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(str1).execute();
                        userInfo = oauth2.userinfo().v2().me().get().execute();
                       
                        if (userInfo.getId() != null && userInfo.getId() != "") {

                            Drive service = new Drive.Builder(httpTransport, jsonFactory, credential)
                                    .setApplicationName("JavaGmailLogin")
                                    .build();

                            FileList result = service.files().list()
                                    .setPageSize(50)
                                    .setQ("mimeType='application/vnd.google-apps.spreadsheet'")
                                    .setFields("nextPageToken, files(id, name)")
                                    .execute();
                            List<File> files = result.getFiles();

                            usfile = adminService.getUserFilesByUser(userInfo.getEmail());
                            for (File file : files) {
                                if (!usfile.stream().filter(f -> f.getFileID().equals(file.getId())).findFirst().isPresent()) {
                                    uf.setName(file.getName());
                                    uf.setFileID(file.getId());
                                    uf.setEmail(userInfo.getEmail());
                                    adminService.persist(uf);
                                }
                            }
                        }
           
//           for(UserFiles usFiles : usfile){
//             usfileList.put(usFiles.getName(), usFiles.getFileID());
//             System.out.println("Name" + usFiles.getName() +" " + usFiles.getFileID());
//             System.out.println(usfileList.size());
//           }
           
           }catch(Exception e) { }
       }
     }
      
    }

    public Apps getEditApps() {
        return editApps;
    }

    public void setEditApps(Apps editApps) {
        this.editApps = editApps;
    }

    public String addUpdateCondition() throws Exception {
        
        //this.editApps.setFileid(this.editApps.);
        this.editApps.setSheet(adminService.findUserFilesById(this.editApps.getFileid()).getName());
        return this.editApps.getId() == null ? adminController.addApps(this.editApps) : adminController.updateApps(this.editApps);
    }

    public String backToList() {

        return "appList.xhtml?faces-redirect=true";
    }

    public List<UserFiles> getUsfile() {
        return usfile;
    }

    public void setUsfile(List<UserFiles> usfile) {
        this.usfile = usfile;
    }
    
    public List<UserFiles> getFilesByFileName(String email) {
        sheetsList.clear();
        usfile = adminService.getUserFilesByUser(email); 
        
        return usfile;
    }

    public List<String> getSheetsByFileName(String sheet) {
        tabsList.clear();
        System.out.println(sheet);
        UserFiles uf = adminService.findFileNamebyFileId(sheet);
        
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        credential = new GoogleCredential.Builder()
                .setClientSecrets(Client_id, SECRET_id)
                .setJsonFactory(jsonFactory).setTransport(httpTransport).build()
                .setRefreshToken(appuser.getRefreshToken());
        
        
        
        Sheets service = new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("JavaGmailLogin")
                .build();
        
        Spreadsheet sp = null;
        try {
            //System.out.println(uf.getName());
            sp = service.spreadsheets().get(sheet).execute();
            //System.out.println("Hiigfdg"+sp);
        } catch (IOException ex) {

        }
        List<Sheet> sheets = sp.getSheets();
        for (Sheet st : sheets) {
           
            tabsList.add(st.getProperties().getTitle());
        }
        return tabsList;
    }

    public void SetFIletowebSession(String fileID) {
        webSession.setFileID(fileID);
    }

    public List<String> getTabsList() {
        return tabsList;
    }

    public void setTabsList(List<String> tabsList) {
        this.tabsList = tabsList;
    }

    public List<String> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(List<String> accountsList) {
        this.accountsList = accountsList;
    }

    public HashMap<String, String> getUsfileList() {
        return usfileList;
    }

    public void setUsfileList(HashMap<String, String> usfileList) {
        this.usfileList = usfileList;
    }
    
    
    
}
