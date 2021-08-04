package zendesk.api;

import zendesk.util.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketFetcher {
    private final ZendeskAPI zendeskAPI;
    private final UserFetcher userFetcher;
    private String nextTicketPageRequest;
    private String prevTicketPageRequest;
    private boolean hasMoreTickets;

    private static final String SINGLE_TICKET_REQUEST = "/api/v2/tickets/";
    private static final String INITIAL_TICKET_PAGE_REQUEST = "/api/v2/tickets.json?page[size]=25";

    public TicketFetcher(ZendeskAPI zendeskAPI){
        this.zendeskAPI = zendeskAPI;
        userFetcher = new UserFetcher(zendeskAPI);
    }

    public Ticket fetchTicket(int ticketId){
        String response = zendeskAPI.makeGetRequest(SINGLE_TICKET_REQUEST + ticketId + ".json");
        Ticket ticket = JSONParser.parseTicketString(response);
        fetchUsernamesFromIds(ticket);
        return ticket;
    }

    public Ticket[] fetchInitialTicketPage(){
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
    }

    public Ticket[] fetchNextTicketPage(){
        String response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
    }

    public Ticket[] fetchPrevTicketPage(){
        String response = zendeskAPI.makeGetRequest(prevTicketPageRequest);
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
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

    private void updateMeta(String response){
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        prevTicketPageRequest = JSONParser.getPrevTicketPageRequest(response);
        hasMoreTickets = JSONParser.hasMoreTickets(response);
    }

    private void fetchUsernamesFromIds(Ticket... tickets){
        ArrayList<Long> requesterIds = new ArrayList<>();
        for (Ticket ticket : tickets){
            requesterIds.add(ticket.requester_id);
        }
        HashMap<Long,String> requesterNames = userFetcher.fetchNamesFromIds(requesterIds);
        for (int i = 0; i < tickets.length; i++){
            Ticket ticket = tickets[i];
            ticket.requesterName = requesterNames.get(ticket.requester_id);
        }
    }
}
