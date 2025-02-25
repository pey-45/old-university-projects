package es.udc.ws.app.model.course;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class CourseDaoAbstract implements CourseDao {

    @Override
    public Course find(Connection connection, Long courseId) throws InstanceNotFoundException {

        String queryString = """
            SELECT city, name, startDate, price, maxPlaces, registrationDate, availablePlaces
            FROM Course
            WHERE courseId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, courseId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(courseId, Course.class.getName());
            }

            i = 1;
            String city = resultSet.getString(i++);
            String name = resultSet.getString(i++);
            LocalDateTime startDate = resultSet.getTimestamp(i++).toLocalDateTime();
            double price = resultSet.getDouble(i++);
            int maxPlaces = resultSet.getInt(i++);
            LocalDateTime registrationDate = resultSet.getTimestamp(i++).toLocalDateTime();
            int availablePlaces = resultSet.getInt(i++);

            return new Course(courseId, city, name, startDate, price, maxPlaces, registrationDate, availablePlaces);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findBy(Connection connection, String city, LocalDateTime minStartDate) {
        // Consulta SQL para buscar cursos por ciudad y fecha mínima
        String queryString = """
        SELECT courseId, city, name, startDate, price, maxPlaces, registrationDate, availablePlaces
        FROM Course
        WHERE LOWER(city) = LOWER(?) AND startDate >= ?
        ORDER BY startDate ASC
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            // Configurar los parámetros de la consulta
            preparedStatement.setString(1, city); // Parámetro de la ciudad
            preparedStatement.setTimestamp(2, Timestamp.valueOf(minStartDate)); // Parámetro de la fecha mínima

            // Ejecutar la consulta
            ResultSet resultSet = preparedStatement.executeQuery();

            // Crear la lista de cursos
            List<Course> courses = new ArrayList<>();

            while (resultSet.next()) {
                // Extraer los datos de cada curso
                Long courseId = resultSet.getLong("courseId");
                String courseCity = resultSet.getString("city");
                String name = resultSet.getString("name");
                LocalDateTime startDate = resultSet.getTimestamp("startDate").toLocalDateTime();
                double price = resultSet.getDouble("price");
                int maxPlaces = resultSet.getInt("maxPlaces");
                LocalDateTime registrationDate = resultSet.getTimestamp("registrationDate").toLocalDateTime();
                int availablePlaces = resultSet.getInt("availablePlaces");

                // Crear y añadir el curso a la lista
                courses.add(new Course(courseId, courseCity, name, startDate, price, maxPlaces, registrationDate, availablePlaces));
            }

            return courses;

        } catch (SQLException e) {
            throw new RuntimeException("Error querying courses by city and date: " + e.getMessage(), e);
        }
    }


    @Override
    public void update(Connection connection, Course course) throws InstanceNotFoundException {

        String queryString = """
            UPDATE Course
            SET city = ?, name = ?, startDate = ?, price = ?, maxPlaces = ?, availablePlaces = ?
            WHERE courseId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setString(i++, course.getCity());
            preparedStatement.setString(i++, course.getName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(course.getStartDate()));
            preparedStatement.setDouble(i++, course.getPrice());
            preparedStatement.setInt(i++, course.getMaxPlaces());
            preparedStatement.setInt(i++, course.getAvailablePlaces());
            preparedStatement.setLong(i++, course.getCourseId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(course.getCourseId(), Course.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long courseId) throws InstanceNotFoundException {

        String queryString = """
            DELETE FROM Course
            WHERE courseId = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, courseId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(courseId, Course.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
