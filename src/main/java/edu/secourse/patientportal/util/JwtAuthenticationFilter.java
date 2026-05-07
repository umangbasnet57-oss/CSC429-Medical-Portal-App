package edu.secourse.patientportal.util;

import edu.secourse.patientportal.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Security filter responsible for authenticating requests that contain JWT tokens.
 *
 * <p>This filter runs once per request and checks the {@code Authorization}
 * header for a valid Bearer token. If a valid token is found, the filter loads
 * the corresponding user and stores the authentication object in the
 * {@link SecurityContextHolder}.
 *
 * <p><b>Expected Header Format:</b>
 * <pre>
 * Authorization: Bearer &lt;jwt-token&gt;
 * </pre>
 *
 * <p><b>Authentication Flow:</b>
 * <ol>
 *     <li>Read the Authorization header</li>
 *     <li>Verify that it contains a Bearer token</li>
 *     <li>Extract the username from the token</li>
 *     <li>Load user details from {@link UserDetailsService}</li>
 *     <li>Validate the token using {@link TokenService}</li>
 *     <li>Set authentication in Spring Security context</li>
 * </ol>
 *
 * <p>If the request does not contain a Bearer token, the filter simply passes
 * the request to the next filter without authentication.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Service used to extract and validate JWT token information. */
    private final TokenService tokenService;

    /** Service used to load user details from the application user store. */
    private final UserDetailsService userDetailsService;

    /**
     * Constructs the JWT authentication filter.
     *
     * @param tokenService service used for JWT validation and username extraction
     * @param userDetailsService service used to load authenticated user details
     */
    public JwtAuthenticationFilter(TokenService tokenService,
                                   UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters each HTTP request and authenticates the user if a valid JWT is present.
     *
     * <p>If the request contains a valid Bearer token, this method extracts the
     * username, loads the user details, validates the token, and sets the
     * authenticated user in the Spring Security context.
     *
     * @param request incoming HTTP request
     * @param response outgoing HTTP response
     * @param filterChain remaining filters in the security chain
     * @throws ServletException if the filter chain fails during request processing
     * @throws IOException if an input/output error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        /*
         * Continue without authentication if the request does not contain
         * a Bearer token.
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = tokenService.extractUsername(jwt);

        /*
         * Only authenticate if:
         * 1. A username was extracted from the token
         * 2. The current request has not already been authenticated
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenService.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}