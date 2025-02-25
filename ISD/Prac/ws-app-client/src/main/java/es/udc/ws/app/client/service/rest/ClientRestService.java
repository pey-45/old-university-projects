
package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.ClientService;
import es.udc.ws.app.client.service.dto.ClientCourseDto;

import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.ClientJsonToCourseDtoConversor;
import es.udc.ws.app.client.service.rest.json.ClientJsonToExceptionConversor;
import es.udc.ws.app.client.service.rest.json.ClientJsonToRegistrationDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ClientRestService implements ClientService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ClientRestService.endpointAddress";

    private static final String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    private InputStream toInputStream(ClientCourseDto course) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    ClientJsonToCourseDtoConversor.toObjectNode(course));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {
        try {
            int statusCode = response.getCode();

            if (statusCode == successCode) {
                return;
            }

            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND ->
                        throw ClientJsonToExceptionConversor.fromNotFoundErrorCode(response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST ->
                        throw ClientJsonToExceptionConversor.fromBadRequestErrorCode(response.getEntity().getContent());
                case HttpStatus.SC_GONE ->
                        throw ClientJsonToExceptionConversor.fromGoneErrorCode(response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN ->
                        throw ClientJsonToExceptionConversor.fromForbiddenErrorCode(response.getEntity().getContent());
                default ->
                        throw new RuntimeException("HTTP error; status code = " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public ClientCourseDto addCourse(ClientCourseDto course) throws InputValidationException {

        try {
            System.out.println(endpointAddress + "courses");
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(endpointAddress + "courses")
                    .bodyStream(toInputStream(course), ContentType.create("application/json"))
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());

            return ClientJsonToCourseDtoConversor.toClientCourseDto(jsonResponse);

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientCourseDto> findCourses(String city) throws InputValidationException {
        try {
            if (city == null || city.isEmpty()) {
                throw new IllegalArgumentException("City cannot be null or empty");
            }

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(endpointAddress + "courses?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8))
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return ClientJsonToCourseDtoConversor.toClientCourseDtos(response.getEntity().getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientCourseDto findCourse(Long courseId) throws InstanceNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(endpointAddress + "courses/" + courseId)
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            ObjectMapper objectMapper = new ObjectMapper();
            return ClientJsonToCourseDtoConversor.toClientCourseDto(objectMapper.readTree(response.getEntity().getContent()));

        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientRegistrationDto registerForCourse(String email, Long courseId, String cardNumber) throws InputValidationException, InstanceNotFoundException, RegistrationOutOfDeadlineException, FullCourseException, EmailAlreadyRegisteredException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(endpointAddress +
                            "registrations?email=" + email + "&courseId=" + courseId + "&cardNumber=" + cardNumber)
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            ObjectMapper objectMapper = new ObjectMapper();
            return ClientJsonToRegistrationDtoConversor.toClientRegistrationDto(objectMapper.readTree(response.getEntity().getContent()));

        } catch (InputValidationException | InstanceNotFoundException | RegistrationOutOfDeadlineException | FullCourseException | EmailAlreadyRegisteredException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelRegistration(Long registrationId, String email) throws InstanceNotFoundException, CancellationEmailDoesNotMatchException, CancellationOutOfDeadlineException, RegistrationAlreadyCancelledException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(endpointAddress + "registrations/" + registrationId + "?email=" + email)
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_SUCCESS, response);

        } catch (InstanceNotFoundException | CancellationEmailDoesNotMatchException | CancellationOutOfDeadlineException | RegistrationAlreadyCancelledException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientRegistrationDto> findAllRegistrationsFrom(String email) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(endpointAddress + "registrations?email=" + email)
                    .execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_SUCCESS, response);

            return ClientJsonToRegistrationDtoConversor.toClientRegistrationDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}