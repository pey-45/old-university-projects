package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.CancellationEmailDoesNotMatchException;
import es.udc.ws.app.model.appservice.exceptions.CancellationOutOfDeadlineException;
import es.udc.ws.app.model.appservice.exceptions.RegistrationAlreadyCancelledException;
import es.udc.ws.app.model.registration.Registration;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.appservice.exceptions.RegistrationOutOfDeadlineException;
import es.udc.ws.app.model.appservice.exceptions.FullCourseException;
import es.udc.ws.app.model.appservice.exceptions.EmailAlreadyRegisteredException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftAppServiceImpl implements ThriftAppService.Iface {

    @Override
    public ThriftCourseDto addCourse(ThriftCourseDto courseDto) throws ThriftInputValidationException {

        Course course = CourseToThriftCourseDtoConversor.toCourse(courseDto);

        try {
            Course addedCourse = AppServiceFactory.getService().addCourse(course);
            return CourseToThriftCourseDtoConversor.toThriftCourseDto(addedCourse);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public List<ThriftCourseDto> findCourses(String keywords) throws ThriftInputValidationException {
        try {
            List<Course> courses = AppServiceFactory.getService().findCourses(keywords, LocalDateTime.now());
            return CourseToThriftCourseDtoConversor.toThriftCourseDtos(courses);
    }catch (InputValidationException e) {
        throw new ThriftInputValidationException(e.getMessage());
    }
    }

    @Override
    public ThriftCourseDto findCourse(long courseId) throws ThriftInstanceNotFoundException {
        try {
            Course course = AppServiceFactory.getService().findCourse(courseId);
            return CourseToThriftCourseDtoConversor.toThriftCourseDto(course);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public ThriftRegistrationDto registerForCourse(String email, long courseId, String cardNumber)
            throws ThriftInputValidationException, ThriftInstanceNotFoundException,
            ThriftRegistrationOutOfDeadlineException, ThriftFullCourseException,
            ThriftEmailAlreadyRegisteredException {
        try {
            Registration registration = AppServiceFactory.getService().registerForCourse(email, courseId, cardNumber);
            return RegistrationToThriftRegistrationDtoConversor.toThriftRegistrationDto(registration);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        } catch (RegistrationOutOfDeadlineException e) {
            throw new ThriftRegistrationOutOfDeadlineException(courseId);
        } catch (FullCourseException e) {
            throw new ThriftFullCourseException(courseId);
        } catch (EmailAlreadyRegisteredException e) {
            throw new ThriftEmailAlreadyRegisteredException(email, courseId);
        }
    }

    @Override
    public void cancelRegistration(long registrationId, String email) throws
            ThriftInstanceNotFoundException,
            ThriftCancellationEmailDoesNotMatchException,
            ThriftCancellationOutOfDeadlineException,
            ThriftRegistrationAlreadyCancelledException {
        try {
            AppServiceFactory.getService().cancelRegistration(registrationId, email);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (CancellationEmailDoesNotMatchException e) {
            throw new ThriftCancellationEmailDoesNotMatchException(registrationId, email);
        } catch (CancellationOutOfDeadlineException e) {
            throw new ThriftCancellationOutOfDeadlineException(registrationId);
        } catch (RegistrationAlreadyCancelledException e) {
            throw new ThriftRegistrationAlreadyCancelledException(registrationId);
        }
    }

    @Override
    public List<ThriftRegistrationDto> findAllRegistrationsFrom(String email) {
        final List<Registration> registrations = AppServiceFactory.getService().findAllRegistrationsFrom(email);
        return RegistrationToThriftRegistrationDtoConversor.toThriftRegistrationDtos(registrations);
    }
}
