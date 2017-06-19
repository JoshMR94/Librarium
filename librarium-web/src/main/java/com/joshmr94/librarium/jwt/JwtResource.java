package com.joshmr94.librarium.jwt;

import com.joshmr94.librarium.model.Usuario;
import com.joshmr94.librarium.comun.service.UsuarioSession;
import com.joshmr94.librarium.jaxrs.model.JwtRequest;
import com.joshmr94.librarium.jaxrs.model.TokenJson;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

/**
 *
 * @author joshmr94
 */
@Path("/token")
public class JwtResource {

    protected static final Logger log = Logger.getLogger(JwtResource.class);
    JwtHelper helper = new JwtHelper();

    @GET
    @Path("/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getByName(@PathParam("token") String token) {
        Map<String, Object> claims = helper.verifyToken(token);
        if (claims == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                    .build();
        } else {
            return Response.ok(claims.get("sub"))
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createToken(JwtRequest request, @Context UriInfo uriInfo) throws NoSuchAlgorithmException {
        try {
            String token = helper.buildToken(request.getUsername(), request.getPassword());
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } else {
                UsuarioSession service = new UsuarioSession();
                //encriptar password para comparar en bbdd
                String passwordEncoded = JwtResource.encryptAndEncodePassword(request.getPassword());
                Usuario usuarioRequest = service.getByUsernameAndPassword(request.getUsername(), passwordEncoded);
                if( usuarioRequest.getBaja() ){
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                } else {
                    TokenJson tokenJson = new TokenJson();
                    service.save(usuarioRequest, null);
                    tokenJson.setToken(token);
                    tokenJson.setIdUsuario(usuarioRequest.getId());
                    tokenJson.setTipoUsuario(usuarioRequest.getTipoUsuario());
                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    return Response.created(uriBuilder.build()).entity(tokenJson).build();
                }  
            }
        } catch (NoResultException e) {
            log.error("Error al crear el token", e);
            return Response.noContent().build();
        }
    }

    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAdmin(JwtRequest request, @Context UriInfo uriInfo) {
        try {
            String token = helper.buildToken(request.getUsername(), request.getPassword());
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } else {
                UsuarioSession service = new UsuarioSession();
                Usuario usuarioRequest = service.getByUsername(request.getUsername());
                if (usuarioRequest.getUsername().contentEquals("admin")) {
                    service.save(usuarioRequest, null);
                    TokenJson tokenJson = new TokenJson();
                    tokenJson.setToken(token);
                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    return Response.created(uriBuilder.build()).entity(tokenJson)
                           .header("Access-Control-Allow-Origin", "*")
                           .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                           .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                           .build();
                } else {
                    return Response.noContent()
                            .header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                            .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                            .build();
                }
            }

        } catch (NoResultException e) {
            log.error("Error al comprobar el token", e);
            return Response.noContent()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                    .build();
        }
    }
    
    public static String encryptAndEncodePassword(String pwd) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(pwd.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
