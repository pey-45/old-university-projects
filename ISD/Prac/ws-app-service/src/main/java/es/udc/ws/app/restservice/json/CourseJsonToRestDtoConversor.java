package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import es.udc.ws.app.restservice.dto.CourseRestDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class CourseJsonToRestDtoConversor {
    public static ObjectNode toObjectNode(CourseRestDto course) {

        ObjectNode courseObject = JsonNodeFactory.instance.objectNode();

        courseObject.put("courseId", course.getCourseId()).
                put("city", course.getCity()).
                put("name", course.getName()).
                put("startDate", course.getStartDate().toString()).
                put("price", course.getPrice()).
                put("maxPlaces", course.getMaxPlaces()).
                put("availablePlaces", course.getAvailablePlaces());

        return courseObject;
    }

    public static ArrayNode toArrayNode(List<CourseRestDto> courses) {

        ArrayNode coursesNode = JsonNodeFactory.instance.arrayNode();
        for (CourseRestDto courseDto : courses) {
            ObjectNode courseObject = toObjectNode(courseDto);
            coursesNode.add(courseObject);
        }

        return coursesNode;
    }

    public static CourseRestDto toRestDto(InputStream jsonCourse) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCourse);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode courseObject = (ObjectNode) rootNode;

                JsonNode courseIdNode = courseObject.get("courseId");
                Long courseId = (courseIdNode != null) ? courseIdNode.longValue() : null;

                String city = courseObject.get("city").textValue().trim();
                String name = courseObject.get("name").textValue().trim();
                LocalDateTime startDate = LocalDateTime.parse(courseObject.get("startDate").asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                double price = courseObject.get("price").doubleValue();
                int maxPlaces = courseObject.get("maxPlaces").intValue();
                int availablePlaces = courseObject.get("availablePlaces").intValue();

                return new CourseRestDto(courseId, city, name, startDate, price, maxPlaces, availablePlaces);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}