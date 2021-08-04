package zendesk;

import zendesk.model.ConsoleManager;
import zendesk.api.ZendeskAPI;

/**
 * Main driver class. Establishes connection to the ZendeskAPI and assesses
 * ConsoleManager state until the program exits.
 */
public class Application {
    private final ConsoleManager consoleManager;

    /**
     * Constructor, instantiates ZendeskAPI and ConsoleManager
     */
    public Application(){
        ZendeskAPI zendeskAPI = new ZendeskAPI();
        zendeskAPI.setCredentials("/config.properties");

        consoleManager = new ConsoleManager(zendeskAPI);
    }

    /**
     * Runs the program until the exit state is reached
     */
    public void run(){
        int state = -1;

        while (state != ConsoleManager.QUIT_STATE){
            state = consoleManager.assessState();
        }
    }


    public static void main(String[] args){
        Application app = new Application();
        app.run();
    }
}
