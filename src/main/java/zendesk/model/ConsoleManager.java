package zendesk.model;

import zendesk.api.Ticket;
import zendesk.controller.ConsoleController;
import zendesk.api.ZendeskAPI;
import zendesk.view.Console;

public class ConsoleManager {
    private ConsoleController consoleController;
    private Console console;
    private TicketManager ticketManager;

    public static final int QUIT_STATE = 0x00000000;
    public static final int START_STATE = 0x00000001;
    public static final int BASIC_QUERY_STATE = 0x00000002;
    public static final int PAGE_QUERY_STATE = 0x00000003;
    public static final int AWAITING_TICKET_STATE = 0x00000004;
    public static final int FETCHING_TICKET_STATE = 0x00000005;
    public static final int AWAITING_PAGE_STATE = 0x00000006;
    public static final int AWAITING_NEXT_PAGE_STATE = 0x00000007;
    public static final int AWAITING_PREV_PAGE_STATE = 0x00000008;
    public static final int INVALID_BASIC_QUERY_INPUT_STATE = 0x00000009;
    public static final int INVALID_PAGE_QUERY_INPUT_STATE = 0x0000000A;
    public static final int INVALID_FIRST_PAGE_QUERY_INPUT_STATE = 0x0000000B;
    public static final int INVALID_LAST_PAGE_QUERY_INPUT_STATE = 0x0000000C;
    public static final int INVALID_ID_INPUT_STATE = 0x0000000D;
    public static final int FIRST_PAGE_QUERY_STATE = 0x0000000E;
    public static final int LAST_PAGE_QUERY_STATE = 0x0000000F;

    private int currentState = START_STATE;
    private int currentTicketId;

    public ConsoleManager(ZendeskAPI zendeskAPI){
        init(zendeskAPI);
    }

    public int assessState(){
        switch (currentState){
            case START_STATE -> onStartup();
            case BASIC_QUERY_STATE -> onBasicQuery();
            case PAGE_QUERY_STATE -> onPageQuery();
            case FIRST_PAGE_QUERY_STATE -> onFirstPageQuery();
            case LAST_PAGE_QUERY_STATE -> onLastPageQuery();
            case QUIT_STATE -> onExit();
            case AWAITING_TICKET_STATE -> getTicketByID();
            case FETCHING_TICKET_STATE -> fetchTicket();
            case AWAITING_PAGE_STATE -> viewAllTickets();
            case AWAITING_NEXT_PAGE_STATE -> nextPage();
            case AWAITING_PREV_PAGE_STATE -> prevPage();
            case INVALID_BASIC_QUERY_INPUT_STATE -> invalidBasicQueryInput();
            case INVALID_PAGE_QUERY_INPUT_STATE -> invalidPageQueryInput();
            case INVALID_FIRST_PAGE_QUERY_INPUT_STATE -> invalidFirstPageQueryInput();
            case INVALID_LAST_PAGE_QUERY_INPUT_STATE -> invalidLastPageQueryInput();
            case INVALID_ID_INPUT_STATE -> invalidIdInput();
        }
        return currentState;
    }

    public void setState(int state){
        currentState = state;
    }

    public int getState(){
        return currentState;
    }

    public void setTicketId(int ticketId){
        currentTicketId = ticketId;
    }

    private void onStartup(){
        console.printBanner();
        currentState = BASIC_QUERY_STATE;
    }

    private void onBasicQuery(){
        console.printBasicQuery();
        consoleController.promptUser("> ");
    }

    private void onPageQuery(){
        console.printPageQuery();
        consoleController.promptUser("> ");
    }

    private void onFirstPageQuery(){
        console.printFirstPageQuery();
        consoleController.promptUser("> ");
    }

    private void onLastPageQuery(){
        console.printLastPageQuery();
        consoleController.promptUser("> ");
    }

    private void onExit(){
        console.printExit();
    }

    public void getTicketByID(){
        consoleController.promptUser("Ticket ID: ");
    }

    public void fetchTicket(){
        Ticket ticket = ticketManager.getTicket(currentTicketId);
        console.printTicket(ticket);
        currentState = BASIC_QUERY_STATE;
    }

    public void viewAllTickets(){
        System.out.println("Getting tickets from API");
        Ticket[] tickets = ticketManager.viewAllTickets();
        System.out.println("Printing tickets");
        console.printTicketPage(tickets);
        System.out.println("Printed tickets");
        currentState = FIRST_PAGE_QUERY_STATE;
    }

    public void nextPage(){
        Ticket[] tickets = ticketManager.getNextPage();

        console.printTicketPage(tickets);

        if (ticketManager.hasMoreTickets())
            currentState = PAGE_QUERY_STATE;
        else
            currentState = LAST_PAGE_QUERY_STATE;

    }

    public void prevPage(){
        Ticket[] tickets = ticketManager.getPrevPage();

        console.printTicketPage(tickets);

        if (ticketManager.getPageNum() > 1)
            currentState = PAGE_QUERY_STATE;
        else
            currentState = FIRST_PAGE_QUERY_STATE;
    }

    public void invalidBasicQueryInput(){
        console.printWarn("Invalid input, please try again.");
        currentState = BASIC_QUERY_STATE;
    }

    public void invalidPageQueryInput(){
        console.printWarn("Invalid input, please try again.");
        currentState = PAGE_QUERY_STATE;
    }

    public void invalidFirstPageQueryInput(){
        console.printWarn("Invalid input, please try again.");
        currentState = FIRST_PAGE_QUERY_STATE;
    }

    public void invalidLastPageQueryInput(){
        console.printWarn("Invalid input, please try again.");
        currentState = LAST_PAGE_QUERY_STATE;
    }

    public void invalidIdInput(){
        console.printWarn("Invalid ticket ID, please try again.");
        currentState = AWAITING_TICKET_STATE;
    }

    private void init(ZendeskAPI zendeskAPI){
        this.consoleController = new ConsoleController(this);
        this.ticketManager = new TicketManager(zendeskAPI);
        this.console = new Console();
    }
}
