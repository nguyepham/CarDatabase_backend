package nguye.cardatabase.repository;

import nguye.cardatabase.model.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

// We set the exported attribute to `false` to prevent the AppUserRepository from being exported as a REST endpoint
@RepositoryRestResource(exported = false)
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    // Find a user from the database in the authentication process by username
    Optional<AppUser> findByUsername(String username);
}
