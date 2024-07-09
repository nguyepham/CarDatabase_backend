package nguye.cardatabase.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// These two annotations switch off the default web security configuration, and we can define our own configuration
// in this class.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // This method creates a PasswordEncoder bean that is used to encode the password.
    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In order to use the information stored in the database for the basic authentication mechanism offered by
    // Spring Security, we just NEED two beans in the application context: one must implement the
    // `UserDetailsService` interface and the other is a PasswordEncoder.
    // We need not explicitly define the configure method.
}
