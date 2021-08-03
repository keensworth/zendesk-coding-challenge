package zendesk.api;

import zendesk.util.JSONParser;

public class UserFetcher {
    private final ZendeskAPI zendeskAPI;

    private static final String USER_REQUEST = "/api/v2/users/";

    public UserFetcher(ZendeskAPI zendeskAPI){
        this.zendeskAPI = zendeskAPI;
    }

    public String fetchNameFromId(long id){
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + id + ".json");
        return JSONParser.parseUserStringForName(response);
    }
}
