package nguye.cardatabase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    // IMPORTANT: The username here must be UNIQUE.
    // That's because here we're using the most basic authentication mechanism offered by Spring Security - the
    // password authentication mechanism.
    // We implement this mechanism in our application by adding to the Spring context a bean that implements the
    // `UserDetailsService` interface.
    // We can easily see the basic idea behind this mechanism by observing that the `UserDetailsService` interface
    // has only one method: loadUserByUsername.
    // The authentication provider (AP) of Spring Security uses the username to determine if a user existed in the
    // database. If it does, the AP then checks if the password provided by the client matched the password of that
    // user (which stored in the database). Therefore, the username must be UNIQUE.
    // Inside the overriding method of loadUserByUsername, we must use
    // `Optional<AppUser> user = repository.findByUsername(username);' to get the user from the database (using ORM).
    @Column(nullable = false, unique = true)
    private String username;

    // The password column MUST NOT be unique because multiple users can have the same password, and we never want
    // any user knows that a password has already used by someone else.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    public AppUser(String username, String password, String role) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
