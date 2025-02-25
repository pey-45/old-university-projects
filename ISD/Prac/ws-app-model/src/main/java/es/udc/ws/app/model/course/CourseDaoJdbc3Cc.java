package es.udc.ws.app.model.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class CourseDaoJdbc3Cc extends CourseDaoAbstract {
    @Override
    public Course create(Connection connection, Course course) {
        String queryString = """
            INSERT INTO Course
            (city, name, startDate, price, maxPlaces, registrationDate, availablePlaces)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, course.getCity());
            preparedStatement.setString(i++, course.getName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getStartDate()));
            preparedStatement.setDouble(i++, course.getPrice());
            preparedStatement.setInt(i++, course.getMaxPlaces());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getRegistrationDate()));
            preparedStatement.setInt(i++, course.getAvailablePlaces());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException("JDBC controller did not return generated key.");
            }

            i = 1;
            Long courseId = resultSet.getLong(i++);

            return new Course(courseId, course.getCity(), course.getName(), course.getStartDate(),
                    course.getPrice(), course.getMaxPlaces(), course.getRegistrationDate(), course.getAvailablePlaces());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
