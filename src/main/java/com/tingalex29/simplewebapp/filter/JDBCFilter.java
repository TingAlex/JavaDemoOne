package com.tingalex29.simplewebapp.filter;

import com.tingalex29.simplewebapp.conn.ConnectionUtils;
import com.tingalex29.simplewebapp.utils.MyUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator on 11/19/2016.
 */
@WebFilter(filterName = "jdbcFilter",urlPatterns = {"/*"})
public class JDBCFilter implements Filter {
    public void destroy() {
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request= (HttpServletRequest) req;


        //
        // Only open connections for the special requests need
        // connection. (For example, the path to the servlet, JSP, ..)
        //
        // Avoid open connection for commons request
        // (for example: image, css, javascript,... )
        //
        if (this.needJDBC(request)) {

            System.out.println("Open Connection for: " + request.getServletPath());

            Connection conn = null;
            try {
                // Create connection
                conn = ConnectionUtils.getConnection();

                // Set Auto commit to false
                conn.setAutoCommit(false);

                // Store connection in attribute of request.
                MyUtils.storeConnection(request, conn);

                // Allow request to go forward
                // (Go to the next filter or target)
                chain.doFilter(request, resp);

                // Commit change.
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
                ConnectionUtils.rollbackQuietly(conn);
                throw new ServletException();
            } finally {
                ConnectionUtils.closeQuietly(conn);
            }
        }

        // With commons requests (images, css, html, ..)
        // No need to open the connection.
        else {

            // Allow request to go forward
            // (Go to the next filter or target)
        chain.doFilter(req, resp);}
    }
    public void init(FilterConfig config) throws ServletException {

    }
    // Check the target of the request is a servlet?
    private boolean needJDBC(HttpServletRequest request) {
        System.out.println("JDBC Filter");
        //
        // Servlet Url-pattern: /spath/*
        //
        // => /spath
        String servletPath = request.getServletPath();
        // => /abc/mnp
        String pathInfo = request.getPathInfo();

        String urlPattern = servletPath;

        if (pathInfo != null) {
            // => /spath/*
            urlPattern = servletPath + "/*";
        }

        // Key: servletName.
        // Value: ServletRegistration
        Map<String, ? extends ServletRegistration> servletRegistrations = request.getServletContext()
                .getServletRegistrations();


        // Collection of all servlet in your webapp.
        Collection<? extends ServletRegistration> values = servletRegistrations.values();
        for (ServletRegistration sr : values) {
            Collection<String> mappings = sr.getMappings();
            if (mappings.contains(urlPattern)) {
                return true;
            }
        }
        return false;
    }

}
