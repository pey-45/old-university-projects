package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.thrift.ThriftCourseDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class CourseToThriftCourseDtoConversor {

    public static Course toCourse(ThriftCourseDto courseDto) {
        return new Course(
                courseDto.getCourseId(),
                courseDto.getCity(),
                courseDto.getName(),
                courseDto.getStartDate() > 0 ?
                        LocalDateTime.ofEpochSecond(courseDto.getStartDate(), 0, ZoneOffset.UTC) :
                        null,
                courseDto.getPrice(),
                courseDto.getMaxPlaces()
        );
    }

    public static List<ThriftCourseDto> toThriftCourseDtos(List<Course> courses) {
        List<ThriftCourseDto> dtos = new ArrayList<>(courses.size());
        for (Course course : courses) {
            dtos.add(toThriftCourseDto(course));
        }
        return dtos;
    }

    public static ThriftCourseDto toThriftCourseDto(Course course) {
        return new ThriftCourseDto(
                course.getCourseId(),
                course.getCity(),
                course.getName(),
                course.getStartDate() != null ?
                        course.getStartDate().toEpochSecond(ZoneOffset.UTC) :
                        0,
                course.getPrice(),
                course.getMaxPlaces(),
                course.getAvailablePlaces()
        );
    }
}