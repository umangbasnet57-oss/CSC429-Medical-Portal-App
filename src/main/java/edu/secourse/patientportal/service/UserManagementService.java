package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.User;

import java.util.List;
import java.util.Map;

public interface UserManagementService {
    User getUser(String username);
    List<User> getAllUsers();
    boolean createUser(User user);
    boolean updateUser(String username, Map<String, String> updates);
    boolean removeUser(User user);
}
