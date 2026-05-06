package edu.secourse.patientportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.exception.GlobalExceptionHandler;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.service.*;
import edu.secourse.patientportal.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userService;

    @MockitoBean
    private AuthenticationManagementService authenticationService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_shouldReturn201_whenRegistrationIsSuccessful() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("fred");
        request.setEmail("fred@test.com");
        request.setPassword("password123");
        request.setFirstName("Fred");
        request.setLastName("Darko");
        request.setRole("PATIENT");

        User registeredUser = User.builder()
                .id(1)
                .username("fred")
                .email("fred@test.com")
                .firstName("Fred")
                .lastName("Darko")
                .role("PATIENT")
                .build();

        when(userService.register(any(RegisterRequest.class)))
                .thenReturn(registeredUser);

        mockMvc.perform(post(Constants.AUTH_ENDPOINT + "/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.MSG_REGISTRATION_SUCCESS))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data.username").value("fred"))
                .andExpect(jsonPath("$.data.email").value("fred@test.com"))
                .andExpect(jsonPath("$.data.firstName").value("Fred"))
                .andExpect(jsonPath("$.data.lastName").value("Darko"))
                .andExpect(jsonPath("$.data.role").value("PATIENT"));
    }

    @Test
    void login_shouldReturn200_whenLoginIsSuccessful() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("fred");
        request.setPassword("plainPassword");

        LoginResponse loginResponse = LoginResponse.builder()
                .id(1)
                .username("fred")
                .email("fred@test.com")
                .firstName("Fred")
                .lastName("Darko")
                .role("PATIENT")
                .token("jwt-token-123")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .loginAt(LocalDateTime.now())
                .build();

        when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        mockMvc.perform(post(Constants.AUTH_ENDPOINT + "/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Constants.MSG_LOGIN_SUCCESS))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.username").value("fred"))
                .andExpect(jsonPath("$.data.email").value("fred@test.com"))
                .andExpect(jsonPath("$.data.role").value("PATIENT"))
                .andExpect(jsonPath("$.data.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").value(3600));
    }

    @Test
    void login_shouldReturn400_whenRequestBodyIsInvalid() throws Exception {
        String invalidJson = """
            {
              "username": "",
              "password": ""
            }
            """;

        mockMvc.perform(post(Constants.AUTH_ENDPOINT + "/login")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).login(any(LoginRequest.class));
    }

    @Test
    void register_shouldReturn400_whenRequestBodyIsInvalid() throws Exception {
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("");
//        request.setEmail("bad-email");
//        request.setPassword("");
        String invalidJson = """
            {
              "username": "",
              "email": "bad-email",
              "password": "",
              "firstName": "",
              "lastName": "",
              "role": ""
            }
            """;

        mockMvc.perform(post(Constants.AUTH_ENDPOINT + "/register")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}