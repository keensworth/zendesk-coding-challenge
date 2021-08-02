package zendesk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zendesk.model.Ticket;

import java.util.List;

public class JSONParser {

    public static Ticket parseTicketString(String ticketString){
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
            return (Ticket[]) ticketList.toArray();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextTicketPageRequest(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode array = objectMapper.readValue(json, JsonNode.class);
            JsonNode object = array.get(1);
            String nextURL = object.get("next").textValue();
            return getURLPath(nextURL);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getPrevTicketPageRequest(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode array = objectMapper.readValue(json, JsonNode.class);
            JsonNode object = array.get(1);
            String prevURL = object.get("prev").textValue();
            return getURLPath(prevURL);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasMoreTickets(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode array = objectMapper.readValue(json, JsonNode.class);
            JsonNode object = array.get(1);
            return object.get("has_more").booleanValue();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return false;
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
