package zendesk.controller;

import zendesk.model.ConsoleManager;

import java.io.Console;

public class ConsoleController {
    private final ConsoleManager consoleManager;

    public ConsoleController(ConsoleManager consoleManager){
        this.consoleManager = consoleManager;
    }

    public void promptUser(String prompt){
        int consoleManagerState = consoleManager.getState();
        Console cnsl = null;
        String input = null;
        try {
            cnsl = System.console();

            input = cnsl.readLine(prompt);
            input = input.toLowerCase();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        parseInput(input, consoleManagerState);
    }

    private void parseInput(String input, int consoleManagerState){
        try {
            int i = Integer.parseInt(input);
            parseNumericInput(i, consoleManagerState);
        } catch (NumberFormatException e){
            parseTextInput(input, consoleManagerState);
        }
    }

    private void parseTextInput(String input, int consoleManagerState){
        if (consoleManagerState == ConsoleManager.BASIC_QUERY_STATE){
            switch (input) {
                case "t" -> consoleManager.setState(ConsoleManager.AWAITING_TICKET_STATE);//getTicketByID
                case "a" -> consoleManager.setState(ConsoleManager.AWAITING_PAGE_STATE);//viewAllTickets
                case "q" -> consoleManager.setState(ConsoleManager.QUIT_STATE);//Quit
                default -> consoleManager.setState(ConsoleManager.INVALID_BASIC_QUERY_INPUT_STATE);//InvalidInput
            }
        } else if (consoleManagerState == ConsoleManager.PAGE_QUERY_STATE){
            switch (input){
                case "t" -> consoleManager.setState(ConsoleManager.AWAITING_TICKET_STATE);//getTicketByID
                case "a" -> consoleManager.setState(ConsoleManager.AWAITING_PAGE_STATE);//viewAllTickets
                case "q" -> consoleManager.setState(ConsoleManager.QUIT_STATE);//Quit
                case "n" -> consoleManager.setState(ConsoleManager.AWAITING_NEXT_PAGE_STATE);//nextPage
                case "p" -> consoleManager.setState(ConsoleManager.AWAITING_PREV_PAGE_STATE);//prevPage
                default -> consoleManager.setState(ConsoleManager.INVALID_PAGE_QUERY_INPUT_STATE);//InvalidInput
            }
        } else if (consoleManagerState == ConsoleManager.FIRST_PAGE_QUERY_STATE){
            switch (input){
                case "t" -> consoleManager.setState(ConsoleManager.AWAITING_TICKET_STATE);//getTicketByID
                case "a" -> consoleManager.setState(ConsoleManager.AWAITING_PAGE_STATE);//viewAllTickets
                case "q" -> consoleManager.setState(ConsoleManager.QUIT_STATE);//Quit
                case "n" -> consoleManager.setState(ConsoleManager.AWAITING_NEXT_PAGE_STATE);//nextPage
                default -> consoleManager.setState(ConsoleManager.INVALID_FIRST_PAGE_QUERY_INPUT_STATE);//InvalidInput
            }
        } else if (consoleManagerState == ConsoleManager.LAST_PAGE_QUERY_STATE){
            switch (input){
                case "t" -> consoleManager.setState(ConsoleManager.AWAITING_TICKET_STATE);//getTicketByID
                case "a" -> consoleManager.setState(ConsoleManager.AWAITING_PAGE_STATE);//viewAllTickets
                case "q" -> consoleManager.setState(ConsoleManager.QUIT_STATE);//Quit
                case "p" -> consoleManager.setState(ConsoleManager.AWAITING_PREV_PAGE_STATE);//prevPage
                default -> consoleManager.setState(ConsoleManager.INVALID_LAST_PAGE_QUERY_INPUT_STATE);//InvalidInput
            }
        }
    }

    private void parseNumericInput(int num, int consoleManagerState){
        if (consoleManagerState == ConsoleManager.AWAITING_TICKET_STATE) {
            consoleManager.setTicketId(num);
            consoleManager.setState(ConsoleManager.FETCHING_TICKET_STATE);//fetchTicket
        } else {
            consoleManager.setState(ConsoleManager.INVALID_ID_INPUT_STATE);
        }
    }
}
