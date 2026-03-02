package edu.secourse.patientportal.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.routes.UserRoute;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    public EntityModel<User> toModel(User user){
        return EntityModel.of(user, //
                linkTo(methodOn(UserRoute.class).getUser(user.getUserId())).withSelfRel(),
                linkTo(methodOn(UserRoute.class).getAllUsers()).withRel("users"));
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> users) {
        // Logic to convert the collection and add links to the collection itself
        List<EntityModel<User>> userModels = StreamSupport.stream(users.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(userModels,
                linkTo(methodOn(UserRoute.class).getAllUsers()).withSelfRel());
    }
}
