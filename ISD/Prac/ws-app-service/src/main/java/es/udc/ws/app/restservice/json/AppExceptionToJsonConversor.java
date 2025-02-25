package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AppExceptionToJsonConversor {
    public static ObjectNode toJson(Exception ex) {
        ObjectNode exceptionNode = JsonNodeFactory.instance.objectNode();

        switch (ex) {
            case InstanceNotFoundException e -> {
                exceptionNode.put("errorType", "InstanceNotFound");
                exceptionNode.put("instanceId", e.getInstanceId().toString());
                exceptionNode.put("instanceType", e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
            }
            case InputValidationException e -> {
                exceptionNode.put("errorType", "InputValidation");
                exceptionNode.put("message", e.getMessage());
            }
            case FullCourseException e -> {
                exceptionNode.put("errorType", "FullCourse");
                exceptionNode.put("courseId", e.getCourseId().toString());
            }
            case RegistrationOutOfDeadlineException e -> {
                exceptionNode.put("errorType", "RegistrationOutOfDeadline");
                exceptionNode.put("courseId", e.getCourseId());
                exceptionNode.put("startDate", e.getStartDate().toString());
            }
            case EmailAlreadyRegisteredException e -> {
                exceptionNode.put("errorType", "EmailAlreadyRegistered");
                exceptionNode.put("courseId", e.getCourseId());
                exceptionNode.put("email", e.getEmail());
            }
            case CancellationEmailDoesNotMatchException e -> {
                exceptionNode.put("errorType", "CancellationEmailDoesNotMatch");
                exceptionNode.put("registrationId", e.getRegistrationId());
                exceptionNode.put("email", e.getEmail());
            }
            case CancellationOutOfDeadlineException e -> {
                exceptionNode.put("errorType", "CancellationOutOfDeadline");
                exceptionNode.put("registrationId", e.getRegistrationId());
            }
            case RegistrationAlreadyCancelledException e -> {
                exceptionNode.put("errorType", "RegistrationAlreadyCancelled");
                exceptionNode.put("registrationId", e.getRegistrationId());
            }
            case null, default -> {
                exceptionNode.put("errorType", "UnknownException");
                if (ex != null) {
                    exceptionNode.put("message", "An unknown error occurred: " + ex.getMessage());
                } else {
                    exceptionNode.put("message", "An unknown error occurred.");
                }
            }
        }

        return exceptionNode;
    }
}
