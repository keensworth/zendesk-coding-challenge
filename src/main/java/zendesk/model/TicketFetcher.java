package zendesk.model;

import zendesk.util.JSONParser;
import zendesk.util.ZendeskAPI;

public class TicketFetcher {
    private final ZendeskAPI zendeskAPI;
    private String nextTicketPageRequest;
    private String prevTicketPageRequest;
    private boolean hasMoreTickets;

    private static final String SINGLE_TICKET_REQUEST = "/api/v2/tickets/";
    private static final String INITIAL_TICKET_PAGE_REQUEST = "/api/v2/tickets.json?page[size]=25";

    public TicketFetcher(ZendeskAPI zendeskAPI){
        this.zendeskAPI = zendeskAPI;
    }

    public Ticket fetchTicket(int ticketId){
        String response = zendeskAPI.makeGetRequest(SINGLE_TICKET_REQUEST + ticketId + ".json");
        return JSONParser.parseTicketString(response);
    }

    public Ticket[] fetchInitialTicketPage(){
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        updateCursors(response);
        return JSONParser.parseTicketArrayString(response);
    }

    public Ticket[] fetchNextTicketPage(){
        String response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        updateCursors(response);
        hasMoreTickets = JSONParser.hasMoreTickets(response);
        return JSONParser.parseTicketArrayString(response);
    }

    public Ticket[] fetchPrevTicketPage(){
        String response = zendeskAPI.makeGetRequest(prevTicketPageRequest);
        updateCursors(response);
        return JSONParser.parseTicketArrayString(response);
    }

    public String getNextTicketPageRequest(){
        return nextTicketPageRequest;
    }

    public String getPrevTicketPageRequest(){
        return prevTicketPageRequest;
    }

    public boolean hasMoreTickets(){
        return hasMoreTickets;
    }

    private void updateCursors(String response){
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        prevTicketPageRequest = JSONParser.getPrevTicketPageRequest(response);
    }
}
