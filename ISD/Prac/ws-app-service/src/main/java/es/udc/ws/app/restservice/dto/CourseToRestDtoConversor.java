package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.course.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseToRestDtoConversor {

    public static List<CourseRestDto> toRestDtos(List<Course> courses) {
        List<CourseRestDto> courseDtos = new ArrayList<>(courses.size());
        for (Course course : courses) {
            courseDtos.add(toRestDto(course));
        }
        return courseDtos;
    }

    public static CourseRestDto toRestDto(Course course) {
        return new CourseRestDto(course.getCourseId(), course.getCity(), course.getName(), course.getStartDate(), course.getPrice(), course.getMaxPlaces(), course.getAvailablePlaces());
    }

    public static Course toCourse(CourseRestDto course) {
        return new Course(course.getCourseId(), course.getCity(), course.getName(), course.getStartDate(), course.getPrice(), course.getMaxPlaces());
    }
}