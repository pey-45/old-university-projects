package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.registration.Registration;
import es.udc.ws.app.restservice.dto.CourseRestDto;
import es.udc.ws.app.restservice.dto.CourseToRestDtoConversor;
import es.udc.ws.app.restservice.dto.RegistrationRestDto;
import es.udc.ws.app.restservice.dto.RegistrationToRestDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.CourseJsonToRestDtoConversor;
import es.udc.ws.app.restservice.json.RegistrationJsonToRestDtoConversor;
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

public class RegistrationsServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = ServletUtils.normalizePath(req.getPathInfo());

            if (path == null || path.isEmpty()) {
                String email = ServletUtils.getMandatoryParameter(req, "email");
                Long courseId = ServletUtils.getMandatoryParameterAsLong(req, "courseId");
                String cardNumber = ServletUtils.getMandatoryParameter(req, "cardNumber");

                Registration registration = AppServiceFactory.getService().registerForCourse(email, courseId, cardNumber);
                RegistrationRestDto registrationRestDto = RegistrationToRestDtoConversor.toRestDto(registration);

                ServletUtils.writeServiceResponse(
                        resp,
                        HttpServletResponse.SC_CREATED,
                        RegistrationJsonToRestDtoConversor.toObjectNode(registrationRestDto),
                        null
                );
            } else {
                Long registrationId = ServletUtils.getIdFromPath(req, "registrationId");
                String email = ServletUtils.getMandatoryParameter(req, "email");
                AppServiceFactory.getService().cancelRegistration(registrationId, email);

                ServletUtils.writeServiceResponse(
                        resp,
                        HttpServletResponse.SC_OK,
                        null,
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
        } catch (RegistrationOutOfDeadlineException | CancellationOutOfDeadlineException e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_GONE,
                    AppExceptionToJsonConversor.toJson(e),
                    null
            );
        } catch (FullCourseException | EmailAlreadyRegisteredException | CancellationEmailDoesNotMatchException | RegistrationAlreadyCancelledException e) {
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_FORBIDDEN,
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
            // Leer parámetros
            String email = ServletUtils.getMandatoryParameter(req, "email");
            List<Registration> registrations = AppServiceFactory.getService().findAllRegistrationsFrom(email);

            // Convertir a DTO y enviar respuesta
            List<RegistrationRestDto> registrationRestDtos = RegistrationToRestDtoConversor.toRestDtos(registrations);
            ServletUtils.writeServiceResponse(
                    resp,
                    HttpServletResponse.SC_OK,
                    RegistrationJsonToRestDtoConversor.toArrayNode(registrationRestDtos),
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
