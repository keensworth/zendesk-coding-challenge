package zendesk.api;

import zendesk.util.JSONParser;

import java.util.ArrayList;

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

    public String[] fetchNamesFromIds(ArrayList<Long> ids){
        StringBuilder idsStringBuilder = new StringBuilder();
        for (long id : ids){
            idsStringBuilder.append(id).append(",");
        }
        String idsString = idsStringBuilder.substring(0, idsStringBuilder.length()-1);
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + "show_many.json?ids=" + idsString);
        return JSONParser.parseUserStringForNames(response);
    }
}
