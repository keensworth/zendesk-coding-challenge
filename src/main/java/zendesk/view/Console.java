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
        ansi().fg(org.fusesource.jansi.Ansi.Color.GREEN);
        for (String line : banner){
            System.out.println(ansi().a(line));
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
        System.out.println(query);
    }

    public void printError(String errorMsg){
        ansi().fg(Color.RED);
        System.out.println(ansi().a("ERROR: "));
        ansi().fg(Color.WHITE);
        System.out.print(ansi().a(errorMsg));
        ansi().reset();
    }

    public void printWarn(String warnMsg){
        ansi().fg(Color.YELLOW);
        System.out.print(ansi().a(warnMsg));
        ansi().reset();
    }

    private void printQueryInput(String input, String prompt){
        ansi().fg(Color.WHITE);
        System.out.println("    (");
        ansi().fg(Color.YELLOW);
        System.out.print(ansi().a(input));
        ansi().fg(Color.WHITE);
        System.out.print(") ");
        ansi().reset();
        System.out.print(ansi().a(prompt));
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

    private void setColor(String color){
        System.out.println(color);
    }

    private void resetColor(){
        System.out.println(RESET);
    }
}
