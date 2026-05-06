package edu.secourse.patientportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.secourse.patientportal.exception.GlobalExceptionHandler;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.service.TokenService;
import edu.secourse.patientportal.service.UserManagementService;
import edu.secourse.patientportal.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UserController.class)
//@Import(GlobalExceptionHandler.class)
@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getUser_shouldReturn200_whenUserExists() throws Exception {
        User user = User.builder()
                .id(1)
                .username("fred")
                .email("fred@test.com")
                .build();

        when(userService.getUser("fred")).thenReturn(user);

        mockMvc.perform(get(Constants.USERS_ENDPOINT + "/fred"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("fred"));
    }

    @Test
    void getUser_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.getUser("fred")).thenReturn(null);

        mockMvc.perform(get(Constants.USERS_ENDPOINT + "/fred"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        List<User> users = List.of(
                User.builder().username("u1").build(),
                User.builder().username("u2").build()
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get(Constants.USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void createUser_shouldReturn200_whenSuccess() throws Exception {
        Map<String, String> body = Map.of(
                "username", "fred",
                "password", "pass123",
                "name", "Fred Darko",
                "email", "fred@test.com",
                "role", "PATIENT"
        );

        when(userService.createUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post(Constants.USERS_ENDPOINT + "/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_shouldReturn400_whenServiceFails() throws Exception {
        Map<String, String> body = Map.of(
                "username", "fred",
                "password", "pass123",
                "name", "Fred Darko",
                "email", "fred@test.com",
                "role", "PATIENT"
        );

        when(userService.createUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post(Constants.USERS_ENDPOINT + "/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_whenInvalidRole() throws Exception {
        Map<String, String> body = Map.of(
                "username", "fred",
                "password", "pass123",
                "name", "Fred Darko",
                "email", "fred@test.com",
                "role", "INVALID"
        );

        mockMvc.perform(post(Constants.USERS_ENDPOINT + "/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturn200_whenSuccess() throws Exception {
        Map<String, String> body = Map.of("email", "new@test.com");

        when(userService.updateUser("fred", body)).thenReturn(true);

        mockMvc.perform(put(Constants.USERS_ENDPOINT + "/fred")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturn400_whenFailure() throws Exception {
        Map<String, String> body = Map.of("email", "new@test.com");

        when(userService.updateUser("fred", body)).thenReturn(false);

        mockMvc.perform(put(Constants.USERS_ENDPOINT + "/fred")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_shouldReturn200_whenSuccess() throws Exception {
        User user = User.builder().username("fred").build();

        when(userService.getUser("fred")).thenReturn(user);
        when(userService.removeUser(user)).thenReturn(true);

        mockMvc.perform(delete(Constants.USERS_ENDPOINT + "/fred"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.getUser("fred")).thenReturn(null);

        mockMvc.perform(delete(Constants.USERS_ENDPOINT + "/fred"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturn500_whenDeletionFails() throws Exception {
        User user = User.builder().username("fred").build();

        when(userService.getUser("fred")).thenReturn(user);
        when(userService.removeUser(user)).thenReturn(false);

        mockMvc.perform(delete(Constants.USERS_ENDPOINT + "/fred"))
                .andExpect(status().isInternalServerError());
    }
}