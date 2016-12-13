package com.tingalex29.simplewebapp.servlet;

import com.tingalex29.simplewebapp.beans.UserAccount;
import com.tingalex29.simplewebapp.conn.ConnectionUtils;
import com.tingalex29.simplewebapp.utils.DBUtils;
import com.tingalex29.simplewebapp.utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Created by Administrator on 11/25/2016.
 */
@WebServlet(name = "doSignUpServlet",urlPatterns = {"/doSignUp"})
public class doSignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String rememberMeStr = request.getParameter("rememberMe");
        boolean remember= "Y".equals(rememberMeStr);
        UserAccount user = null;
        boolean hasError = false;
        String errorString = null;

        if (userName == null || password == null
                || userName.length() == 0 || password.length() == 0) {
            hasError = true;
            errorString = "Required username and password!";
        } else {
            try {
                Connection conn=ConnectionUtils.getConnection();
                DBUtils.createUser(conn, userName ,gender,password);
            }catch (ClassNotFoundException e){
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            }catch (SQLException e) {
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            }
            user = new UserAccount();
            user.setUserName(userName);
            user.setPassword(password);
            user.setGender(gender);
        }
        // If error, forward to /WEB-INF/views/login.jsp
        if (hasError) {

            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user);


            // Forward to /WEB-INF/views/login.jsp
            RequestDispatcher dispatcher //
                    = this.getServletContext().getRequestDispatcher("/WEB-INF/views/signUpView.jsp");

            dispatcher.forward(request, response);
        }
        else {
            HttpSession session = request.getSession();
            MyUtils.storeLoginedUser(session, user);

            // If user checked "Remember me".
            if(remember)  {
                MyUtils.storeUserCookie(response,user);
            }

            // Else delete cookie.
            else  {
                MyUtils.deleteUserCookie(response);
            }

            response.sendRedirect(request.getContextPath()+"/userInfo");
        }

        // If no error
        // Store user information in Session
        // And redirect to userInfo page.
}}
