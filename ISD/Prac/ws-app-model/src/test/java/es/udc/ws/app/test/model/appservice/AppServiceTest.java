package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import es.udc.ws.app.model.appservice.AppService;
import es.udc.ws.app.model.appservice.AppServiceFactory;
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
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppServiceTest {

    private final String MY_EMAIL = "me@email.com";
    private final String NOT_MY_EMAIL = "notme@email.com";
    private final String VALID_CARD_NUMBER = "1234567890123456";
    private final String VALID_CITY = "New York";
    private final String VALID_COURSE_NAME = "Course";
    private final double VALID_PRICE = 1000.;
    private final int VALID_MAX_PLACES = 100;
    private final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(20);
    private final LocalDateTime NOW = LocalDateTime.now();

    private static AppService appService = null;
    private static CourseDao courseDao = null;
    private static RegistrationDao registrationDao = null;

    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(DATA_SOURCE, dataSource);
        appService = AppServiceFactory.getService();
        courseDao = CourseDaoFactory.getDao();
        registrationDao = RegistrationDaoFactory.getDao();
    }

    private void updateCourse(Course course) {
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                courseDao.update(connection, course);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeCourse(Long courseId) {
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // Eliminar el curso directamente
                courseDao.remove(connection, courseId);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Registration findRegistration(Long registrationId) {
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Registration registration = registrationDao.find(connection, registrationId);

                connection.commit();

                return registration;

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void updateRegistration(Registration registration) {
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                registrationDao.update(connection, registration);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeRegistration(Long registrationId) {
        DataSource dataSource = DataSourceLocator.getDataSource(DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                registrationDao.remove(connection, registrationId);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Course getValidCourse() {
        return new Course(VALID_CITY, VALID_COURSE_NAME, VALID_START_DATE, VALID_PRICE, VALID_MAX_PLACES);
    }


    // FUNC-1
    @Test
    public void testAddCourse() {

        Course course = getValidCourse();

        Course addedCourse = null;

        try {
            // Validate city
            course.setCity(null);
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setCity("");
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setCity(VALID_CITY);

            // Validate name
            course.setName(null);
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setName("");
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setName(VALID_COURSE_NAME);

            // Validate start date
            course.setStartDate(null);
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setStartDate(NOW.plusDays(MIN_DAYS_BEFORE_START).minusDays(1));
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setStartDate(VALID_START_DATE);

            // Validate price
            course.setPrice(-1);
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setPrice(VALID_PRICE);

            // Validate max places
            course.setMaxPlaces(-1);
            assertThrows(InputValidationException.class, () -> appService.addCourse(course));
            course.setMaxPlaces(VALID_MAX_PLACES);

            // POST course
            addedCourse = assertDoesNotThrow(() -> appService.addCourse(course));

        } finally {
            if (addedCourse != null) {
                removeCourse(addedCourse.getCourseId());
            }
        }
    }

    // FUNC-2
    @Test
    public void testFindCoursesByCityAndStartDate() throws InputValidationException {
        final Course addedCourse = appService.addCourse(getValidCourse()); // Declarar como final
        LocalDateTime searchDate = addedCourse.getStartDate().minusDays(1);

        try {
            // Caso 1: Ciudad nula
            assertThrows(InputValidationException.class, () -> appService.findCourses(null, searchDate));

            // Caso 2: Ciudad vacía
            assertThrows(InputValidationException.class, () -> appService.findCourses("  ", searchDate));

            // Caso 3: Fecha mínima nula
            assertThrows(InputValidationException.class, () -> appService.findCourses(addedCourse.getCity(), null));

            // Caso 4: Búsqueda exitosa
            List<Course> foundCourses = assertDoesNotThrow(() -> appService.findCourses(addedCourse.getCity(), searchDate));
            assertFalse(foundCourses.isEmpty());
            System.out.println(foundCourses.size());
            assertEquals(addedCourse, foundCourses.getLast());

        } finally {
            removeCourse(addedCourse.getCourseId());
        }
    }


    // FUNC-3
    @Test
    public void testFindCourse() throws InputValidationException {
        Course addedCourse = appService.addCourse(getValidCourse());

        try {
            // Verify case of nonexistent course
            assertThrows(InstanceNotFoundException.class, () -> appService.findCourse(-1L));

            // Verify case of existing course
            Course foundCourse = assertDoesNotThrow(() -> appService.findCourse(addedCourse.getCourseId()));

            // Verify the found course is the one we searched for
            assertEquals(addedCourse, foundCourse);
        } finally {
            removeCourse(addedCourse.getCourseId());
        }
    }


    // FUNC-4
    @Test
    public void testRegisterForCourse() throws InputValidationException {

        // POST valid course
        Course addedCourse = appService.addCourse(getValidCourse());
        Registration registration = null;

        try {
            // Verify courseId
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, null, VALID_CARD_NUMBER));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, -1L, VALID_CARD_NUMBER));
            assertThrows(InstanceNotFoundException.class, () -> appService.registerForCourse(MY_EMAIL, 1000L, VALID_CARD_NUMBER));

            // Verify email
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(null, addedCourse.getCourseId(), VALID_CARD_NUMBER));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse("", addedCourse.getCourseId(), VALID_CARD_NUMBER));

            // Verify card number
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), null));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), ""));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), "12345678901234567"));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), "123456789012345"));
            assertThrows(InputValidationException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), "12345678901s3456"));

            // Verify case of trying to register out of deadline
            addedCourse.setStartDate(NOW.minusDays(1));
            updateCourse(addedCourse);
            assertThrows(RegistrationOutOfDeadlineException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), VALID_CARD_NUMBER));

            // Verify case of full course
            addedCourse.setStartDate(NOW.plusDays(1));
            addedCourse.setAvailablePlaces(0);
            updateCourse(addedCourse);
            assertThrows(FullCourseException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), VALID_CARD_NUMBER));
            addedCourse.setAvailablePlaces(VALID_MAX_PLACES);
            updateCourse(addedCourse);

            // POST registration
            registration = assertDoesNotThrow(() -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), VALID_CARD_NUMBER));

            // Verify case of email already registered
            assertThrows(EmailAlreadyRegisteredException.class, () -> appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), VALID_CARD_NUMBER));

        } finally {
            if (registration != null) {
                removeRegistration(registration.getRegistrationId());
            }
            removeCourse(addedCourse.getCourseId());
        }
    }

    // FUNC-5
    @Test
    public void testCancelRegistration() throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException, FullCourseException, EmailAlreadyRegisteredException {

        // POST valid course and a valid registration for it
        Course addedCourse = appService.addCourse(getValidCourse());
        Registration registration = appService.registerForCourse(MY_EMAIL, addedCourse.getCourseId(), VALID_CARD_NUMBER);
        final Long registrationId = registration.getRegistrationId();

        try {
            // Verify case of nonexistent registration
            assertThrows(InstanceNotFoundException.class, () -> appService.cancelRegistration(-1L, MY_EMAIL));

            // Verify case of trying to cancel with a different email
            assertThrows(CancellationEmailDoesNotMatchException.class, () -> appService.cancelRegistration(registrationId, NOT_MY_EMAIL));

            // Verify case of trying to cancel when the course has already started
            addedCourse.setStartDate(NOW.minusDays(1));
            updateCourse(addedCourse);
            assertThrows(CancellationOutOfDeadlineException.class, () -> appService.cancelRegistration(registrationId, MY_EMAIL));

            // Verify the case of trying to cancel less than 7 days before the course starts
            addedCourse.setStartDate(NOW.plusDays(MIN_DAYS_TO_CANCEL_BEFORE_START - 1));
            updateCourse(addedCourse);
            assertThrows(CancellationOutOfDeadlineException.class, () -> appService.cancelRegistration(registrationId, MY_EMAIL));

            // UPDATE registration
            addedCourse.setStartDate(VALID_START_DATE);
            updateCourse(addedCourse);
            assertDoesNotThrow(() -> appService.cancelRegistration(registrationId, MY_EMAIL));

            // Registration already cancelled
            assertThrows(RegistrationAlreadyCancelledException.class, () -> appService.cancelRegistration(registrationId, MY_EMAIL));
        } finally {
            removeRegistration(registrationId);
            removeCourse(addedCourse.getCourseId());
        }
    }


    // FUNC-6
    @Test
    public void testFindAllRegistrationsFrom() throws InputValidationException, FullCourseException, InstanceNotFoundException, RegistrationOutOfDeadlineException, EmailAlreadyRegisteredException {

        int N = 5, i;

        List<Course> courses = new ArrayList<>();
        List<Registration> registrations = new ArrayList<>();

        // Register for different courses in different days
        for (i = 0; i < N; i++) {
            courses.add(appService.addCourse(getValidCourse()));
            registrations.add(findRegistration(appService.registerForCourse(MY_EMAIL, courses.get(i).getCourseId(), VALID_CARD_NUMBER).getRegistrationId()));
            registrations.get(i).setRegistrationDate(NOW.plusDays(i));
            updateRegistration(registrations.get(i));
        }

        try {
            // Verify looking for registrations of an email which has not
            List<Registration> registrationsFromEmailWhichHasNot =    appService.findAllRegistrationsFrom(NOT_MY_EMAIL);
            assertTrue(registrationsFromEmailWhichHasNot.isEmpty());

            // Registrations found
            List<Registration> registrationsFromEmail = assertDoesNotThrow(() -> appService.findAllRegistrationsFrom(MY_EMAIL));

            for (i = 0; i < N; i++) {
                // Verify the found registrations are the ones we searched for. The order is inverted
                assertEquals(registrations.get(i), registrationsFromEmail.get(N-i-1));
            }

        } finally {
            for (i = 0; i < N; i++) {
                removeRegistration(registrations.get(i).getRegistrationId());
                removeCourse(registrations.get(i).getCourseId());
            }
        }
    }

}