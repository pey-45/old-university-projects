package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RegistrationRestDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RegistrationJsonToRestDtoConversor {

    public static ObjectNode toObjectNode(RegistrationRestDto registration) {

        ObjectNode registrationNode = JsonNodeFactory.instance.objectNode();

        if (registration.getRegistrationId() != null) {
            registrationNode.put("registrationId", registration.getRegistrationId());
        }

        registrationNode.put("courseId", registration.getCourseId())
                .put("email", registration.getEmail())
                .put("cardNumber", registration.getCardNumber())
                .put("registrationDate", registration.getRegistrationDate().toString());

        // Manejo de null en cancellationDate
        if (registration.getCancellationDate() != null) {
            registrationNode.put("cancellationDate", registration.getCancellationDate().toString());
        } else {
            registrationNode.putNull("cancellationDate");
        }

        return registrationNode;
    }

    public static ArrayNode toArrayNode(List<RegistrationRestDto> registrations) {

        ArrayNode registrationsNode = JsonNodeFactory.instance.arrayNode();
        for (RegistrationRestDto registrationRestDto : registrations) {
            ObjectNode registrationObject = toObjectNode(registrationRestDto);
            registrationsNode.add(registrationObject);
        }

        return registrationsNode;
    }

    public static RegistrationRestDto toRestDto(InputStream jsonRegistration) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRegistration);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode registrationObject = (ObjectNode) rootNode;

                JsonNode registrationIdNode = registrationObject.get("registrationId");
                Long registrationId = (registrationIdNode != null) ? registrationIdNode.longValue() : null;

                Long courseId = registrationObject.get("courseId").longValue();
                String email = registrationObject.get("email").textValue();
                String cardNumber = registrationObject.get("cardNumber").textValue();
                LocalDateTime registrationDate = LocalDateTime.parse(registrationObject.get("registrationDate").asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // Manejo de null en cancellationDate
                JsonNode cancellationDateNode = registrationObject.get("cancellationDate");
                LocalDateTime cancellationDate = (cancellationDateNode != null && !cancellationDateNode.isNull())
                        ? LocalDateTime.parse(cancellationDateNode.asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null;

                return new RegistrationRestDto(registrationId, courseId, email, cardNumber, registrationDate, cancellationDate);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
