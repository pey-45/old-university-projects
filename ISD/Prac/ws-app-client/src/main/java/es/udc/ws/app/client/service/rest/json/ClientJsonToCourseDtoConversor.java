
package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCourseDto;
import es.udc.ws.util.json.ObjectMapperFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientJsonToCourseDtoConversor {

    public static ObjectNode toObjectNode(ClientCourseDto course) throws IOException {

        ObjectNode courseObject = JsonNodeFactory.instance.objectNode();

        if (course.getCourseId() != null) {
            courseObject.put("courseId", course.getCourseId());
        }
        courseObject.put("city", course.getCity())
                .put("name", course.getName())
                .put("startDate", course.getStartDate())
                .put("price", course.getPrice())
                .put("maxPlaces", course.getMaxPlaces())
                .put("availablePlaces", course.getAvailablePlaces());

        return courseObject;
    }

    public static List<ClientCourseDto> toClientCourseDtos(InputStream json) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.instance();
        ArrayNode coursesArray = (ArrayNode) objectMapper.readTree(json);
        List<ClientCourseDto> courseDtos = new ArrayList<>();

        for (JsonNode courseNode : coursesArray) {
            courseDtos.add(toClientCourseDto(courseNode));
        }
        return courseDtos;
    }

    public static ClientCourseDto toClientCourseDto(JsonNode courseNode) {
        return new ClientCourseDto(
                courseNode.get("courseId").longValue(),
                courseNode.get("city").textValue(),
                courseNode.get("name").textValue(),
                courseNode.get("startDate").textValue(),
                courseNode.get("price").doubleValue(),
                courseNode.get("maxPlaces").intValue(),
                courseNode.get("availablePlaces").intValue()
        );
    }
}
