package edu.secourse.patientportal.services.impl;

import edu.secourse.patientportal.dto.AdminDTO;
import edu.secourse.patientportal.exceptions.AdminNotFoundException;
import edu.secourse.patientportal.modelassemblers.AdminModelAssembler;
import edu.secourse.patientportal.models.Admin;
import edu.secourse.patientportal.repositories.AdminRepository;
import edu.secourse.patientportal.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repository;
    private final AdminModelAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(AdminRepository repository, AdminModelAssembler assembler, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }
    @Override
    public Admin createAdmin(AdminDTO adminDTO) {
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        admin = repository.save(admin);

        return admin;
    }

    @Override
    public List<EntityModel<Admin>> getAllAdministrators() {
        List<EntityModel<Admin>> administrators = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return administrators;
    }

    @Override
    public Admin getOneAdministrator(Integer id) {
        Admin admin = repository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        return  admin;
    }
}
