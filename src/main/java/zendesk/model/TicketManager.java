package zendesk.model;

import zendesk.api.Ticket;
import zendesk.api.TicketFetcher;
import zendesk.api.ZendeskAPI;

/**
 * TicketManager provides high level access to the Zendesk API and TicketFetcher.
 * It stores the most recent Ticket and Ticket[] page fetches, as well as keeps track
 * of the current state. TicketFetcher is used to store API relevant information
 * (such as page cursors, and API request commands).
 */
public class TicketManager {
    private final TicketFetcher ticketFetcher;
    private Ticket[] currentTicketPage;
    private Ticket currentTicket;
    private int pageNum;

    /**
     * Constructor for TicketManager
     *
     * @param zendeskAPI instance of ZendeskAPI
     */
    public TicketManager(ZendeskAPI zendeskAPI){
        this.ticketFetcher = new TicketFetcher(zendeskAPI);
        pageNum = 1;
    }

    /**
     * Retrieves a ticket from the users account, from a provided id
     *
     * @param ticketId id of ticket to retrieve
     * @return Ticket object
     */
    public Ticket getTicket(int ticketId){
        Ticket ticket = ticketFetcher.fetchTicket(ticketId);
        currentTicket = ticket;
        return ticket;
    }

    /**
     * Retrieves the initial page of tickets from the user's account
     *
     * @return Ticket[] containing the initial page of tickets
     */
    public Ticket[] viewAllTickets(){
        Ticket[] tickets = ticketFetcher.fetchInitialTicketPage();
        currentTicketPage = tickets;
        return tickets;
    }

    /**
     * Retrieves the next page of tickets from the user's account, with
     * respect to the current page. This will only occur if we are not
     * currently on the last page.
     *
     * @return Ticket[] containing the next page of tickets
     */
    public Ticket[] getNextPage(){
        if (ticketFetcher.hasMoreTickets()) {
            Ticket[] tickets = ticketFetcher.fetchNextTicketPage();
            currentTicketPage = tickets;
            pageNum++;
            return tickets;
        }
        return null;
    }

    /**
     * Retrieves the previous page of tickets from the user's account, with
     * respect to the current page. This will only occur if we are not
     * currently on the first page.
     *
     * @return
     */
    public Ticket[] getPrevPage(){
        if (pageNum > 1){
            Ticket[] tickets = ticketFetcher.fetchPrevTicketPage();
            currentTicketPage = tickets;
            pageNum--;
            return tickets;
        }
        return null;
    }

    /**
     * Calls upon TicketFetcher to see if there is another page of tickets available
     *
     * @return
     */
    public boolean hasMoreTickets(){
        return ticketFetcher.hasMoreTickets();
    }

    /**
     * Retrieve the current page of tickets (most recently fetched).=
     *
     * @return Ticket[]
     */
    public Ticket[] getCurrentTicketPage(){
        return currentTicketPage;
    }

    /**
     * Retrieve the current ticket (most recently retrieved)
     *
     * @return Ticket
     */
    public Ticket getCurrentTicket(){
        return currentTicket;
    }

    /**
     * Retrieves the current page number
     *
     * @return page number
     */
    public int getPageNum(){
        return pageNum;
    }
}
