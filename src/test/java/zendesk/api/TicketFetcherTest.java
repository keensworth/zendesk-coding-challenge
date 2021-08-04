package zendesk.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zendesk.util.JSONParser;

import static org.junit.jupiter.api.Assertions.*;

class TicketFetcherTest {
    ZendeskAPI zendeskAPI;

    private static final String SINGLE_TICKET_REQUEST = "/api/v2/tickets/";
    private static final String INITIAL_TICKET_PAGE_REQUEST = "/api/v2/tickets.json?page[size]=25";

    @BeforeEach
    void setUp() {
        zendeskAPI = new ZendeskAPI();
        zendeskAPI.setCredentials("/config/properties");
    }

    @Test
    void fetchTicket_Retrieves_ticket() {
        int ticketId = 1;
        String response = zendeskAPI.makeGetRequest(SINGLE_TICKET_REQUEST + ticketId + ".json");
        Ticket ticket = JSONParser.parseTicketString(response);

        assertNotNull(ticket);
        assertEquals(1, ticket.id);
    }

    @Test
    void fetchTicket_Nonexistent_ticket_id() {
        int ticketId = 31415;
        String response = zendeskAPI.makeGetRequest(SINGLE_TICKET_REQUEST + ticketId + ".json");
        Ticket ticket = JSONParser.parseTicketString(response);

        assertNull(ticket);
    }

    @Test
    void fetchInitialTicketPage_Valid_input() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);

        Ticket[] tickets = JSONParser.parseTicketArrayString(response);

        assertNotNull(tickets);
        assertEquals(1, tickets[0].id);
        assertEquals(25, tickets[24].id);
    }

    @Test
    void fetchInitialTicketPage_Valid_length() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);

        Ticket[] tickets = JSONParser.parseTicketArrayString(response);

        assertNotNull(tickets);
        assertTrue(tickets.length <= 25);
    }

    @Test
    void fetchNextTicketPage_Valid_input() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        String nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);

        assertNotNull(tickets);
        assertEquals(26, tickets[0].id);
    }

    @Test
    void fetchPrevTicketPage_Invalid_input() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        String nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);

        Ticket[] ticketsOriginal = JSONParser.parseTicketArrayString(response);

        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        String prevTicketPageRequest = JSONParser.getPrevTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(prevTicketPageRequest);

        Ticket[] ticketsNew = JSONParser.parseTicketArrayString(response);

        assertEquals(ticketsOriginal, ticketsNew);
    }

    @Test
    void hasMoreTickets_True() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        boolean hasMoreTickets = JSONParser.hasMoreTickets(response);

        assertTrue(hasMoreTickets);
    }

    @Test
    void hasMoreTickets_False() {
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        String nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        response = zendeskAPI.makeGetRequest(nextTicketPageRequest);

        boolean hasMoreTickets = JSONParser.hasMoreTickets(response);

        assertFalse(hasMoreTickets);
    }
}