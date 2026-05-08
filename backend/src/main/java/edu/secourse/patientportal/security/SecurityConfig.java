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
import org.springframework.http.HttpMethod;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public  SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");  // match your claim name

        converter.setAuthorityPrefix("ROLE_");        // optional, Spring expects ROLE_ prefix
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .addFilterBefore()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Constants.USERS_ENDPOINT + "/create").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/maclogixapi/v1/appointments/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/maclogixapi/v1/appointments/*/cancel")
                        .hasAnyRole("ADMIN", "PATIENT")

                        .requestMatchers(HttpMethod.PUT, "/maclogixapi/v1/appointments/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/maclogixapi/v1/appointments/*/toggle-status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/maclogixapi/v1/users/*")
                        .hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(Constants.API_VERSION + "/auth/**").permitAll()

                        .anyRequest().authenticated()
                )
                // CRITICAL: Add your JWT filter before the standard authentication filter
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
