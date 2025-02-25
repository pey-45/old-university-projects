
package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.app.client.service.dto.ClientRegistrationDto;
import es.udc.ws.util.json.ObjectMapperFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientJsonToRegistrationDtoConversor {

    public static ObjectNode toObjectNode(ClientRegistrationDto registration) throws IOException {

        ObjectNode registrationObject = JsonNodeFactory.instance.objectNode();

        if (registration.getCourseId() != null) {
            registrationObject.put("registrationId", registration.getRegistrationId());
        }
        registrationObject.put("courseId", registration.getCourseId())
                .put("email", registration.getEmail())
                .put("cardNumber", registration.getCardNumber())
                .put("registrationDate", registration.getRegistrationDate());
        if (registration.getCancellationDate() != null) {
            registrationObject.put("cancellationDate", registration.getCancellationDate());
        }

        return registrationObject;
    }

    public static List<ClientRegistrationDto> toClientRegistrationDtos(InputStream json) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.instance();
        ArrayNode registrationsArray = (ArrayNode) objectMapper.readTree(json);
        List<ClientRegistrationDto> registrationDtos = new ArrayList<>();

        for (JsonNode registrationNode : registrationsArray) {
            registrationDtos.add(toClientRegistrationDto(registrationNode));
        }
        return registrationDtos;
    }

    public static ClientRegistrationDto toClientRegistrationDto(JsonNode registrationNode) {
        return new ClientRegistrationDto(
                registrationNode.get("registrationId").longValue(),
                registrationNode.get("courseId").longValue(),
                registrationNode.get("email").textValue(),
                registrationNode.get("cardNumber").textValue(),
                registrationNode.get("registrationDate").textValue(),
                registrationNode.has("cancellationDate") && !registrationNode.get("cancellationDate").isNull()
                        ? registrationNode.get("cancellationDate").textValue()
                        : null
        );
    }
}
