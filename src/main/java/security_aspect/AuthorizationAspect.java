package security_aspect;

import security_aspect.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import dto.Response;
import java.text.ParseException;
import java.util.Date;
import javax.crypto.SecretKey;
import net.minidev.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    
    @Around("controller() && execution(public * com.mycompany.restfulspring.*.*Authorized(..))")
    public Response authorize(ProceedingJoinPoint joinPoint) {

        Response response = new Response();
        response.setError("1");
        response.setResponseData(null);
        response.setStatus("failed");

        System.out.println("Authorize at " + System.currentTimeMillis());
        Object[] args = joinPoint.getArgs();
        
       // for(Object arg : args)
        //    System.out.println((String)arg);
        
        if (args != null) {
            if (args[0] != null) {

                try {
                    String accessToken = (String) args[0];
                                        System.out.println("token " + accessToken);

                    JSONObject jSONObject = verifyToken(accessToken);
                    
                    //replace the first parameter(token) with user id
                    args[0] = (String)jSONObject.get("id");
                    System.out.println("responce to be called");
                    return (Response) joinPoint.proceed(args);

                } catch (ParseException ex) {

                    System.out.println("parse exception");
                    ex.printStackTrace();
                } catch (JOSEException ex) {
                    System.out.println("jose exception");

                    if (ex.getMessage().equals("expiredToken")) {
                        System.out.println("jose time exception");
                        response.setError("2");

                    }
                    ex.printStackTrace();
                } catch (Throwable ex) {

                    System.out.println("throwable exception");
                    ex.printStackTrace();
                }

            }
        }

        System.out.println("returning " + response.toString());

        return response;
    }

    public JSONObject verifyToken(String accessToken) throws JOSEException, ParseException {
        SecretKey secretKey = SecurityKeyInstance.getEncryptionKey();
        SignedJWT signedJWT = SignedJWT.parse(accessToken);

        JWSVerifier verifier = new MACVerifier(secretKey);
        
        if(!signedJWT.verify(verifier)){
            throw new ParseException("invalidToken", 0);
        }
     
        if (signedJWT.getJWTClaimsSet().getExpirationTime().
                compareTo(new Date(System.currentTimeMillis())) <= 0) { //expired token

            throw new JOSEException("expiredToken");
        }

        return signedJWT.getPayload().toJSONObject();

    }

    /*
    @Around("controller() && execution(* *.*Authorized(..))")
    public Response authorize(ProceedingJoinPoint joinPoint) {

        Response response = new Response();
        response.setError("1");
        response.setResponseData(null);
        response.setStatus("failed");

        System.out.println("Authorize at " + System.currentTimeMillis());
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            if (args[0] != null) {
                String token = (String) args[0];

                try {
                    System.out.println("decryption");
                    JSONObject jSONObject = decryptToken(token);
                    System.out.println("after decryption");

                    //replace the token with user id
                    args[0] = jSONObject.get("id");
                    System.out.println("responce to be called");
                    return (Response) joinPoint.proceed(args);

                } catch (ParseException ex) {

                    System.out.println("parse exception");

                } catch (JOSEException ex) {
                    System.out.println("jose exception");

                    if (ex.getMessage().equals("expiredToken")) {
                        System.out.println("jose time exception");
                        response.setError("2");

                    }
                } catch (Throwable ex) {

                    System.out.println("throwable exception");

                }

            }
        }

        System.out.println("returning " + response.toString());

        return response;
    }
     */
 /*
    public JSONObject decryptToken(String token)
            throws ParseException,
            JOSEException {

        System.out.println("decrypt token  has been called");
        // Serialise to JWE compact form
        // Parse the JWE string
        SecretKey secretKey = SecurityKeyInstance.getEncryptionKey();
        JWEObject jweObject = JWEObject.parse(token);

        // Decrypt with shared key
        byte[] encodedSecret = secretKey.getEncoded();

        DirectDecrypter directDecrypter
                = new DirectDecrypter(encodedSecret);

        jweObject.decrypt(directDecrypter);

        // Extract payload
        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

        JSONObject jSONObject = signedJWT.getPayload().toJSONObject();
        if (signedJWT.getJWTClaimsSet().getExpirationTime().
                compareTo(new Date(System.currentTimeMillis())) <= 0) //expired token
        {
            System.out.println(" date error");
            throw new JOSEException("expiredToken");
        }

        return jSONObject;

    }*/
}
