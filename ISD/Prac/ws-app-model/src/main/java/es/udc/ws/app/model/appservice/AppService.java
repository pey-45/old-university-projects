package es.udc.ws.app.model.appservice;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.registration.Registration;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface AppService {
    // FUNC-1
    Course addCourse(Course course) throws InputValidationException;

    // FUNC-2
    List<Course> findCourses(String city, LocalDateTime minStartDate) throws InputValidationException;

    // FUNC-3
    Course findCourse(Long courseId) throws InstanceNotFoundException;

    // FUNC-4
    Registration registerForCourse(String email, Long courseId, String paymentCard) throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException, FullCourseException, EmailAlreadyRegisteredException;

    // FUNC-5
    void cancelRegistration(Long registrationId, String email) throws InstanceNotFoundException, CancellationEmailDoesNotMatchException, CancellationOutOfDeadlineException, RegistrationAlreadyCancelledException;

    // FUNC-6
    List<Registration> findAllRegistrationsFrom(String email);
}
