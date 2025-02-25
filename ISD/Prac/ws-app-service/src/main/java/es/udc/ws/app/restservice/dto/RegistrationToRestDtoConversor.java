package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.registration.Registration;

import java.util.ArrayList;
import java.util.List;

public class RegistrationToRestDtoConversor {
    public static List<RegistrationRestDto> toRestDtos(List<Registration> registrations) {
        List<RegistrationRestDto> registrationDtos = new ArrayList<>(registrations.size());
        for (Registration registration : registrations) {
            registrationDtos.add(toRestDto(registration));
        }
        return registrationDtos;
    }

    public static RegistrationRestDto toRestDto(Registration registration) {
        return new RegistrationRestDto(registration.getRegistrationId(), registration.getCourseId(), registration.getEmail(), registration.getCardNumber(), registration.getRegistrationDate(), registration.getCancellationDate());
    }

    public static Registration toRegistration(RegistrationRestDto registration) {
        return new Registration(registration.getRegistrationId(), registration.getCourseId(), registration.getEmail(), registration.getCardNumber(), registration.getRegistrationDate(), registration.getCancellationDate());
    }
}
