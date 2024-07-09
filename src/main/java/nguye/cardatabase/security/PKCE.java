package nguye.cardatabase.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Getter
public class PKCE {

    private final String verifier;
    private final String challenge;

    public PKCE() {
        this.verifier = generateVerifier();
        this.challenge = generateChallenge();
    }

    private String generateVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(code);
    }

    private String generateChallenge() throws RuntimeException {
        if (verifier == null) throw new RuntimeException("Code verifier is not defined");

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digested = messageDigest.digest(verifier.getBytes());
            return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(digested);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
