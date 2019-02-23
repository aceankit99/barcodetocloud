/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.filter;

import java.io.IOException; 
import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;  

/**
 *
 * @author Admin
 */
@WebFilter(urlPatterns = "/login.xhtml")
public class SecureFilter implements Filter {
    
    private static long maxAge = 86400 * 30;
    
    public void doFilter(ServletRequest req, ServletResponse resp,  
     FilterChain chain) throws IOException, ServletException {  
           
     HttpServletRequest request = (HttpServletRequest) req;
     HttpServletResponse response = (HttpServletResponse) resp;
     HttpSession session = request.getSession(false);
     
     //System.out.println("session"+session.getAttribute("email"));
     String loginURI = request.getContextPath() + "/login.xhtml";
     
     boolean loggedIn = session != null && session.getAttribute("email") != null;
     //System.out.println("loggedin"+request.getRequestURI());
     boolean loginRequest = request.getRequestURI().equals(loginURI);
     
     boolean signupRequest = request.getRequestURI().equals(request.getContextPath() + "/signUp.xhtml");
     
     boolean forgetpasswordRequest = request.getRequestURI().equals(request.getContextPath() + "/forgetPassword.xhtml");
     
     boolean resetetpasswordRequest = request.getRequestURI().equals(request.getContextPath() + "/resetPassword.xhtml");
     
     boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);
     
     String path = ((HttpServletRequest) request).getRequestURI();
     
     if (path.contains(".js") || path.contains(".css") || path.contains(".svg") || path.contains(".gif")
                || path.contains(".woff") || path.contains(".png")||path.contains(".jpeg")) {
            response.setHeader("Cache-Control", "max-age=" + maxAge);
        }
     
     if(path.contains("/webresources")) {
         //System.out.println("hi");
         chain.doFilter(request, response);  
     }
     else {
            if (loggedIn && (loginRequest || forgetpasswordRequest || signupRequest || resetetpasswordRequest)){
                 //request.getRequestDispatcher("/dashboard.xhtml").forward(request, response);
                 response.sendRedirect(request.getContextPath() + "/dashboard.xhtml");
            }
            else if(!loggedIn && (loginRequest || forgetpasswordRequest || signupRequest || resetetpasswordRequest || resourceRequest ) ){
              //System.out.println(request);
              chain.doFilter(request, response);  
            }

            else if(loggedIn && !(loginRequest || forgetpasswordRequest || signupRequest || resetetpasswordRequest) ){
              chain.doFilter(request, response);  
            }

            else {
                response.sendRedirect(loginURI);
            }
     }
      //chain.doFilter(request, response);  
     }  
 
 @Override
     public void destroy() {}
 
 @Override
 public void init(FilterConfig arg0) throws ServletException {
 // TODO Auto-generated method stub
 
 }  
    
}
