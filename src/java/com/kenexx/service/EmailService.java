/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.service;

import com.kenexx.model.UserProfile;
import com.kenexx.service.AdminService;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

/**
 *
 * @author Admin
 */
@Stateless
public class EmailService {
    
    @Inject
    private AdminService adminService;
    
    @Resource(name = "mail/BarcodeMailSession")
    private Session mailSession;
    private final String sender = "Barcode To Cloud Administrator";
    private final String fromAddress = "noreply@constacloud.com";
    private boolean production = false;
    
    public void testEmailService() throws NamingException, AddressException {
        Message message = new MimeMessage(mailSession);
        InternetAddress recipientsReviewersList = new InternetAddress("animesh27051982@gmail.com");
        try {
            message.setSubject("IMPORTANT: Monthly JE Report for Review - Testing" );
            message.setFrom(new InternetAddress(fromAddress, sender));
            message.setRecipient(Message.RecipientType.TO, recipientsReviewersList);
            message.setContent("<html><body><b>Animesh</body></html>","text/html");
            //message.setText("IMPORTANT: RCS Action Required -  JE Report has been submitted for your review; please open the link to review your required action.  https://flowserve.enspir.net/rcs");
            Transport.send(message);
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, "Message Sent SuccessFully..please check your mail");

        } catch (MessagingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, "Oops! Got an Exception");
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, ex.toString());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendForgetEmailService(UserProfile up) throws NamingException, AddressException {
        Message message = new MimeMessage(mailSession);
        InternetAddress recipientsReviewersList = new InternetAddress(up.getEmail());
        try {
            
            String forgetPasswordURI = "http://barcode.constacloud.com:8080/kenexx/resetPassword.xhtml?code="+up.getForgetPasswordCode();
            message.setSubject("Barcode To Cloud Forget Password Request");
            message.setFrom(new InternetAddress(fromAddress, sender));
            message.setRecipient(Message.RecipientType.TO, recipientsReviewersList);
            message.setContent("<html><body><html><body>Dear "+ up.getUserName()+",<br>As per your request for forgetting password, below is the link for restting new password,please click on the link given below <br><a href='"+forgetPasswordURI+"'>click here to reset your password</a><br><br>Thanks<br><br>Suppport Team<br>Barcode To Cloud</body></html>","text/html");
            //message.setText("IMPORTANT: RCS Action Required -  JE Report has been submitted for your review; please open the link to review your required action.  https://flowserve.enspir.net/rcs");
            Transport.send(message);
            adminService.updateUserProfile(up);
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, "Message Sent SuccessFully..please check your mail");

        } catch (MessagingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, "Oops! Got an Exception");
            Logger.getLogger(EmailService.class.getName()).log(Level.INFO, ex.toString());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
