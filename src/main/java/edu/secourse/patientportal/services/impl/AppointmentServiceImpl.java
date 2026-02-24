package edu.secourse.patientportal.services.impl;

import edu.secourse.patientportal.dto.AppointmentDTO;
import edu.secourse.patientportal.exceptions.AppointmentNotFoundException;
import edu.secourse.patientportal.exceptions.DoctorNotFoundException;
import edu.secourse.patientportal.modelassemblers.AppointmentModelAssembler;
import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.repositories.AppointmentRepository;
import edu.secourse.patientportal.services.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository repository;
    private final AppointmentModelAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository, AppointmentModelAssembler assembler, ModelMapper modelMapper){
        this.repository = repository;
        this.assembler = assembler;
        this.modelMapper = modelMapper;
    }
    /**
     * @param appointment
     * @return
     */
    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        return null;
    }

    /**
     * @param appointmentId
     * @return
     */
    @Override
    public boolean cancelAppointment(Integer appointmentId) {
        return false;
    }

    /**
     * @param appointmentId
     * @param patient
     * @param doctor
     * @param newDateTime
     * @return
     */
    @Override
    public boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime) {
        return false;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public ArrayList<Appointment> getAppointmentsForUser(User user) {
        return null;
    }

    @Override
    public Appointment getOneAppointment(Integer id) {
        Appointment appointment = repository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        return  appointment;
    }

    @Override
    public List<EntityModel<Appointment>> getAllAppointments() {
        List<EntityModel<Appointment>> appointments = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return appointments;
    }
}
