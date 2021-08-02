package zendesk.model;

public class TicketManager {
    private TicketFetcher ticketFetcher;
    private Ticket[] currentTicketPage;
    private Ticket currentTicket;
    private int pageNum;

    public TicketManager(){
        pageNum = 1;
    }

    public Ticket getTicket(int ticketId){
        Ticket ticket = ticketFetcher.fetchTicket(ticketId);
        currentTicket = ticket;
        return ticket;
    }

    public Ticket[] viewAllTickets(){
        Ticket[] tickets = ticketFetcher.fetchInitialTicketPage();
        currentTicketPage = tickets;
        return tickets;
    }

    public Ticket[] getNextPage(){
        if (ticketFetcher.hasMoreTickets()) {
            Ticket[] tickets = ticketFetcher.fetchNextTicketPage();
            currentTicketPage = tickets;
            pageNum++;
            return tickets;
        }
        return null;
    }

    public Ticket[] getPrevPage(){
        if (pageNum > 1){
            Ticket[] tickets = ticketFetcher.fetchPrevTicketPage();
            currentTicketPage = tickets;
            pageNum--;
            return tickets;
        }
        return null;
    }

    public Ticket[] getCurrentTicketPage(){
        return currentTicketPage;
    }

    public Ticket getCurrentTicket(){
        return currentTicket;
    }

    public int getPageNum(){
        return pageNum;
    }
}
