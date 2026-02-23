package edu.secourse.patientportal.services.impl;

import edu.secourse.patientportal.dto.PatientDTO;
import edu.secourse.patientportal.exceptions.PatientNotFoundException;
import edu.secourse.patientportal.modelassemblers.PatientModelAssembler;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.repositories.PatientRepository;
import edu.secourse.patientportal.services.PatientService;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientModelAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, PatientModelAssembler assembler, ModelMapper modelMapper){
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }

    @Override
    public Patient createPatient(PatientDTO patientDTO) {
        Patient patient = modelMapper.map(patientDTO, Patient.class);
        patient = patientRepository.save(patient);

        return patient;
    }

    @Override
    public List<EntityModel<Patient>> getAllPatients() {
        List<EntityModel<Patient>> patients = patientRepository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return patients;
    }

    @Override
    public Patient getOnePatient(Integer id){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        return  patient;
    }
}
