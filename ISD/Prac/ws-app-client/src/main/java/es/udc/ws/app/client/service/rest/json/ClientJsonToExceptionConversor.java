package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;

public class ClientJsonToExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }


    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }


    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                return switch (errorType) {
                    case "RegistrationOutOfDeadline" -> toRegistrationOutOfDeadlineException(rootNode);
                    case "CancellationOutOfDeadline" -> toCancellationOutOfDeadlineException(rootNode);
                    default -> throw new ParsingException("Unrecognized error type: " + errorType);
                };
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static RegistrationOutOfDeadlineException toRegistrationOutOfDeadlineException(JsonNode rootNode) {
        Long courseId = rootNode.get("courseId").longValue();
        String startDate = rootNode.get("startDate").textValue();
        return new RegistrationOutOfDeadlineException(courseId, LocalDateTime.parse(startDate));
    }

    private static CancellationOutOfDeadlineException toCancellationOutOfDeadlineException(JsonNode rootNode) {
        Long registrationId = rootNode.get("registrationId").longValue();
        return new CancellationOutOfDeadlineException(registrationId);
    }


    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                return switch (errorType) {
                    case "FullCourse" -> toFullCourseException(rootNode);
                    case "EmailAlreadyRegistered" -> toEmailAlreadyRegisteredException(rootNode);
                    case "CancellationEmailDoesNotMatch" -> toCancellationEmailDoesNotMatchException(rootNode);
                    case "RegistrationAlreadyCancelled" -> toRegistrationAlreadyCancelledException(rootNode);
                    default -> throw new ParsingException("Unrecognized error type: " + errorType);
                };
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static FullCourseException toFullCourseException(JsonNode rootNode) {
        Long courseId = rootNode.get("courseId").longValue();
        return new FullCourseException(courseId);
    }

    private static EmailAlreadyRegisteredException toEmailAlreadyRegisteredException(JsonNode rootNode) {
        Long courseId = rootNode.get("courseId").longValue();
        String email = rootNode.get("email").textValue();
        return new EmailAlreadyRegisteredException(courseId, email);
    }

    private static RegistrationAlreadyCancelledException toRegistrationAlreadyCancelledException(JsonNode rootNode) {
        Long registrationId = rootNode.get("registrationId").longValue();
        return new RegistrationAlreadyCancelledException(registrationId);
    }

    private static CancellationEmailDoesNotMatchException toCancellationEmailDoesNotMatchException(JsonNode rootNode) {
        Long registrationId = rootNode.get("registrationId").longValue();
        String email = rootNode.get("email").textValue();
        return new CancellationEmailDoesNotMatchException(registrationId, email);
    }
}