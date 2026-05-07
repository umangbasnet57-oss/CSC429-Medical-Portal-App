package edu.secourse.patientportal.security;

import edu.secourse.patientportal.util.Constants;
import edu.secourse.patientportal.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration for the patient portal application.
 *
 * <p>This class defines the application's security behavior, including:
 * <ul>
 *     <li>Password encoding</li>
 *     <li>JWT authentication support</li>
 *     <li>Endpoint authorization rules</li>
 *     <li>Stateless session management</li>
 *     <li>Method-level security using {@code @PreAuthorize}</li>
 * </ul>
 *
 * <p><b>Security Model:</b>
 * <ul>
 *     <li>JWT tokens are used for authentication</li>
 *     <li>Sessions are disabled using stateless session management</li>
 *     <li>Protected endpoints require authentication and/or roles</li>
 *     <li>Public endpoints are explicitly permitted</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /** Custom filter used to authenticate requests containing JWT tokens. */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructs the security configuration with the JWT authentication filter.
     *
     * @param jwtAuthenticationFilter custom JWT filter used in the security chain
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Provides the password encoder used for hashing user passwords.
     *
     * <p>{@link BCryptPasswordEncoder} is used because it is a strong,
     * adaptive hashing algorithm suitable for storing passwords securely.
     *
     * @return configured password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures conversion of JWT claims into Spring Security authorities.
     *
     * <p>This converter reads user roles from the {@code roles} claim and
     * prefixes them with {@code ROLE_}, which is the format expected by
     * Spring Security role checks such as {@code hasRole("ADMIN")}.
     *
     * @return configured JWT authentication converter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtConverter;
    }

    /**
     * Defines the HTTP security filter chain for the application.
     *
     * <p>This method configures:
     * <ul>
     *     <li>CSRF protection disabled for stateless REST API usage</li>
     *     <li>Role-based authorization for protected endpoints</li>
     *     <li>Public access for configured API routes</li>
     *     <li>Stateless session management</li>
     *     <li>JWT filter placement before username/password authentication</li>
     * </ul>
     *
     * @param http Spring Security HTTP configuration object
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * CSRF is disabled because this is a stateless REST API
                 * using JWT tokens instead of server-side sessions.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * Configure route-level authorization rules.
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Constants.USERS_ENDPOINT + "/create").hasRole("ADMIN")
                        .requestMatchers(Constants.API_VERSION + "/**").permitAll()
                        .anyRequest().authenticated()
                )

                /*
                 * Disable server-side session storage.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /*
                 * Add JWT authentication filter before Spring Security's
                 * default username/password authentication filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}