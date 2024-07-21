package nguye.cardatabase.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nguye.cardatabase.util.VaultClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {

    static final long EXPIRATION_TIME = 10800000;

    private final Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(getSecretKey()));

    public JwtService() throws Exception {}

    // Get the JWT secret key from HCP Vault Secrets using API
    private String getSecretKey() throws Exception {

        String vaultToken = VaultClient.getToken();
        String resBodyString = VaultClient.makeApiGetRequest("jwt_key", vaultToken);
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(resBodyString);
            JsonNode valueNode = rootNode.path("secret").path("version").path("value");
            String key = valueNode.asText();
            System.out.println("Value: " + key);
            return key;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Generate a signed JWT token
    public String generateToken(String username)  {

        return JWT.create()
                .withIssuer("cardb")
                .withSubject(username)
                .withClaim("des", "user_cred")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Get a token from the request, verify the token, and get the username
    public String verifyAndGetUser(String header) {

        String token = header.substring(7);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("cardb")
                    .withClaim("des", "user_cred")
                    .build();

            DecodedJWT decoded = verifier.verify(token);
            return decoded.getSubject();

        } catch (JWTVerificationException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
        }
        return null;
    }
}
