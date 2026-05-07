package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for loading user-specific data for authentication.
 *
 * <p>This class implements Spring Security's {@link UserDetailsService}
 * and is used during authentication to retrieve user credentials and authorities
 * from the database.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *     <li>Retrieve user by username from {@link UserRepository}</li>
 *     <li>Convert application {@link User} into Spring Security {@link UserDetails}</li>
 *     <li>Assign roles as granted authorities for authorization checks</li>
 * </ul>
 *
 * <p><b>Security Behavior:</b>
 * <ul>
 *     <li>Throws {@link UsernameNotFoundException} if user does not exist</li>
 *     <li>Prefixes roles with {@code ROLE_} to match Spring Security expectations</li>
 *     <li>Provides hashed password for authentication comparison</li>
 * </ul>
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    /** Repository used to retrieve user data from persistence layer. */
    private final UserRepository userRepository;

    /**
     * Constructs the JwtUserDetailsService.
     *
     * @param userRepository repository used to fetch user information
     */
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username for authentication.
     *
     * <p>This method is called by Spring Security during the authentication process.
     * It retrieves the user from the database and converts it into a
     * {@link UserDetails} object that contains credentials and authorities.
     *
     * @param username the username identifying the user
     * @return {@link UserDetails} containing authentication data
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole();

        /*
         * Convert application User to Spring Security UserDetails.
         * Roles must be prefixed with "ROLE_" for compatibility with
         * methods like hasRole("ADMIN").
         */
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_" + role)
        );
    }
}