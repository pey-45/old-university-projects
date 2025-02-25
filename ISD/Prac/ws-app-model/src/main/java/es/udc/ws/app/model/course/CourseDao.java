package es.udc.ws.app.model.course;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface CourseDao {
    Course create(Connection connection, Course course);
    Course find(Connection connection, Long courseId) throws InstanceNotFoundException;
    List<Course> findBy(Connection connection, String city, LocalDateTime minStartDate);
    void update(Connection connection, Course course) throws InstanceNotFoundException;
    void remove(Connection connection, Long courseId) throws InstanceNotFoundException;
}
