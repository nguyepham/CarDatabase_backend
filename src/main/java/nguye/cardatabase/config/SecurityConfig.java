package nguye.cardatabase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// These two annotations switch off the default web security configuration, and we can define our own configuration
// in this class.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // This method creates a PasswordEncoder bean that is used to encode the password.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // This method creates an InMemoryUserDetailsManager bean that is used to define a user with the username user and
    // password secret. The password is encoded with the passwordEncoder bean.
    // The InMemoryUserDetailsManager bean is used to store the user details in memory.
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("secret"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
}
