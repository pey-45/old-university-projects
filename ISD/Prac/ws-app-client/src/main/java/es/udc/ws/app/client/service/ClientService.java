package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientService {
    ClientCourseDto addCourse(ClientCourseDto course) throws InputValidationException;
    List<ClientCourseDto> findCourses(String keywords) throws InputValidationException;
    ClientCourseDto findCourse(Long courseId) throws InstanceNotFoundException;
    ClientRegistrationDto registerForCourse(String email, Long courseId, String paymentCard) throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException, FullCourseException, EmailAlreadyRegisteredException;
    void cancelRegistration(Long registrationId, String email) throws InstanceNotFoundException, CancellationEmailDoesNotMatchException, CancellationOutOfDeadlineException, RegistrationAlreadyCancelledException;
    List<ClientRegistrationDto> findAllRegistrationsFrom(String email);
}
