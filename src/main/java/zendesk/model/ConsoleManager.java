package zendesk.model;

import zendesk.api.Ticket;
import zendesk.controller.ConsoleController;
import zendesk.api.ZendeskAPI;
import zendesk.view.Console;

/**
 * ConsoleManager manages the overall state of the program. It calls upon ConsoleController for
 * input, recieves state changes from ConsoleController, and prompts visual changes to the Console
 * (such as printing error messages, warning messages, user prompts, queries...)
 */
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

    /**
     * Constructor for ConsoleManager
     *
     * @param zendeskAPI instance of the ZendeskAPI
     */
    public ConsoleManager(ZendeskAPI zendeskAPI){
        init(zendeskAPI);
    }

    /**
     * Assesses the current state of ConsoleManager and performs the relevant actions
     *
     * @return the current state, which is used by Application to determine when to stop the program
     */
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

    /**
     * Sets the state
     *
     * @param state new state
     */
    public void setState(int state){
        currentState = state;
    }

    /**
     * Gets the state
     *
     * @return current state
     */
    public int getState(){
        return currentState;
    }

    /**
     * Sets the next ticketId to retrieve
     *
     * @param ticketId ticket id to retrieve
     */
    public void setTicketId(int ticketId){
        currentTicketId = ticketId;
    }

    /**
     * Startup method, prints the banner and establishes the state
     */
    private void onStartup(){
        console.printBanner();
        currentState = BASIC_QUERY_STATE;
    }

    /**
     * Prints the basic query and prompts the user for input. This input is
     * handled by ConsoleController.
     */
    private void onBasicQuery(){
        console.printBasicQuery();
        consoleController.promptUser("> ");
    }

    /**
     * Prints the page query and prompts the user for input. This input is
     * handled by ConsoleController.
     */
    private void onPageQuery(){
        console.printPageQuery();
        consoleController.promptUser("> ");
    }

    /**
     * Prints the first page query (no previous page option) and prompts the
     * user for input. This input is handled by ConsoleController.
     */
    private void onFirstPageQuery(){
        console.printFirstPageQuery();
        consoleController.promptUser("> ");
    }

    /**
     * Prints the last page query (no next page option) and prompts the
     * user for input. This input is handled by ConsoleController.
     */
    private void onLastPageQuery(){
        console.printLastPageQuery();
        consoleController.promptUser("> ");
    }

    /**
     * Method called on exit
     */
    private void onExit(){
        console.printExit();
    }

    /**
     * Prompts the user to enter a ticket ID
     */
    public void getTicketByID(){
        consoleController.promptUser("Ticket ID: ");
    }

    /**
     * Fetches the ticket with id currentTicketId from Zendesk and prints the full
     * ticket details to the user
     */
    public void fetchTicket(){
        Ticket ticket = ticketManager.getTicket(currentTicketId);
        if (ticket == null){
            console.printError("Ticket does not exist");
        } else {
            console.printTicket(ticket);
        }
        currentState = BASIC_QUERY_STATE;
    }

    /**
     * Fetches the initial page of tickets from Zendesk and prints the page to the console.
     */
    public void viewAllTickets(){
        Ticket[] tickets = ticketManager.viewAllTickets();
        if (tickets==null){
            console.printError("Error connecting to the ZendeskAPI");
        } else {
            console.printTicketPage(tickets);
            currentState = FIRST_PAGE_QUERY_STATE;
        }
    }

    /**
     * Fetches the next page of tickets from Zendesk with respect to the current page,
     * and prints the page to the console.
     */
    public void nextPage(){
        Ticket[] tickets = ticketManager.getNextPage();

        if (tickets==null){
            console.printError("Error connecting to the ZendeskAPI");
        } else {
            console.printTicketPage(tickets);

            if (ticketManager.hasMoreTickets())
                currentState = PAGE_QUERY_STATE;
            else
                currentState = LAST_PAGE_QUERY_STATE;
        }
    }

    /**
     * Fetches the previous page of tickets from Zendesk with respect to the current page,
     * and prints the page to the console.
     */
    public void prevPage(){
        Ticket[] tickets = ticketManager.getPrevPage();

        if (tickets==null){
            console.printError("Error connecting to the ZendeskAPI");
        } else {
            console.printTicketPage(tickets);

            if (ticketManager.getPageNum() > 1)
                currentState = PAGE_QUERY_STATE;
            else
                currentState = FIRST_PAGE_QUERY_STATE;
        }
    }

    // The following methods are called upon by ConsoleController when an invalid input
    // is entered.
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
