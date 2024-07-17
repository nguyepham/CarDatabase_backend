package nguye.cardatabase.security;

import nguye.cardatabase.domain.AppUser;
import nguye.cardatabase.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository repository;

    public UserDetailsServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }

    // This method is called by the DaoAuthenticationProvider, which is the default authentication provider in Spring
    // Security.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> user = repository.findByUsername(username);
        User.UserBuilder builder;
 
        if (user.isPresent()) {
            // Create a UserDetails object from the data in the AppUser object
            // The UserDetails object is used by an AuthenticationProvider to authenticate the user
            AppUser currentUser = user.get();
            // Create the UserDetails object
            builder = User
                .withUsername(username)
                .password(currentUser.getPassword());
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        return builder.build();
    }
}
