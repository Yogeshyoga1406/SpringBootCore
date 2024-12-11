package com.beautyhub.beautyhub_service;

import com.beautyhub.beautyhub_service.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    final static Logger log = LogManager.getLogger(JWTAuthorizationFilter.class);


    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.signing.key}")
    String jwtSignKey;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
     //   log.info("in doFilterInternal ");
        //log.info("this.tokenHeader :"+ this.tokenHeader);
      //  log.info("req: " + req.getRequestURL());
        try {
            //String header = req.getHeader(this.tokenHeader);
            String header = req.getHeader("Authorization");
          //  log.info("header: " + header);
            if (header == null || !header.startsWith("Bearer")) {
//				log.info("cannot authenticate");
                chain.doFilter(req, res);
                return;
            }
        } catch (NullPointerException e) {
            log.info("NPE... cannot authenticate");
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);



        chain.doFilter(req, res);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
      //  log.info("getAuthentication ");
        String token = req.getHeader("Authorization");

        if (token != null) {
         //  log.info("Signing key =" + jwtTokenUtil.getSigningKey());
            try {
                String user = Jwts.parser().setSigningKey(jwtSignKey).parseClaimsJws(token.replace("Bearer", "")).getBody()
                        .get("username").toString();

                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (ExpiredJwtException e) {
                log.error("Token Expired: " + e);
                return null;
            } catch (MalformedJwtException e) {
                log.error("JWT token is malformed: " + e.getMessage());
                // Optionally, you can include more information about the token, such as the token itself or the request details.
                // log.error("Invalid token: " + token);
                return null;
            } catch (Exception e) {
                log.error("Error parsing JWT token: " + e);
                return null;
            }
        }

        return null;
    }

//    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
//
//        log.info("getAuthentication ");
//        String token = req.getHeader("Authorization");
//
//        if (token != null) {
//            //String user = jwtTokenUtil.getUsernameFromToken(token);
//            log.info("Signing key =" + jwtTokenUtil.getSigningKey());
//            try {
//                String user = Jwts.parser().setSigningKey(jwtTokenUtil.getSigningKey()).parseClaimsJws(token.replace("Bearer", "")).getBody()
//                        //.getSubject()
//                        .get("username").toString();
//
//                if (user != null) {
//                    //log.info("got user: "+user);
//                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
//                }
//            } catch (io.jsonwebtoken.ExpiredJwtException e) {
//                log.error("Token Expired: " + e);
//                return null;
//            }
//            return null;
//        }
//
//        return null;
//
//    }

}
