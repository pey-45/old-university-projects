package es.udc.ws.app.model.registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class RegistrationDaoJdbc3Cc extends RegistrationDaoAbstract {

    @Override
    public Registration create(Connection connection, Registration registration) {
        String queryString = """
                INSERT INTO Registration
                (courseId, email, cardNumber, registrationDate, cancellationDate)
                VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setLong(i++, (registration.getCourseId()));
            preparedStatement.setString(i++, registration.getEmail());
            preparedStatement.setString(i++, registration.getCardNumber());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(registration.getRegistrationDate()));
            preparedStatement.setNull(i++, java.sql.Types.TIMESTAMP);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException("JDBC controller did not return generated key.");
            }

            i = 1;
            Long inscriptionId = resultSet.getLong(i++);

            return new Registration(inscriptionId, registration.getCourseId(), registration.getEmail(),
                    registration.getCardNumber(), registration.getRegistrationDate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}