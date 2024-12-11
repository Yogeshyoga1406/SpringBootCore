package com.beautyhub.beautyhub_service.jwt;

import com.coreerp.model.User;
import com.coreerp.multitenancy.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    final static Logger log = LogManager.getLogger(JwtTokenUtil.class);

    @Value("${jwt.signing.key}")
    private String signingKey;
    //private final AtomicBoolean initialized = new AtomicBoolean(false);
    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${jwt.expiration}")
    private Long expiryMilliseconds;
// to generate the dynamic secret key
    // it cause problem when it is deployed when the we have the 2 task
//    @PostConstruct
//    public void initialize() {
//        if (!initialized.getAndSet(true)) {
//            generateSigningKey(); // Generate a secure signing key only if not already initialized
//        }
//    }

    public String generate(User jwtUser, Date subscriptionEndDate) {
        log.info("this.signingKey =" + this.signingKey);
        Claims claims = Jwts.claims().setSubject(jwtUser.getUsername());
        claims.put("username", String.valueOf(jwtUser.getUsername()));
//        claims.put("role", jwtUser.getAuthorities());
        claims.put("role", jwtUser.getRoles());
        claims.put("tenantId", TenantContext.getCurrentTenant());
        claims.put("subscriptionEndDate", subscriptionEndDate);
        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + expiryMilliseconds)).
                signWith(SignatureAlgorithm.HS512, this.signingKey).compact();


    }

    public String getTenantIdFromToken(String token) {
        if (token != null) {
            try {
                // Parse the token with the signing key
                String user = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token.replace(this.tokenPrefix, "")).getBody().get("tenantId").toString();
                return user;
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.error("Token Expired: " + e);
                return null;
            }
        }


        return null;
    }

    public Date getSubscriptionEndDateFromToken(String token) {
        if (token != null) {

            try {
                // parse the token.
                String subscriptionEndDate = Jwts.parser().setSigningKey(this.signingKey).parseClaimsJws(token.replace(this.tokenPrefix, "")).getBody()
                        // .getSubject()
                        .get("subscriptionEndDate").toString();
//				log.info("subscriptionEndDate: " + subscriptionEndDate);
                if (subscriptionEndDate != null) {
                    return new Date(Long.parseLong(subscriptionEndDate));
                } else {
                    return null;
                }

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.error("Token Expired: " + e);
                return null;
            } catch (NullPointerException npe) {
                log.error("subscriptionEndDate not available: " + npe);
                return null;
            }
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        if (token != null) {

            try {
                // parse the token.
                String user = Jwts.parser().setSigningKey(this.signingKey).parseClaimsJws(token.replace(this.tokenPrefix, "")).getBody()
                        // .getSubject()
                        .get("username").toString();

                return user;

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.error("Token Expired: " + e);
                return null;
            }
        }
        return null;
    }

    // dynamically generating the key
//    public void generateSigningKey() {
//        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//        printSigningKey();
//
//    }

//    public SecretKey getSigningKey() {
//        return this.signingKey;
//    }
//    public void printSigningKey() {
//        if (this.signingKey != null) {
//            byte[] encodedKey = this.signingKey.getEncoded();
//            String encodedKeyString = Base64.getEncoder().encodeToString(encodedKey);
//            log.info("Signing Key: " + encodedKeyString);
//        } else {
//            log.warn("Signing Key is null.");
//        }
//    }
}
