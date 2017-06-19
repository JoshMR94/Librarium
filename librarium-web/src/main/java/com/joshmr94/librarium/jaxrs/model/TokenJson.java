package com.joshmr94.librarium.jaxrs.model;

import com.joshmr94.librarium.model.Usuario.TipoUsuario;

/**
 *
 * @author joshmr94
 */
public class TokenJson {

    String token;
    Long idUsuario;
    TipoUsuario tipoUsuario;

    public TokenJson() {
    }

    public TokenJson(String token) {
        this.token = token;
    }

    public TokenJson(String token, Long idUsuario) {
        this.token = token;
        this.idUsuario = idUsuario;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
}
