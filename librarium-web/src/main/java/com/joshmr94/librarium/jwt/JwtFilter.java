package com.joshmr94.librarium.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author joshmr94
 */

public class JwtFilter implements Filter {
    public static final String JWTNAME = "jwt.name";
    public static final String JWTUSERNAME = "jwt.username";
    public static final String JWTUSERROLES = "jwt.userroles";

    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    FilterConfig filterConfig = null;
    
    JwtConfiguration config = new JwtConfiguration();    
    JwtHelper helper = new JwtHelper();
    
    public JwtFilter() {
    }    

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        if (!"OPTIONS".equals(req.getMethod())) {

            String pathExclusions = config.getProperty("jwt.path-exclusions");
            if (pathExclusions != null) {
                String[] paths = pathExclusions.split(",");
                HashSet<String> exclusions = new HashSet<>();
                Arrays.asList(paths).stream().forEach(path -> exclusions.add(path.trim()));
                String uri = req.getPathInfo();
                for (String prefix : exclusions) {
                    if (uri.startsWith(prefix)) {
                        chain.doFilter(request, response);
                        return;
                    }
                }
            }

            String header = config.getProperty("jwt.header");
            String token = req.getHeader(header);
            Map<String, Object> claims = helper.verifyToken(token);
            if (claims == null) {
                HttpServletResponse resp = (HttpServletResponse)response;
                resp.sendError(401);
            } else {
                req.setAttribute(JWTUSERNAME, claims.get("sub"));
                req.setAttribute(JWTNAME, claims.get("name"));
                req.setAttribute(JWTUSERROLES, claims.get("rol"));
                chain.doFilter(request, response);
            }        
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
        this.filterConfig = filterConfig;
    }
    
}
