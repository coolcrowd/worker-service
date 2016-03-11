package edu.kit.ipd.crowdcontrol.workerservice;

import io.jsonwebtoken.SignatureException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public class JWTHelperTest {
    private JWTHelper jwtHelper = new JWTHelper("VHlwZSAob3IgcGFzdGUpIGhlcmUuLi4=");

    @Test
    public void testGenerateJWT() throws Exception {
        String jwt = jwtHelper.generateJWT(13);
        assertEquals(jwt, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMyJ9.8hs07EsZJLZAjU8moUWJqPQjT3Yqw8ACNLfow7VsEqo");
    }

    @Test
    public void testGetWorkerID() throws Exception {
        String jwt = jwtHelper.generateJWT(13);
        int workerID = jwtHelper.getWorkerID(jwt);
        System.out.println(workerID);
    }

    @Test(expected = SignatureException.class)
    public void testInvalidSignature() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMyJ9.V57dTeOPAJWp8nJaso9XIDQV1XxFoE64WktVoWQ4z4o";
        jwtHelper.getWorkerID(jwt);
    }
}