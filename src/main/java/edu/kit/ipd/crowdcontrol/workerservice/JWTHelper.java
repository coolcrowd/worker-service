package edu.kit.ipd.crowdcontrol.workerservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author LeanderK
 * @version 1.0
 */
public class JWTHelper {
    public String generateJWT(int workerId) {
        return Jwts.builder()
                .setSubject(String.valueOf(workerId))
                .signWith(SignatureAlgorithm.ES512, "a")
                .compact();
    }
}
