package edu.secourse.patientportal;

import edu.secourse.patientportal.models.*;
import edu.secourse.patientportal.services.UserService;
import edu.secourse.patientportal.controllers.UserController;
import edu.secourse.patientportal.services.AppointmentService;
import edu.secourse.patientportal.controllers.AppointmentController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean state = false;

        UserService userService = new UserService();
        AppointmentService appointmentService = new AppointmentService();
        UserController userController = new UserController(userService);
        AppointmentController appointmentController = new AppointmentController(appointmentService);

        // Demo Data
        Patient newUser = new Patient("john123", "John Smith", "pass123", "johnsmith123@gmail.com");
        Doctor newUser2 = new Doctor("jack123", "Jack Smith", "pass123", "jack123@gmail.com");
        LocalDateTime time = LocalDateTime.of(2025, 12, 12, 8, 30);
        Appointment app = new Appointment(newUser, newUser2, time);

        appointmentService.createAppointment(app);
        userService.createUser(newUser);
        userService.createUser(newUser2);

        while (!state) {

            Scanner input = new Scanner(System.in);

            System.out.println("********************************");
            System.out.println("Patient Portal - Admin Dashboard");
            System.out.println("********************************");
            System.out.println("1. Create User");
            System.out.println("2. Print User");
            System.out.println("3. Delete User");
            System.out.println("4. Update User Details");
            System.out.println("5. Create Appointment");
            System.out.println("6. Modify Appointment");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Find Appointment");
            System.out.println("9. Exit");
            System.out.println("********************************");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine(); // Clear newline

            switch (choice) {

                case 1:
                    System.out.println("Please enter a username: ");
                    String username = input.nextLine().trim();

                    System.out.println("Please enter a password: ");
                    String password = input.nextLine().trim();

                    System.out.println("Please enter a name: ");
                    String name = input.nextLine().trim();

                    System.out.println("Please enter an email: ");
                    String email = input.nextLine().trim();

                    System.out.println("Please enter a role (patient, doctor, admin): ");
                    String role = input.nextLine().toLowerCase();

                    if (role.equals("patient")) {
                        User user = new Patient(username, password, name, email);
                        user.setRole(role);
                        userController.createUser(user);

                    } else if (role.equals("doctor")) {
                        User user = new Doctor(username, password, name, email);
                        user.setRole(role);
                        userController.createUser(user);

                    } else if (role.equals("admin")) {
                        User user = new Admin(username, password, name, email);
                        user.setRole(role);
                        userController.createUser(user);

                    } else {
                        System.out.println("Invalid role");
                    }
                    break;

                case 2:
                    System.out.print("Please enter a username: ");
                    username = input.nextLine().trim();
                    userService.printUser(username);
                    break;

                case 3:
                    System.out.print("Please enter a username: ");
                    username = input.nextLine().trim();

                    User user = userService.getUser(username);
                    userService.printUser(username);

                    System.out.println("\nAre you sure you want to delete this user?");
                    System.out.print("Re-enter the username to confirm or type 'no' to exit: ");
                    String confirm = input.nextLine().trim();

                    if (confirm.equals(username)) {
                        userService.removeUser(user);
                        System.out.println("User removed successfully");
                    } else {
                        System.out.println("Deletion cancelled");
                    }
                    break;

                case 4:
                    System.out.print("Please enter a username: ");
                    String oldUsername = input.nextLine().trim();

                    System.out.print("Please enter new username: ");
                    String newUsername = input.nextLine().trim();

                    System.out.print("Please enter new password: ");
                    password = input.nextLine().trim();

                    System.out.print("Please enter new name: ");
                    name = input.nextLine().trim();

                    System.out.print("Please enter new email: ");
                    email = input.nextLine().trim();

                    boolean success = userController.updateUser(
                            oldUsername, newUsername, password, name, email
                    );

                    if (success) {
                        System.out.println("\nNew user details:");
                        userService.printUser(newUsername);
                    }
                    break;

                case 5:
                    try {
                        System.out.print("Please enter patient username: ");
                        String patientUsername = input.nextLine().trim();
                        User patientUser = userService.getUser(patientUsername);

                        System.out.print("Please enter doctor username: ");
                        String doctorUsername = input.nextLine().trim();
                        User doctorUser = userService.getUser(doctorUsername);

                        if (!(patientUser instanceof Patient)) {
                            System.out.println("Invalid patient username.");
                            break;
                        }
                        if (!(doctorUser instanceof Doctor)) {
                            System.out.println("Invalid doctor username.");
                            break;
                        }

                        System.out.print("Please enter date/time (Year,Month,Day,Hour,Minute): ");
                        String[] sections = input.nextLine().split(",");

                        LocalDateTime appointmentDateTime = LocalDateTime.of(
                                Integer.parseInt(sections[0]),
                                Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]),
                                Integer.parseInt(sections[3]),
                                Integer.parseInt(sections[4])
                        );

                        Appointment appointment = new Appointment(
                                (Patient) patientUser,
                                (Doctor) doctorUser,
                                appointmentDateTime
                        );

                        success = appointmentController.createAppointment(appointment);

                        if (success) {
                            appointmentController.printAppointment(appointment);
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error creating appointment: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        System.out.print("Enter appointment ID to modify: ");
                        int appointmentId = input.nextInt();
                        input.nextLine();

                        System.out.print("Please enter patient username: ");
                        String pUser = input.nextLine().trim();
                        User patientUser = userService.getUser(pUser);

                        System.out.print("Please enter doctor username: ");
                        String dUser = input.nextLine().trim();
                        User doctorUser = userService.getUser(dUser);

                        if (!(patientUser instanceof Patient)) {
                            System.out.println("Invalid patient username.");
                            break;
                        }
                        if (!(doctorUser instanceof Doctor)) {
                            System.out.println("Invalid doctor username.");
                            break;
                        }

                        System.out.print("Please enter new date/time (Year,Month,Day,Hour,Minute): ");
                        String[] s2 = input.nextLine().split(",");

                        LocalDateTime newDateTime = LocalDateTime.of(
                                Integer.parseInt(s2[0]),
                                Integer.parseInt(s2[1]),
                                Integer.parseInt(s2[2]),
                                Integer.parseInt(s2[3]),
                                Integer.parseInt(s2[4])
                        );

                        appointmentController.modifyAppointment(
                                appointmentId,
                                (Patient) patientUser,
                                (Doctor) doctorUser,
                                newDateTime
                        );

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error modifying appointment: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.print("Enter appointment ID to cancel: ");
                    int appointmentId = input.nextInt();
                    input.nextLine();
                    appointmentController.cancelAppointment(appointmentId);
                    break;

                case 8:
                    System.out.print("Enter username: ");
                    username = input.nextLine().trim();

                    user = userService.getUser(username);

                    if (user != null) {
                        List<Appointment> appointments =
                                appointmentController.getAppointmentsForUser(user);

                        if (appointments != null && !appointments.isEmpty()) {
                            System.out.println("Appointments for " + user.getUsername() + ":");
                            for (Appointment appointment : appointments) {
                                System.out.println(appointment);
                            }
                        } else {
                            System.out.println("No appointments found for " + username + ".");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 9:
                    state = true;
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
