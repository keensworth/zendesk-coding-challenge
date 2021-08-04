package zendesk.api;

import zendesk.util.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TicketFetcher provides specific exposure to the ZendeskAPI
 * to allow the user to perform a variety of ticket-related
 * API requests. It also stores the page cursors for the next
 * and previous pages.
 */
public class TicketFetcher {
    private final ZendeskAPI zendeskAPI;
    private final UserFetcher userFetcher;
    private String nextTicketPageRequest;
    private String prevTicketPageRequest;
    private boolean hasMoreTickets;

    private static final String SINGLE_TICKET_REQUEST = "/api/v2/tickets/";
    private static final String INITIAL_TICKET_PAGE_REQUEST = "/api/v2/tickets.json?page[size]=25";

    /**
     * Constructor for TicketFetcher
     * @param zendeskAPI instance of the ZendeskAPI
     */
    public TicketFetcher(ZendeskAPI zendeskAPI){
        this.zendeskAPI = zendeskAPI;
        userFetcher = new UserFetcher(zendeskAPI);
    }

    /**
     * Fetches a ticket from Zendesk using the Zendesk API
     *
     * @param ticketId id of the ticket to retrieve
     * @return Ticket containing all relevant information returned to us after the API call
     */
    public Ticket fetchTicket(int ticketId){
        String response = zendeskAPI.makeGetRequest(SINGLE_TICKET_REQUEST + ticketId + ".json");

        if (response.equals("ex: FileNotFound")){
            return null;
        }

        Ticket ticket = JSONParser.parseTicketString(response);
        fetchUsernamesFromIds(ticket);
        return ticket;
    }

    /**
     * Fetches the first ticket(s) in the user's account (up to 25)
     *
     * @return Ticket[] containing up to 25 Ticket objects
     */
    public Ticket[] fetchInitialTicketPage(){
        String response = zendeskAPI.makeGetRequest(INITIAL_TICKET_PAGE_REQUEST);
        if (response.equals("ex: FileNotFound")){
            return null;
        }
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
    }

    /**
     * Fetches up to 25 Ticket objects, with respect to the current page
     * of tickets. Cursor pagination us proved by Zendesk, through this
     * command we are increasing the cursor by 25 tickets
     *
     * @return Ticket[] containing up to 25 Ticket objects
     */
    public Ticket[] fetchNextTicketPage(){
        String response = zendeskAPI.makeGetRequest(nextTicketPageRequest);
        if (response.equals("ex: FileNotFound")){
            return null;
        }
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
    }

    /**
     * Fetches up to 25 Ticket objects, with respect to the current page
     * of tickets. Cursor pagination us proved by Zendesk, through this
     * command we are decreasing the cursor by 25 tickets
     *
     * @return Ticket[] containing up to 25 Ticket objects
     */
    public Ticket[] fetchPrevTicketPage(){
        String response = zendeskAPI.makeGetRequest(prevTicketPageRequest);
        if (response.equals("ex: FileNotFound")){
            return null;
        }
        updateMeta(response);
        Ticket[] tickets = JSONParser.parseTicketArrayString(response);
        fetchUsernamesFromIds(tickets);
        return tickets;
    }

    /**
     * Checks the current hasMoreTickets state, which is set during meta updates. This
     * tells the user if there are more tickets beyond the current page during pagination
     *
     * @return boolean
     */
    public boolean hasMoreTickets(){
        return hasMoreTickets;
    }

    /**
     * Updates current metadata with respect to the most recently retrieved ticket page
     *
     * @param response JSON String response of an API request
     */
    private void updateMeta(String response){
        nextTicketPageRequest = JSONParser.getNextTicketPageRequest(response);
        prevTicketPageRequest = JSONParser.getPrevTicketPageRequest(response);
        hasMoreTickets = JSONParser.hasMoreTickets(response);
    }

    /**
     * Adds a requesterName to all tickets passed in, via an API request with respect to
     * all ticket requester_ids
     *
     * @param tickets Ticket[] to have their requesterName attributes filled, after
     *                an API call is made with respect to their requester_ids
     */
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
