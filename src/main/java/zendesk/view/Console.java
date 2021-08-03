package zendesk.view;

import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static zendesk.util.ConsoleColors.*;

import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import zendesk.api.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.fusesource.jansi.Ansi.*;

public class Console {
    static String[] banner = new String[]{
            "",
            "",
            "     ______     ______     __   __     ______   __     ______     __  __     ______     ______                                    ",
            "    /\\___  \\   /\\  ___\\   /\\ '-.\\ \\   /\\__  _\\ /\\ \\   /\\  ___\\   /\\ \\/ /    /\\  ___\\   /\\__  _\\                ",
            "    \\/_/  /__  \\ \\  __\\   \\ \\ \\-.  \\  \\/_/\\ \\/ \\ \\ \\  \\ \\ \\____  \\ \\  _'-.  \\ \\  __\\   \\/_/\\ \\/          ",
            "      /\\_____\\  \\ \\_____\\  \\ \\_\\\\'\\_\\    \\ \\_\\  \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\    \\ \\_\\    ",
            "      \\/_____/   \\/_____/   \\/_/ \\/_/     \\/_/   \\/_/   \\/_____/   \\/_/\\/_/   \\/_____/     \\/_/                        ",
            "",
            ""
    };

    public Console(){
        AnsiConsole.systemInstall();
    }

    public void printBanner(){
        System.out.println(ansi().eraseScreen());
        System.out.println(ansi().fg(Ansi.Color.GREEN));
        for (String line : banner){
            System.out.println(ansi().a(line));
        }
        System.out.println(ansi().reset());
    }

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
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{2, 18, 18, 30, 18, 10}));
        System.out.println(ansi().a(at.render()));
    }

    public void printBasicQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("a", "View all available tickets");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    public void printPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("n", "View next page");
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    public void printFirstPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("n", "View next page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    public void printLastPageQuery(){
        System.out.println(ansi().a(""));
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
        System.out.println(ansi().a(""));
    }

    public void printInputQuery(String query){
        System.out.println(query);
    }

    public void printError(String errorMsg){
        System.out.println(ansi().a(""));
        System.out.println(ansi().fg(Ansi.Color.RED).a("ERROR: ").fg(Ansi.Color.WHITE).a(errorMsg).reset());
        System.out.println(ansi().a(""));
    }

    public void printWarn(String warnMsg){
        System.out.println(ansi().a(""));
        System.out.println(ansi().fg(Ansi.Color.YELLOW).a(warnMsg).reset());
        System.out.println(ansi().a(""));
    }

    private void printQueryInput(String input, String prompt){
        System.out.println(ansi().fg(Ansi.Color.WHITE).a("       (").fg(Ansi.Color.YELLOW).a(input).fg(Ansi.Color.WHITE).a(") ").reset().a(prompt));
    }

    public void printExit(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("                Thank you for using ZenTicket!");
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    private String formatTime(String iso8601Time){
        DateTimeFormatter formatIn = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneId.of("UTC"));
        DateTimeFormatter formatOut = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")
                .withZone(ZoneId.of("UTC"));
        LocalDateTime date = LocalDateTime.parse(iso8601Time, formatIn);
        return(date.format(formatOut));
    }

    private String formatTags(String[] tags){
        StringBuilder out = new StringBuilder();
        for (String tag : tags){
            out.append(tag).append(", ");
        }
        return out.substring(0, out.length()-2);
    }

    private void setColor(String color){
        System.out.println(color);
    }

    private void resetColor(){
        System.out.println(RESET);
    }
}
