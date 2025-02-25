package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.restservice.dto.CourseRestDto;
import es.udc.ws.app.restservice.dto.CourseToRestDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.CourseJsonToRestDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ServletUtils.checkEmptyPath(req);

            CourseRestDto courseRestDto = CourseJsonToRestDtoConversor.toRestDto(req.getInputStream());
            Course course = CourseToRestDtoConversor.toCourse(courseRestDto);

            course = AppServiceFactory.getService().addCourse(course);

            CourseRestDto createdCourseRestDto = CourseToRestDtoConversor.toRestDto(course);
            String courseURL = ServletUtils.normalizePath(req.getRequestURL().toString() + "/" + course.getCourseId());

            Map<String, String> headers = new HashMap<>();
            headers.put("Location", courseURL);

            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_CREATED,
                    CourseJsonToRestDtoConversor.toObjectNode(createdCourseRestDto),
                    headers
            );
        } catch (InputValidationException e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        } catch (Exception e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = ServletUtils.normalizePath(req.getPathInfo());

            if (path == null || path.isEmpty()) {
                String cityParam = ServletUtils.getMandatoryParameter(req, "city");
                List<Course> courses = AppServiceFactory.getService().findCourses(cityParam, LocalDateTime.now());
                List<CourseRestDto> courseRestDtos = CourseToRestDtoConversor.toRestDtos(courses);

                ServletUtils.writeServiceResponse(
                        resp,
                        HttpServletResponse.SC_OK,
                        CourseJsonToRestDtoConversor.toArrayNode(courseRestDtos),
                        null
                );
            } else {
                Long courseId = Long.parseLong(path.substring(1));
                Course course = AppServiceFactory.getService().findCourse(courseId);
                CourseRestDto courseRestDto = CourseToRestDtoConversor.toRestDto(course);

                ServletUtils.writeServiceResponse(
                        resp,
                        HttpServletResponse.SC_OK,
                        CourseJsonToRestDtoConversor.toObjectNode(courseRestDto),
                        null
                );
            }
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_NOT_FOUND,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        } catch (InputValidationException e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        } catch (Exception e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        }
    }
}
