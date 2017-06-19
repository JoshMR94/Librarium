package com.joshmr94.librarium.jwt;

import com.joshrm94.librarium.comun.helper.Helper;

/**
 *
 * @author joshmr94
 */
public class JwtConfiguration {

    public String getProperty(String name) {
        return Helper.getProperty("jwt.properties", name);
    }
    
}