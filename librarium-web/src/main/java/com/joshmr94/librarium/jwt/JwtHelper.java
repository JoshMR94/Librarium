package com.joshmr94.librarium.jwt;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.joshmr94.librarium.model.Usuario;
import com.joshmr94.librarium.comun.service.CommonSession;
import com.joshmr94.librarium.comun.service.UsuarioSession;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author joshmr94
 */
public class JwtHelper {

    protected static final Logger log = Logger.getLogger(JwtHelper.class);

    JwtConfiguration config;

    public JwtHelper() {
        config = new JwtConfiguration();
    }

    public JwtHelper(JwtConfiguration helper) {
        this.config = helper;
    }

    public String buildToken(String username, String pwd) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("JWT: token requested for %s", username));
        }
        Map<String, Object> claims = initClaimsMap(username, pwd);
        if (claims == null) {
            return null;
        }
        claims.put("iss", config.getProperty("jwt.issuer"));
        claims.put("aud", config.getProperty("jwt.audience"));
        long currentSeconds = System.currentTimeMillis() / 1000l;
        claims.put("iat", currentSeconds);
        claims.put("exp", currentSeconds + Integer.parseInt(config.getProperty("jwt.duration")));

        byte[] secret = Base64.getDecoder().decode(config.getProperty("jwt.b64secret"));
        JWTSigner signer = new JWTSigner(secret);

        String token = signer.sign(claims, new JWTSigner.Options().setAlgorithm(Algorithm.HS256));
        if (log.isDebugEnabled()) {
            log.debug(String.format("JWT: token created for %s: %s", username, token));
        }
        return token;
    }

    protected Map<String, Object> initClaimsMap(String username, String pwd) {
        //roles is a lazy collection, so we need an active connection to obtain its values
        EntityManager em = CommonSession.buildEntityManager();
        try {
            UsuarioSession usession = getUsuarioSession(em);
            Usuario usuario = usession.getUserWithCredentials(username, pwd);
            if (usuario == null) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("JWT: invalid credentials for %s", username));
                }
                return null;
            }
            Set<String> roles = new HashSet<>();
            usuario.getRoles().stream().forEach(r -> roles.add(r.getDescripcion()));
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("sub", usuario.getUsername());
            claims.put("name", usuario.getDescripcion());
            claims.put("rol", roles);
            return claims;
        } finally {
            em.close();
        }
    }

    UsuarioSession getUsuarioSession(EntityManager em) {
        return new UsuarioSession(em);
    }

    public Map<String, Object> verifyToken(String token) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("JWT: verifying token %s", token));
        }
        byte[] secret = Base64.getDecoder().decode(config.getProperty("jwt.b64secret"));
        JWTVerifier verifier = new JWTVerifier(
                secret,
                config.getProperty("jwt.audience"),
                config.getProperty("jwt.issuer"));
        Map<String, Object> claims = null;
        try {
            claims = verifier.verify(token);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("JWT: invalid token %s", token), e);
            }
            return null;
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("JWT: valid token verified: %s", token));
        }
        return claims;
    }

}
