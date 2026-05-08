package edu.secourse.patientportal.dto;

import edu.secourse.patientportal.model.User;

public record UserResponse(
    String username,
    String name,
    String email,
    String role
) {
    // Static factory method to make mapping easier in controller
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
