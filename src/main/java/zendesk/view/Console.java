package zendesk.view;

import static zendesk.util.ConsoleColors.*;
import org.fusesource.jansi.AnsiConsole;
import zendesk.util.ConsoleColors;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class Console {
    static String[] banner = new String[]{
            "\n",
            "\n",
            "\n",
            "███████ ███████ ███    ██ ████████ ██  ██████ ██   ██ ███████ ████████",
            "   ███  ██      ████   ██    ██    ██ ██      ██  ██  ██         ██   ",
            "  ███   █████   ██ ██  ██    ██    ██ ██      █████   █████      ██   ",
            " ███    ██      ██  ██ ██    ██    ██ ██      ██  ██  ██         ██   ",
            "███████ ███████ ██   ████    ██    ██  ██████ ██   ██ ███████    ██   ",
            "\n",
            "\n",
            "\n"
    };

    public Console(){
        AnsiConsole.systemInstall();
    }

    public void printBanner(){
        ansi().eraseScreen();
        ansi().fg(Color.GREEN);
        for (String line : banner){
            System.console().writer().println(ansi().a(line));
        }
        ansi().reset();
    }

    public void printBasicQuery(){
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("a", "View all available tickets");
        printQueryInput("q", "Quit");
    }

    public void printPageQuery(){
        printQueryInput("n", "View next page");
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
    }

    public void printFirstPageQuery(){
        printQueryInput("n", "View next page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
    }

    public void printLastPageQuery(){
        printQueryInput("p", "View previous page");
        printQueryInput("t", "Fetch ticket by ID");
        printQueryInput("q", "Quit");
    }

    public void printInputQuery(String query){
        System.console().writer().println(query);
    }

    public void printError(String errorMsg){
        ansi().fg(Color.RED);
        System.console().writer().println(ansi().a("ERROR: "));
        ansi().fg(Color.WHITE);
        System.console().writer().print(ansi().a(errorMsg));
        ansi().reset();
    }

    public void printWarn(String warnMsg){
        ansi().fg(Color.YELLOW);
        System.console().writer().print(ansi().a(warnMsg));
        ansi().reset();
    }

    private void printQueryInput(String input, String prompt){
        ansi().fg(Color.WHITE);
        System.console().writer().println("    (");
        ansi().fg(Color.YELLOW);
        System.console().writer().print(ansi().a(input));
        ansi().fg(Color.WHITE);
        System.console().writer().print(") ");
        ansi().reset();
        System.console().writer().print(ansi().a(prompt));
    }

    public void printExit(){
        System.console().writer().println("");
        System.console().writer().println("");
        System.console().writer().println("");
        System.console().writer().println("                Thank you for using ZenTicket!");
        System.console().writer().println("");
        System.console().writer().println("");
        System.console().writer().println("");
    }

    private void setColor(String color){
        System.console().writer().println(color);
    }

    private void resetColor(){
        System.console().writer().println(RESET);
    }
}
