package zendesk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import zendesk.api.Ticket;

import java.util.List;

public class JSONParser {

    public static Ticket parseTicketString(String ticketString){
        ticketString = ticketString.substring(10, ticketString.length()-1);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            return objectMapper.readValue(ticketString, Ticket.class);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Ticket[] parseTicketArrayString(String ticketArrayString){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            List<Ticket> ticketList = objectMapper.readValue(ticketArrayString, new TypeReference<>() {
            });
            Ticket[] ticketArray = new Ticket[ticketList.size()];
            return ticketList.toArray(ticketArray);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextTicketPageRequest(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("links");
            jNode = jNode.get("next");
            return getURLPath(jNode.asText());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String getPrevTicketPageRequest(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("links");
            jNode = jNode.get("prev");
            return getURLPath(jNode.asText());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "NULL";
    }

    public static boolean hasMoreTickets(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("meta");
            jNode = jNode.get("has_more");
            return Boolean.parseBoolean(jNode.asText());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return false;
    }

    public static String parseUserStringForName(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("user");
            jNode = jNode.get("name");
            return (jNode.asText());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "NULL";
    }

    private static String getURLPath(String url){
        String[] split = url.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 3; i < split.length; i++){
            stringBuilder.append("/").append(split[i]);
        }
        return stringBuilder.toString();
    }
}
