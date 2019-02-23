/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
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
import com.kenexx.model.UserFiles;
import com.kenexx.model.UserProfile;
import com.kenexx.model.Users;
import com.kenexx.service.AdminService;
import com.kenexx.web.WebSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.Drive.Files;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;

/**
 *
 * @author user
 */
@Named
@ViewScoped
public class Dashboard implements Serializable {

    private String ProfileName;
    private String profilePicture;
    private String parameterOne;
    private String username;
    private final String SECRET_id = "y-xlxc0iyHZcmdaj8vfLOBRe";
    private final String Client_id = "426617506714-o0tre34l8tji9ch4m8rmjd4kksgqdveg.apps.googleusercontent.com";
    private final String REDIRECT_URI = "http://barcode.constacloud.com:8080/kenexx/dashboard.xhtml";
    Userinfoplus userInfo;
    
    
    private UserProfile user = new UserProfile();
    private UserFiles uf = new UserFiles();
    List<UserFiles> usfile = new ArrayList();
    Users userToken = new Users();
    private List<Users> usaccountslist = new ArrayList();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
     
    GoogleCredential credential;
    JsonFactory jsonFactory = new JacksonFactory();
    HttpTransport httpTransport = new NetHttpTransport();
    GoogleTokenResponse response;
     
    @Inject
    private AdminService adminService;
    @Inject
    private WebSession webSession;

  @PostConstruct
   public void construct()  {
        FacesContext cont = FacesContext.getCurrentInstance();
       //username = (String) cont.getExternalContext().getSessionMap().get("user");
        String email = String.valueOf(cont.getExternalContext().getSessionMap().get("email"));
        String selectedGmailAccount = webSession.getSelectedGmailAccount();
        
        user=adminService.findUserProfilebyEmail(email);
        if(user!=null){
            username=user.getUserName();
        }
        usaccountslist = adminService.getAllAccountsListForUsers(user);
        if(selectedGmailAccount!=null){
           
            try {
           userToken = adminService.findUserbyEmail(selectedGmailAccount);
           
            credential = new GoogleCredential.Builder()
                   .setClientSecrets(Client_id, SECRET_id)
                   .setJsonFactory(jsonFactory).setTransport(httpTransport).build()
                  .setRefreshToken(userToken.getRefreshToken());
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
             usfile=adminService.getUserFilesByUser(selectedGmailAccount);
            }catch(Exception e){
                                
                                }
            
        }

   }

    public List<Users> getUsaccountslist() {
        return usaccountslist;
    }

    public void setUsaccountslist(List<Users> usaccountslist) {
        this.usaccountslist = usaccountslist;
    }
    
    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String ProfileName) {
        this.ProfileName = ProfileName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public List<UserFiles> getUsfile() {
        return usfile;
    }

    public void setUsfile(List<UserFiles> usfile) {
        this.usfile = usfile;
    }
    
    public boolean isAppsExistWithEmail(String email){
        FacesContext cont = FacesContext.getCurrentInstance();
        if(adminService.getAppsByEmailForUser(email, (UserProfile)(cont.getExternalContext().getSessionMap().get("userprofileid"))).size() > 0){
            return true;
        }
        return false;
    }
    
    public String saveBarcode(UserFiles uf){
    webSession.setFiles(uf);
     return "barcode";   
    } 
    
}
