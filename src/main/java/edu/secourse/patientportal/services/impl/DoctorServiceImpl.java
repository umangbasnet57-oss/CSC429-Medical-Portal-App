package edu.secourse.patientportal.services.impl;

import edu.secourse.patientportal.dto.DoctorDTO;
import edu.secourse.patientportal.exceptions.DoctorNotFoundException;
import edu.secourse.patientportal.modelassemblers.DoctorModelAssembler;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.repositories.DoctorRepository;
import edu.secourse.patientportal.services.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;
    private final DoctorModelAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository repository, DoctorModelAssembler assembler, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }
    @Override
    public Doctor createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctor = repository.save(doctor);

        return doctor;
    }

    @Override
    public List<EntityModel<Doctor>> getAllDoctors() {
        List<EntityModel<Doctor>> doctors = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return doctors;
    }

    @Override
    public Doctor getOneDoctor(Integer id) {
        Doctor doctor = repository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return  doctor;
    }
}
