package es.udc.ws.app.thriftservice;


import es.udc.ws.app.model.registration.Registration;
import es.udc.ws.app.thrift.ThriftRegistrationDto;

import java.util.List;

public class RegistrationToThriftRegistrationDtoConversor {

    public static ThriftRegistrationDto toThriftRegistrationDto(Registration registration) {
        return new ThriftRegistrationDto(
                registration.getRegistrationId(),
                registration.getCourseId(),
                registration.getEmail(),
                registration.getCardNumber(),
                registration.getRegistrationDate().toString(),
                registration.getCancellationDate() != null ? registration.getCancellationDate().toString() : null
        );
    }

    public static List<ThriftRegistrationDto> toThriftRegistrationDtos(List<Registration> registrations) {
        return registrations.stream().map(RegistrationToThriftRegistrationDtoConversor::toThriftRegistrationDto).toList();
    }
}
