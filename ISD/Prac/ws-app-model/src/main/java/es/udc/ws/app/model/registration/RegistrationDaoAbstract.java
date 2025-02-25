package es.udc.ws.app.model.registration;

import es.udc.ws.app.model.course.Course;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class RegistrationDaoAbstract implements RegistrationDao {

    @Override
    public Registration find(Connection connection, Long registrationId) throws InstanceNotFoundException {

        String queryString = """
            SELECT courseId, email, cardNumber, registrationDate, cancellationDate
            FROM Registration
            WHERE registrationId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, registrationId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(registrationId, Course.class.getName());
            }

            i = 1;
            Long courseId = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String cardNumber = resultSet.getString(i++);
            LocalDateTime registrationDate = resultSet.getTimestamp(i++).toLocalDateTime();
            LocalDateTime cancellationDate = (resultSet.getTimestamp(i) != null) ? resultSet.getTimestamp(i++).toLocalDateTime() : null;

            return new Registration(registrationId, courseId, email, cardNumber, registrationDate, cancellationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Registration> findBy(Connection connection, String email){

        String queryString = """
            SELECT registrationId, courseId, cardNumber, registrationDate, cancellationDate
            FROM Registration
            WHERE email = ?
            ORDER BY registrationDate DESC
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setString(i++, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Registration> registrations = new ArrayList<>();

            while (resultSet.next()) {
                i = 1;
                Long registrationId = resultSet.getLong(i++);
                Long courseId = resultSet.getLong(i++);
                String cardNumber = resultSet.getString(i++);
                LocalDateTime registrationDate = resultSet.getTimestamp(i++).toLocalDateTime();
                LocalDateTime cancellationDate = resultSet.getTimestamp(i++) != null ? resultSet.getTimestamp(5).toLocalDateTime() : null;
                registrations.add(new Registration(registrationId, courseId, email, cardNumber, registrationDate, cancellationDate));
            }

            return registrations;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Registration registration) throws InstanceNotFoundException {
        String queryString = """
            UPDATE Registration
            SET courseId = ?, email = ?, cardNumber = ?, registrationDate = ?, cancellationDate = ?
            WHERE registrationId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, registration.getCourseId());
            preparedStatement.setString(i++, registration.getEmail());
            preparedStatement.setString(i++, registration.getCardNumber());
            preparedStatement.setTimestamp(i++, (registration.getRegistrationDate() != null) ? Timestamp.valueOf(registration.getRegistrationDate()) : null);
            preparedStatement.setTimestamp(i++, (registration.getCancellationDate() != null) ? Timestamp.valueOf(registration.getCancellationDate()) : null);
            preparedStatement.setLong(i++, registration.getRegistrationId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(registration.getCourseId(), Registration.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long registrationId) throws InstanceNotFoundException {
        String queryString = """
            DELETE FROM Registration
            WHERE registrationId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, registrationId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(registrationId, Course.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
