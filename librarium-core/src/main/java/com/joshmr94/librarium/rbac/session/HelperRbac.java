package com.joshmr94.librarium.rbac.session;

import java.util.Stack;

/**
 *
 * @author joshmr94
 */
public class HelperRbac {

    private HelperRbac() {
    }
    
    public static String imprimeCiclo(Stack<String> stack, String rol) {
        if (stack == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (String s : stack) {
            sb.append("/");
            sb.append(s);
        }
        sb.append("/");
        sb.append(rol);
        return sb.toString();
    }
    
}
