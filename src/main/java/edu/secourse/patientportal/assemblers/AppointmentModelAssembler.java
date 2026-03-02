package edu.secourse.patientportal.assemblers;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.routes.AppointmentRoute;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {
    public EntityModel<Appointment> toModel(Appointment appointment){
        return EntityModel.of(appointment, //
                linkTo(methodOn(AppointmentRoute.class).getOneAppointment(appointment.getAppointmentId())).withSelfRel(),
                linkTo(methodOn(AppointmentRoute.class).getAllAppointments()).withRel("appointments"));
    }
}
