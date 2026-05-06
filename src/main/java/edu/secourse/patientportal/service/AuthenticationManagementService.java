package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;

public interface AuthenticationManagementService {
    LoginResponse login(LoginRequest loginRequest);
}
