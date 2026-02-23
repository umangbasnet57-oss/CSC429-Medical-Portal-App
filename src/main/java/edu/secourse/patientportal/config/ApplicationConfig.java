package edu.secourse.patientportal.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Application config class is there to deal with various configuration issues application or spring boot.
 * <p>
 * This class includes configurations that allows the autowiring of other objects
 * with the @Bean annotation
 * <p>
 *
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}