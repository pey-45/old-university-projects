package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.thrift.ThriftCourseDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientCourseDtoToThriftCourseDtoConversor {

    public static ThriftCourseDto toThriftCourseDto(
            ClientCourseDto clientCourseDto) {

        Long courseId = clientCourseDto.getCourseId();

        return new ThriftCourseDto(
                courseId == null ? -1 : courseId.longValue(),
                clientCourseDto.getCity(),
                clientCourseDto.getName(),
                // Parse the date string to LocalDateTime and convert to timestamp
                LocalDateTime.parse(clientCourseDto.getStartDate()).toEpochSecond(ZoneOffset.UTC),
                clientCourseDto.getPrice(),
                clientCourseDto.getMaxPlaces(),
                clientCourseDto.getAvailablePlaces());
    }

    public static List<ClientCourseDto> toClientCourseDtos(List<ThriftCourseDto> courses) {
        List<ClientCourseDto> clientCourseDtos = new ArrayList<>(courses.size());

        for (ThriftCourseDto course : courses) {
            clientCourseDtos.add(toClientCourseDto(course));
        }
        return clientCourseDtos;
    }

    private static ClientCourseDto toClientCourseDto(ThriftCourseDto course) {
        return new ClientCourseDto(
                course.getCourseId(),
                course.getCity(),
                course.getName(),
                // Convert timestamp back to a formatted date string
                LocalDateTime.ofEpochSecond(course.getStartDate(), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                course.getPrice(),
                course.getMaxPlaces(),
                course.getAvailablePlaces());
    }
}