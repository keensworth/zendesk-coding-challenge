package zendesk.view;

import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import zendesk.api.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Provides access to the console to present information to the user, including prompts,
 * tables, queries, warnings, and errors
 */
public class Console {
    static String[] banner = new String[]{
            "",
            "",
            "        ______     ______     __   __     ______   __     ______     __  __     ______     ______        ",
            "       /\\___  \\   /\\  ___\\   /\\ '-.\\ \\   /\\__  _\\ /\\ \\   /\\  ___\\   /\\ \\/ /    /\\  ___\\   /\\__  _\\                ",
            "       \\/_/  /__  \\ \\  __\\   \\ \\ \\-.  \\  \\/_/\\ \\/ \\ \\ \\  \\ \\ \\____  \\ \\  _'-.  \\ \\  __\\   \\/_/\\ \\/          ",
            "         /\\_____\\  \\ \\_____\\  \\ \\_\\\\'\\_\\    \\ \\_\\  \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\    \\ \\_\\    ",
            "         \\/_____/   \\/_____/   \\/_/ \\/_/     \\/_/   \\/_/   \\/_____/   \\/_/\\/_/   \\/_____/     \\/_/                        ",
            "",
            "",
            "",
            ""
    };

    public Console(){
        AnsiConsole.systemInstall();
    }

    /**
     * Prints the ZenTicket banner
     */
    public void printBanner(){
        System.out.println(ansi().eraseScreen());
        System.out.println(ansi().fg(Ansi.Color.GREEN));
        for (String line : banner){
            System.out.println(ansi().a(line));
        }
        System.out.println(ansi().reset());
    }

    /**
     * Prints a formatted ticket to the console
     *
     * @param ticket Ticket to be formatted and printed
     */
    public void printTicket(Ticket ticket){
        System.out.println(ansi().eraseScreen());
        AsciiTable at = new AsciiTable();
        at.addRule();
        AT_Row row1 = at.addRow("ID", "Requester", "Subject", "Description", "Updated", "Tags");
        at.addRule();
        AT_Row row2 = at.addRow(
                ticket.id,
                ticket.requesterName,
                ticket.subject,
                ticket.description,
                formatTime(ticket.updated_at),
                formatTags(ticket.tags));
        at.addRule();
        row1.setTextAlignment(TextAlignment.CENTER);
        row2.setTextAlignment(TextAlignment.LEFT);
        at.getRenderer().setCWC(new CWC_FixedWidth().add(5).add(14).add(18).add(42).add(22).add(12));
        System.out.println(ansi().a(at.render()));
    }

    /**
     * Prints a Ticket[] as a formatted table to the console
     *
     * @param ticketPage Ticket[] to be formatted and printed
     */
    public void printTicketPage(Ticket[] ticketPage){
        System.out.println(ansi().eraseScreen());
        AsciiTable at = new AsciiTable();
        at.addRule();
        AT_Row row1 = at.addRow("ID", "Requester", "Subject", "Updated", "Tags");
        at.addRule();

        for (Ticket ticket : ticketPage){
            AT_Row row = at.addRow(
                    ticket.id,
                    ticket.requesterName,
                    ticket.subject,
                    formatTime(ticket.updated_at),
                    formatTags(ticket.tags));
            at.addRule();
            row.setTextAlignment(TextAlignment.LEFT);
        }
        row1.setTextAlignment(TextAlignment.CENTER);
        at.getRenderer().setCWC(new CWC_FixedWidth().add(5).add(14).add(32).add(22).add(41));
        System.out.println(ansi().a(at.render()));
    }

    /**
     * Prints a query containing the basic options
     */
    public void printBasicQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("a", "View all available tickets");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a query containing information relevant to viewing pages
     */
    public void printPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("n", "View next page");
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a query containing information relevant to being on the first
     * ticket page
     */
    public void printFirstPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("n", "View next page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a query containing information relevant to being on the last
     * ticket page
     */
    public void printLastPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a formatted error message to the console
     *
     * @param errorMsg
     */
    public void printError(String errorMsg){
        System.out.println(ansi().a(""));
        System.out.println(ansi().fg(Ansi.Color.RED).a("ERROR: ").fg(Ansi.Color.WHITE).a(errorMsg).reset());
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a formatted warning message to the console
     *
     * @param warnMsg
     */
    public void printWarn(String warnMsg){
        System.out.println(ansi().a(""));
        System.out.println(ansi().fg(Ansi.Color.YELLOW).a(warnMsg).reset());
        System.out.println(ansi().a(""));
    }

    /**
     * Prints a formatted query with input option
     * ex: (q) Quit
     *
     * @param input string indicating input to choose said option
     * @param prompt string providing information about the choice
     */
    private void printQueryInput(String input, String prompt){
        System.out.println(ansi().fg(Ansi.Color.WHITE).a("       (").fg(Ansi.Color.YELLOW).a(input).fg(Ansi.Color.WHITE).a(") ").reset().a(prompt));
    }

    /**
     * Printed on exiting the program
     */
    public void printExit(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("                Thank you for using ZenTicket!");
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    /**
     * Formats ISO8601 time to a more presentable form
     *
     * @param iso8601Time time string adhering to ISO 8601 conventions
     * @return "MM/dd/yyyy hh:mm a" format of iso8601Time
     */
    private String formatTime(String iso8601Time){
        DateTimeFormatter formatIn = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneId.of("UTC"));
        DateTimeFormatter formatOut = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")
                .withZone(ZoneId.of("UTC"));
        LocalDateTime date = LocalDateTime.parse(iso8601Time, formatIn);
        return(date.format(formatOut));
    }

    /**
     * Formats a presented String[] into a comma separated string
     *
     * @param tags String[] containing tags
     * @return comma separated string
     */
    private String formatTags(String[] tags){
        StringBuilder out = new StringBuilder();
        for (String tag : tags){
            out.append(tag).append(", ");
        }
        return out.substring(0, out.length()-2);
    }
}
