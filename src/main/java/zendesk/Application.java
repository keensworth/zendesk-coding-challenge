package zendesk;

import zendesk.model.ConsoleManager;
import zendesk.api.ZendeskAPI;

public class Application {
    private final ConsoleManager consoleManager;


    public Application(){
        ZendeskAPI zendeskAPI = new ZendeskAPI();
        zendeskAPI.setCredentials("config.properties");

        consoleManager = new ConsoleManager(zendeskAPI);
    }


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
