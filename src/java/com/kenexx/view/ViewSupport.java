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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.kenexx.model.UserFiles;
import com.kenexx.model.Users;
import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import com.kenexx.web.WebSession;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
public class ViewSupport implements Serializable {

    @Inject
    private AdminService adminService;
    @Inject
    private WebSession webSession;

    private String ProfileName;
    private String profilePicture;
    private String username;
    private String parameterOne;
    private String forgetPasswordCode;
    private final String SECRET_id = "y-xlxc0iyHZcmdaj8vfLOBRe";
    private final String Client_id = "426617506714-o0tre34l8tji9ch4m8rmjd4kksgqdveg.apps.googleusercontent.com";
    private final String REDIRECT_URI = "http://barcode.constacloud.com:8080/kenexx/dashboard.xhtml";
    Userinfoplus userInfo;
    List<UserFiles> usfile = new ArrayList();
    List<Users> allUsers = new ArrayList();
    GoogleCredential credential;
    JsonFactory jsonFactory = new JacksonFactory();
    HttpTransport httpTransport = new NetHttpTransport();
    GoogleTokenResponse response;

    private Users user = new Users();
    private UserFiles uf = new UserFiles();
    Users usertoken = new Users();
    UserProfile userprofile = new UserProfile();
    UserProfile userProfile;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        userProfile = (UserProfile) context.getExternalContext().getSessionMap().get("userprofileid");
        String email = String.valueOf(context.getExternalContext().getSessionMap().get("email"));
        usertoken = adminService.findUserbyEmail(email);
    }

    public String profile() throws IOException {
        Map<String, String> params1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        parameterOne = params1.get("code");
        //System.out.println("Code " + parameterOne);
        if (parameterOne != null) {

            allUsers = adminService.getAppUser();

            FacesContext cont = FacesContext.getCurrentInstance();
            username = String.valueOf(cont.getExternalContext().getSessionMap().get("user"));

            if (username.equals("null")) {
                //Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                //parameterOne = params.get("code");

                if (parameterOne == null) {
                    try {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                    } catch (IOException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
                jsonFactory = new JacksonFactory();
                httpTransport = new NetHttpTransport();
                response = null;
                try {

                    response = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(), Client_id, SECRET_id, parameterOne, REDIRECT_URI).execute();
                } catch (Exception e) {
                    
                }
                if (response != null) {
                    try {
                       
                        credential = new GoogleCredential.Builder()
                                //.setJsonFactory(jsonFactory)
                                .setJsonFactory(jsonFactory)
                                .setTransport(httpTransport)
                                .setClientSecrets(Client_id, SECRET_id).build()
                                //.setAccessToken(response.getAccessToken())
                                //.setRefreshToken(response.getRefreshToken());
                                .setFromTokenResponse(response);
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
                            user = adminService.findUserbyEmail(userInfo.getEmail());
                            if (user != null) {
                                if (!allUsers.stream().filter(u -> u.getCode().equals(credential.getAccessToken())).findFirst().isPresent()) {
                                    user.setName(userInfo.getName());
                                    user.setEmailAddress(userInfo.getEmail());
                                    user.setUrl(userInfo.getPicture());
                                    user.setCode(credential.getAccessToken());
                                    user.setRefreshToken(credential.getRefreshToken());
                                    user.setUserprofileid(userProfile);
                                    adminService.update(user);

                                }
                            } else {
                                if (!allUsers.stream().filter(u -> u.getCode().equals(credential.getAccessToken())).findFirst().isPresent()) {
                                    Users newUser = new Users();
                                    newUser.setName(userInfo.getName());
                                    newUser.setEmailAddress(userInfo.getEmail());
                                    newUser.setUrl(userInfo.getPicture());
                                    newUser.setCode(credential.getAccessToken());
                                    newUser.setRefreshToken(credential.getRefreshToken());
                                    newUser.setUserprofileid(userProfile);
                                    adminService.persist(newUser);

                                }

                            }

//                            ProfileName = userInfo.getName();
//                            profilePicture = userInfo.getPicture();
//                            FacesContext context = FacesContext.getCurrentInstance();
//                            if (userInfo.getName() == null) {
//                                context.getExternalContext().getSessionMap().put("user", userInfo.getEmail());
//                            } else {
//                                context.getExternalContext().getSessionMap().put("user", userInfo.getName());
//                            }

                            FacesContext context = FacesContext.getCurrentInstance();
                            //context.getExternalContext().getSessionMap().put("email", userInfo.getEmail());
                            context.getExternalContext().getSessionMap().put("credential", credential);

                            

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
//        } else {
//            FacesContext conte = FacesContext.getCurrentInstance();
//
//            String email = String.valueOf(conte.getExternalContext().getSessionMap().get("email"));
//
//            usertoken = adminService.findUserbyEmail(email);
//            conte.getExternalContext().getSessionMap().put("user", usertoken.getName());
//            conte.getExternalContext().getSessionMap().put("email", usertoken.getEmailAddress());
//
//            credential = new GoogleCredential.Builder()
//                    .setClientSecrets(Client_id, SECRET_id)
//                    .setJsonFactory(jsonFactory).setTransport(httpTransport).build()
//                    .setRefreshToken(usertoken.getRefreshToken());
//            conte.getExternalContext().getSessionMap().put("credential", credential);
//
//        }
        return "qww";
    }
    
    public String forgetPasswordCodeValidate() throws IOException {
        Map<String, String> params1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        forgetPasswordCode = params1.get("code");
        FacesContext context = FacesContext.getCurrentInstance();
        //System.out.println("Code " + parameterOne);
        if (forgetPasswordCode != null) {
           userProfile = adminService.findUserProfilebyForgetPasswordCode(forgetPasswordCode);
           System.out.println(userProfile.getEmail());
           if(userProfile==null){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! Entered Code is incorrect", ""));
                webSession.setUserprofile(null);
            }
           else {
               userProfile.setPassword(null);
               webSession.setUserprofile(userProfile);
           }
        }else {
           context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry!! Please enter code", ""));
           webSession.setUserprofile(null); 
        }
        return "success";
    }
    
    public String userProfile() {
        
        return "userProfile";
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        try {
            context.getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkSession() {
        
        FacesContext cont = FacesContext.getCurrentInstance();
        String email = String.valueOf(cont.getExternalContext().getSessionMap().get("email"));
        System.out.println(email + " test");

        usfile = adminService.getUserFilesByUser(email);

    }

    public List<UserFiles> getUsfile() {
        return usfile;
    }

    public void setUsfile(List<UserFiles> usfile) {
        this.usfile = usfile;
    }

    public String getParameterOne() {
        return parameterOne;
    }

    public void setParameterOne(String parameterOne) {
        this.parameterOne = parameterOne;
    }

    public String getForgetPasswordCode() {
        return forgetPasswordCode;
    }

    public void setForgetPasswordCode(String forgetPasswordCode) {
        this.forgetPasswordCode = forgetPasswordCode;
    }
    
    
    
    public String writeSpreadSheet(UserFiles uf, String barcode, int row, String column, String sheetName) {

        String cellID = sheetName + "!" + column + row;
        System.out.println(cellID);
        GoogleCredential SessionCredential;
        FacesContext cont = FacesContext.getCurrentInstance();
        SessionCredential = (GoogleCredential) cont.getExternalContext().getSessionMap().get("credential");
        System.out.println("session " + SessionCredential);
        Sheets service = new Sheets.Builder(httpTransport, jsonFactory, SessionCredential)
                .setApplicationName("JavaGmailLogin")
                .build();

        try {

            List<List<Object>> li = Arrays.asList(
                    Arrays.asList(barcode
                    )
            );

            ValueRange requestBody = new ValueRange();
            requestBody.setValues(li);
            Sheets.Spreadsheets.Values.Update request = service.spreadsheets().values().update(uf.getFileID(), cellID, requestBody);
            request.setValueInputOption("RAW");
            UpdateValuesResponse resp = request.execute();
            System.out.println(request);
            System.out.println(resp);

        } catch (IOException ex) {
            Logger.getLogger(ViewSupport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "excelSheetsForAccounts.xhtml";
    }

    public void downloadFile(UserFiles uf) {
        //System.out.println(uf.getFileID());
        GoogleCredential SessionCredential;
        FacesContext cont = FacesContext.getCurrentInstance();
        SessionCredential = (GoogleCredential) cont.getExternalContext().getSessionMap().get("credential");

        Drive service = new Drive.Builder(httpTransport, jsonFactory, SessionCredential)
                .setApplicationName("JavaGmailLogin")
                .build();

        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            service.files().export(uf.getFileID(), "text/tab-separated-values")
                    .executeMediaAndDownloadTo(outputStream);

            System.out.println(outputStream);
        } catch (IOException ex) {
            Logger.getLogger(ViewSupport.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}

		return hash.toString();
	}

    

}
