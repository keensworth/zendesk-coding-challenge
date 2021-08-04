package zendesk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import zendesk.api.Ticket;

import java.util.HashMap;

/**
 * JSONParser is used to parse API responses into meaningful objects. There are methods
 * provided for a variety of use cases.
 */
public class JSONParser {

    /**
     * Parses a JSON Zendesk API response for a single ticket
     *
     * @param ticketString JSON Zendesk API Ticket response
     * @return Ticket object containing all data present on the ticketString
     */
    public static Ticket parseTicketString(String ticketString){
        ticketString = ticketString.substring(10, ticketString.length()-1);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            return objectMapper.readValue(ticketString, Ticket.class);
        } catch (JsonProcessingException e){
            return null;
        }
    }

    /**
     * Parses a JSON Zendesk API response for a page of tickets
     *
     * @param ticketArrayString JSON Zendesk API Ticket response
     * @return Ticket[] containing all tickets present in the response.
     */
    public static Ticket[] parseTicketArrayString(String ticketArrayString){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            JsonNode node = objectMapper.readTree(ticketArrayString).get("tickets");
            Ticket[] tickets = objectMapper.convertValue(node, Ticket[].class);
            return tickets;
        } catch (JsonProcessingException e){
            return null;
        }
    }

    /**
     * Retrieves the next page cursor from a ticket page response
     *
     * @param json JSON Zendesk API Ticket response
     * @return "next" URL for the next page
     */
    public static String getNextTicketPageRequest(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("links");
            jNode = jNode.get("next");
            return getURLPath(jNode.asText());
        } catch (JsonProcessingException e){
            return "NULL";
        }
    }

    /**
     * Retrieves the previous page cursor from a ticket page response
     *
     * @param json JSON Zendesk API Ticket response
     * @return "next" URL for the next page
     */
    public static String getPrevTicketPageRequest(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("links");
            jNode = jNode.get("prev");
            return getURLPath(jNode.asText());
        } catch (JsonProcessingException e){
            return "NULL";
        }
    }

    /**
     * Retrieves the "has_more" value from a ticket page response
     *
     * @param json JSON Zendesk API Ticket response
     * @return "has_more" value, to determine if there are any more pages
     */
    public static boolean hasMoreTickets(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("meta");
            jNode = jNode.get("has_more");
            return Boolean.parseBoolean(jNode.asText());
        } catch (JsonProcessingException e){
            return false;
        }
    }

    /**
     * Retrieves the "name" value from a Zendesk API response
     *
     * @param json JSON Zendesk API User response
     * @return "name" value
     */
    public static String parseUserStringForName(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            JsonNode jNode = node.get("user");
            jNode = jNode.get("name");
            return (jNode.asText());
        } catch (JsonProcessingException e){
            return "NULL";
        }
    }

    /**
     * Retrieves the "name" values from a Zendesk API response and pairs them with
     * the respective ids
     *
     * @param json JSON Zendesk API User response
     * @return HashMap containing all present pairs of names and ids
     */
    public static HashMap<Long,String> parseUserStringForNames(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            JsonNode node = objectMapper.readTree(json).get("users");

            HashMap<Long,String> nameIdPairs = new HashMap<>();

            for (JsonNode subNode : node){
                String name = subNode.get("name").asText();
                long id = subNode.get("id").asLong();
                nameIdPairs.put(id,name);
            }

            return nameIdPairs;
        } catch (JsonProcessingException e){
            return null;
        }
    }

    /**
     * Removes the host URL from a URL (so as to store the cursors)
     *
     * @param url URL to get the path of
     * @return path of a URL
     */
    private static String getURLPath(String url){
        String[] split = url.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 3; i < split.length; i++){
            stringBuilder.append("/").append(split[i]);
        }
        return stringBuilder.toString();
    }
}
