package nguye.cardatabase.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // If the Authorization header is null, it means that the
        if (authHeader != null) {
            // Extract the username from the JWT carried in the Authorization header of the incoming request.
            // If the token is valid, store the info about the authenticated user to the security context.
            String user = jwtService.verifyAndGetUser(authHeader);
            // The authorities are used here as how roles are often used
            // The naming convention is also non-standard, as the authority is derived from the username without any prefix
            GrantedAuthority authority = new SimpleGrantedAuthority(user.toUpperCase());

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
