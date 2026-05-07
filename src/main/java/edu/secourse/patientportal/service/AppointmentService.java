package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.*;
import edu.secourse.patientportal.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service

/**
 * Service layer responsible for managing {@link Appointment} objects.
 * <p>
 * Uses JPA repository when running in Spring Boot context.
 * Falls back to in-memory ArrayList for unit tests.
 */
public class AppointmentService implements AppointmentManagementService{

    /** JPA repository — injected by Spring, null when used in unit tests. */
    @Autowired(required = false)
    private AppointmentRepository appointmentRepository;

    /** In-memory fallback list (used by unit tests). */
    private final ArrayList<Appointment> appointments = new ArrayList<>();

    /** Auto-incrementing ID counter for in-memory mode. */
    private int nextId = 1;

    /**
     * Creates a new appointment if it does not duplicate an existing one.
     * <p>
     * Two appointments are considered duplicates if they share:
     * <ul>
     *     <li>Same patient</li>
     *     <li>Same doctor</li>
     *     <li>Same date/time (down to the minute)</li>
     * </ul>
     *
     * @param appointment the appointment to be added
     * @return true if appointment was successfully created, false otherwise
     */
    @Override
    public boolean createAppointment(Appointment appointment) {
        if (appointment == null ||
                appointment.getPatient() == null ||
                appointment.getDoctor() == null ||
                appointment.getAppointmentDateTime() == null) {
            System.out.println("Invalid appointment data.");
            return false;
        }

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();

        LocalDateTime appointmentTime = appointment.getAppointmentDateTime()
                .truncatedTo(ChronoUnit.MINUTES);

        try {
            if (appointmentRepository != null) {

                boolean doctorAlreadyBooked = appointmentRepository.findByDoctor(doctor)
                        .stream()
                        .anyMatch(existing ->
                                existing.getAppointmentDateTime() != null &&
                                        existing.getAppointmentDateTime()
                                                .truncatedTo(ChronoUnit.MINUTES)
                                                .equals(appointmentTime)
                        );

                boolean patientAlreadyBooked = appointmentRepository.findByPatient(patient)
                        .stream()
                        .anyMatch(existing ->
                                existing.getAppointmentDateTime() != null &&
                                        existing.getAppointmentDateTime()
                                                .truncatedTo(ChronoUnit.MINUTES)
                                                .equals(appointmentTime)
                        );

                if (doctorAlreadyBooked || patientAlreadyBooked) {
                    System.out.println("Doctor or patient already booked at: " + appointmentTime);
                    return false;
                }

                appointmentRepository.save(appointment);
                return true;
            }

            boolean conflictExists = appointments.stream().anyMatch(existing -> {
                if (existing.getAppointmentDateTime() == null) {
                    return false;
                }

                LocalDateTime existingTime = existing.getAppointmentDateTime()
                        .truncatedTo(ChronoUnit.MINUTES);

                boolean sameDoctorSameTime =
                        existing.getDoctor().equals(doctor) &&
                                existingTime.equals(appointmentTime);

                boolean samePatientSameTime =
                        existing.getPatient().equals(patient) &&
                                existingTime.equals(appointmentTime);

                return sameDoctorSameTime || samePatientSameTime;
            });

            if (conflictExists) {
                return false;
            }

            appointment.setAppointmentId(nextId++);
            appointments.add(appointment);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//    public boolean createAppointment(Appointment appointment) {
//        try {
//            if (appointment == null ||
//                    appointment.getPatient() == null ||
//                    appointment.getDoctor() == null ||
//                    appointment.getAppointmentDateTime() == null) {
//                return false;
//            }
//
//            Patient patient = appointment.getPatient();
//            Doctor doctor = appointment.getDoctor();
//            LocalDateTime appointmentTime = appointment.getAppointmentDateTime()
//                    .truncatedTo(ChronoUnit.MINUTES);
//
//            if (appointmentRepository != null) {
//
//                boolean doctorAlreadyBooked = appointmentRepository.findByDoctor(doctor)
//                        .stream()
//                        .anyMatch(existing ->
//                                existing.getAppointmentDateTime()
//                                        .truncatedTo(ChronoUnit.MINUTES)
//                                        .equals(appointmentTime)
//                        );
//
//                boolean patientAlreadyBooked = appointmentRepository.findByPatient(patient)
//                        .stream()
//                        .anyMatch(existing ->
//                                existing.getAppointmentDateTime()
//                                        .truncatedTo(ChronoUnit.MINUTES)
//                                        .equals(appointmentTime)
//                        );
//
//                if (doctorAlreadyBooked || patientAlreadyBooked) {
//                    return false;
//                }
//
//                appointmentRepository.save(appointment);
//                return true;
//            }
//
//            boolean conflictExists = appointments.stream()
//                    .anyMatch(existing -> {
//                        LocalDateTime existingTime = existing.getAppointmentDateTime()
//                                .truncatedTo(ChronoUnit.MINUTES);
//
//                        boolean sameDoctorSameTime =
//                                existing.getDoctor().equals(doctor) &&
//                                        existingTime.equals(appointmentTime);
//
//                        boolean samePatientSameTime =
//                                existing.getPatient().equals(patient) &&
//                                        existingTime.equals(appointmentTime);
//
//                        return sameDoctorSameTime || samePatientSameTime;
//                    });
//
//            if (conflictExists) {
//                return false;
//            }
//
//            appointments.add(appointment);
//            return true;
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    public boolean createAppointment(Appointment appointment) {
//        boolean success = false;
//        try {
//            if (appointmentRepository != null) {
//                // Check for duplicates in the database
//                Patient p = appointment.getPatient();
//                Doctor d = appointment.getDoctor();
//                boolean exists = appointmentRepository.findByPatient(p).stream().anyMatch(a ->
//                        a.getDoctor().equals(d) &&
//                        a.getAppointmentDateTime().truncatedTo(ChronoUnit.MINUTES)
//                         .equals(appointment.getAppointmentDateTime().truncatedTo(ChronoUnit.MINUTES))
//                );
//                if (!exists) {
//                    appointmentRepository.save(appointment);
//                    success = true;
//                }
//            } else {
//                boolean exists = false;
//                for (Appointment existingAppointment : appointments) {
//                    boolean samePatient = existingAppointment.getPatient().equals(appointment.getPatient());
//                    boolean sameDoctor = existingAppointment.getDoctor().equals(appointment.getDoctor());
//                    boolean sameTime = existingAppointment.getAppointmentDateTime()
//                            .truncatedTo(ChronoUnit.MINUTES)
//                            .equals(appointment.getAppointmentDateTime().truncatedTo(ChronoUnit.MINUTES));
//                    if (samePatient && sameDoctor && sameTime) {
//                        exists = true;
//                        break;
//                    }
//                }
//                if (!exists) {
////                    appointment.setAppointmentId(nextId++);
//                    appointments.add(appointment);
//                    success = true;
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//        return success;
//    }

    /**
     * Cancels an existing appointment by ID by marking its status as CANCELLED.
     *
     * @param appointmentId the ID of the appointment to cancel
     * @return true if cancellation succeeded, false if not found
     */
    @Override
    public boolean cancelAppointment(int appointmentId) {
        boolean success = false;
        try {
            if (appointmentRepository != null) {
                Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
                if (appointment != null) {
                    appointment.setStatus(Appointment.Status.CANCELLED);
                    appointmentRepository.save(appointment);
                    success = true;
                }
            } else {
                for (Appointment appointment : appointments) {
                    if (appointment != null && appointment.getAppointmentId() == appointmentId) {
                        appointment.setStatus(Appointment.Status.CANCELLED);
                        success = true;
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Modifies an existing appointment by updating its patient, doctor, and date/time.
     *
     * @param appointmentId the ID of the appointment to modify
     * @param patient       the new patient object
     * @param doctor        the new doctor object
     * @param newDateTime   the new appointment date/time
     * @return true if the appointment was modified, false otherwise
     */
    @Override
    public boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime) {
        boolean success = false;
        try {
            if (appointmentRepository != null) {
                Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
                if (appointment != null) {
                    appointment.setPatient(patient);
                    appointment.setDoctor(doctor);
                    appointment.setAppointmentDateTime(newDateTime);
                    appointment.setStatus(Appointment.Status.ACTIVE);
                    appointmentRepository.save(appointment);
                    success = true;
                }
            } else {
                for (Appointment value : appointments) {
                    if (value != null && value.getAppointmentId() == appointmentId) {
                        value.setPatient(patient);
                        value.setDoctor(doctor);
                        value.setAppointmentDateTime(newDateTime);
                        value.setStatus(Appointment.Status.ACTIVE);
                        success = true;
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Retrieves all appointments associated with a specific user.
     * <p>
     * Rules:
     * <ul>
     *     <li>If user is a Patient → return all appointments in which they are the patient</li>
     *     <li>If user is a Doctor → return all appointments in which they are the doctor</li>
     * </ul>
     *
     * @param user the user whose appointments should be returned
     * @return a list of matching appointments (empty if none or if user is invalid)
     */
    @Override
    public ArrayList<Appointment> getAppointmentsForUser(User user) {
        ArrayList<Appointment> result = new ArrayList<>();
        try {
            if (user != null) {
                if (appointmentRepository != null) {
                    if (user instanceof Patient) {
                        result.addAll(appointmentRepository.findByPatient((Patient) user));
                    } else if (user instanceof Doctor) {
                        result.addAll(appointmentRepository.findByDoctor((Doctor) user));
                    }
                } else {
                    for (Appointment appointment : appointments) {
                        if (user instanceof Patient && appointment.getPatient().equals(user)) {
                            result.add(appointment);
                        } else if (user instanceof Doctor && appointment.getDoctor().equals(user)) {
                            result.add(appointment);
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return result;
    }
}
