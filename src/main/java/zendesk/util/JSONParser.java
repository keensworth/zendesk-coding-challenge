package zendesk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import zendesk.model.Ticket;

import java.util.List;

public class JSONParser {
    public static Ticket[] parseTicketArrayString(String ticketArrayString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ticket> ticketList = objectMapper.readValue(ticketArrayString, new TypeReference<>() {});
        return (Ticket[]) ticketList.toArray();
    }

    public static Ticket parseTicketString(String ticketString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(ticketString, Ticket.class);
    }
}
