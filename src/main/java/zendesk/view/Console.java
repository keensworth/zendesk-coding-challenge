package zendesk.view;

import static zendesk.view.ConsoleColors.*;

public class Console {
    static String[] banner = new String[]{
            "\n",
            "\n",
            "\n",
            "        ███████╗███████╗███╗   ██╗████████╗██╗ ██████╗██╗  ██╗███████╗████████╗",
            "        ╚══███╔╝██╔════╝████╗  ██║╚══██╔══╝██║██╔════╝██║ ██╔╝██╔════╝╚══██╔══╝",
            "          ███╔╝ █████╗  ██╔██╗ ██║   ██║   ██║██║     █████╔╝ █████╗     ██║   ",
            "         ███╔╝  ██╔══╝  ██║╚██╗██║   ██║   ██║██║     ██╔═██╗ ██╔══╝     ██║   ",
            "        ███████╗███████╗██║ ╚████║   ██║   ██║╚██████╗██║  ██╗███████╗   ██║   ",
            "        ╚══════╝╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝   ╚═╝   ",
            "\n",
            "\n",
            "\n"
    };

    public Console(){ }

    public void printBanner(){
        setColor(GREEN);
        for (String line : banner){
            System.console().writer().println(line);
        }
        resetColor();
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

    public void printInputQuery(String query){
        System.console().writer().println(query);
    }

    public void printError(String errorMsg){
        setColor(RED_BRIGHT);
        System.console().writer().println("ERROR: ");
        setColor(WHITE_BRIGHT);
        System.console().writer().print(errorMsg);
        resetColor();
    }

    private void printQueryInput(String input, String prompt){
        setColor(WHITE_BRIGHT);
        System.console().writer().println("    (");
        setColor(YELLOW_BRIGHT);
        System.console().writer().print(input);
        setColor(WHITE_BRIGHT);
        System.console().writer().print(") ");
        resetColor();
        System.console().writer().print(prompt);
    }

    private void setColor(String color){
        System.console().writer().println(color);
    }

    private void resetColor(){
        System.console().writer().println(RESET);
    }
}
