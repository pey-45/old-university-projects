package es.udc.ws.app.model.appservice;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.course.CourseDao;
import es.udc.ws.app.model.course.CourseDaoFactory;
import es.udc.ws.app.model.registration.Registration;
import es.udc.ws.app.model.registration.RegistrationDao;
import es.udc.ws.app.model.registration.RegistrationDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class AppServiceImpl implements AppService {

    private final DataSource dataSource;
    private final CourseDao courseDao;
    private final RegistrationDao registrationDao;

    public AppServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);
        courseDao = CourseDaoFactory.getDao();
        registrationDao = RegistrationDaoFactory.getDao();
    }

    private void validateCourse(Course course) throws InputValidationException {
        // Validate city
        PropertyValidator.validateMandatoryString("city", course.getCity());
        // Validate name
        PropertyValidator.validateMandatoryString("name", course.getName());
        // Validate startDate
        if (course.getStartDate() == null) {
            throw new InputValidationException("Start date cannot be null");
        }
        // Validate price
        PropertyValidator.validateDouble("price", course.getPrice(), 0, Double.MAX_VALUE);
        // Validate maxPlaces

        if (course.getMaxPlaces() <= 0) {
            throw new InputValidationException("Maximum places number must be higher or equal than 0");
        }
    }

    private void validateRegistration(Registration registration) throws InputValidationException {
        // Validate courseId
        if (registration.getCourseId() == null) {
            throw new InputValidationException("Course id cannot be null");
        }
        PropertyValidator.validateNotNegativeLong("courseId", registration.getCourseId());
        // Validate email
        PropertyValidator.validateMandatoryString("email", registration.getEmail());
        // Validate cardNumber
        PropertyValidator.validateCreditCard(registration.getCardNumber());
    }

    @Override
    public Course addCourse(Course course) throws InputValidationException {
        course.setRegistrationDate(LocalDateTime.now());

        validateCourse(course);

        // Verify relation between startDate and registrationDate
        if (course.getStartDate().isBefore(course.getRegistrationDate().plusDays(MIN_DAYS_BEFORE_START))) {
            throw new InputValidationException("Course's start date must be at least 15 days after registration date");
        }

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // POST course
                Course createdCourse = courseDao.create(connection, course);

                connection.commit();

                return createdCourse;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findCourses(String city, LocalDateTime minStartDate) throws InputValidationException {
        // Validación: La ciudad no puede ser nula ni vacía
        if (city == null || city.trim().isEmpty()) {
            throw new InputValidationException("City cannot be null or empty");
        }

        // Validación: La fecha mínima no puede ser nula
        if (minStartDate == null) {
            throw new InputValidationException("Start date cannot be null");
        }

        // Llamada al DAO para buscar cursos
        try (Connection connection = dataSource.getConnection()) {
            return courseDao.findBy(connection, city, minStartDate);
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing database: " + e.getMessage(), e);
        }
    }

    @Override
    public Course findCourse(Long courseId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            // GET course by id
            return courseDao.find(connection, courseId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Registration registerForCourse(String email, Long courseId, String paymentCard) throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException, FullCourseException, EmailAlreadyRegisteredException {

        // Establish a static time to refer to now
        LocalDateTime now = LocalDateTime.now();

        // Create and validate a registration
        Registration registration = new Registration(courseId, email, paymentCard, now);
        validateRegistration(registration);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // GET the course to register into
                Course course = courseDao.find(connection, courseId);

                // Verify that the email is not already registered into the course
                for (Registration r : registrationDao.findBy(connection, email)) {
                    if (r.getCourseId().equals(courseId)) {
                        throw new EmailAlreadyRegisteredException(courseId, email);
                    }
                }

                // Verify that the course has not already started
                if (course.getStartDate().isBefore(now)) {
                    throw new RegistrationOutOfDeadlineException(courseId, course.getStartDate());
                }

                // Verify that there are available places
                if (course.getAvailablePlaces() <= 0) {
                    throw new FullCourseException(courseId);
                }

                // POST registration
                Registration createdRegistration = registrationDao.create(connection, registration);
                course.setAvailablePlaces(course.getAvailablePlaces() - 1);
                courseDao.update(connection, course);

                connection.commit();

                return createdRegistration;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelRegistration(Long registrationId, String email) throws InstanceNotFoundException, CancellationEmailDoesNotMatchException, CancellationOutOfDeadlineException, RegistrationAlreadyCancelledException {
        LocalDateTime now = LocalDateTime.now();

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Registration registration = registrationDao.find(connection, registrationId);
                Course course = courseDao.find(connection, registration.getCourseId());

                // Firstly verify that the canceller is the owner of the registration
                if (!email.equals(registration.getEmail())) {
                    throw new CancellationEmailDoesNotMatchException(registrationId, email);
                }

                // Verify that the registration is not already cancelled
                if (registrationDao.find(connection, registration.getRegistrationId()).getCancellationDate() != null) {
                    throw new RegistrationAlreadyCancelledException(registrationId);
                }

                // Verify it is not too late to cancel
                if (now.isAfter(course.getStartDate()) || Duration.between(now, course.getStartDate()).toDays() < MIN_DAYS_TO_CANCEL_BEFORE_START) {
                    throw new CancellationOutOfDeadlineException(registrationId);
                }

                registration.setCancellationDate(now);

                // UPDATE registration
                registrationDao.update(connection, registration);
                course.setAvailablePlaces(course.getAvailablePlaces() + 1);
                courseDao.update(connection, course);

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Registration> findAllRegistrationsFrom(String email) {
        try (Connection connection = dataSource.getConnection()) {
            // GET registrations by email
            return registrationDao.findBy(connection, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
