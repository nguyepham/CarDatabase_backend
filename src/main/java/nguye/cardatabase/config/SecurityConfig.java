package nguye.cardatabase.config;

import nguye.cardatabase.security.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

// These two annotations switch off the default web security configuration, and we can define our own configuration
// in this class.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFilter authFilter;
    private final AuthenticationEntryPoint exceptionHandler;

    public SecurityConfig(AuthenticationFilter authFilter, AuthenticationEntryPoint exceptionHandler) {
        this.authFilter = authFilter;
        this.exceptionHandler = exceptionHandler;
    }

    // This method creates a PasswordEncoder bean that is used to encode the password.
    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In order to use the information stored in the database for the basic authentication mechanism offered by
    // Spring Security, we just NEED two beans in the application context: one must implement the
    // `UserDetailsService` interface and the other is a PasswordEncoder.
    // We need not explicitly define the configure method.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();

                        config.setAllowedOrigins(List.of("*"));
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                        config.setAllowedHeaders(List.of("*"));

                        return config;
                    };
                    configurer.configurationSource(source);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/**").hasAuthority("ADMIN") // ADMIN authority for all other requests
                        .anyRequest().authenticated())
                // All requests other than login will not carry the user credential in the request body and this filter
                // will perform some operations before delegates them to the next filter in the chain.
                // Therefore, it must be added to the filter chain before the filter that carries out the authentication
                // process.
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exHandler -> exHandler.authenticationEntryPoint(exceptionHandler))
                .build();
    }
}
