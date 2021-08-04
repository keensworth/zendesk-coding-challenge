package zendesk.api;

import zendesk.util.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * UserFetcher provides specific exposure to the ZendeskAPI
 * to allow the user to perform a variety of user-related
 * API requests.
 */
public class UserFetcher {
    private final ZendeskAPI zendeskAPI;

    private static final String USER_REQUEST = "/api/v2/users/";

    /**
     * Constructor for UserFetcher
     *
     * @param zendeskAPI instance of the ZendeskAPI
     */
    public UserFetcher(ZendeskAPI zendeskAPI){
        this.zendeskAPI = zendeskAPI;
    }

    /**
     * Fetches a user's name with respect to their id
     *
     * @param id user id
     * @return name String returned by the Zendesk API
     */
    public String fetchNameFromId(long id){
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + id + ".json");
        return JSONParser.parseUserStringForName(response);
    }

    /**
     * Fetches the names of users with respect to their ids. This is performed
     * in a single API call, and is used during the formatting of tables
     * to display to the user
     *
     * @param ids An ArrayList of user ids to map names to
     * @return HashMap pairings of requester_ids to user names
     */
    public HashMap<Long, String> fetchNamesFromIds(ArrayList<Long> ids){
        StringBuilder idsStringBuilder = new StringBuilder();
        for (long id : ids){
            idsStringBuilder.append(id).append(",");
        }
        String idsString = idsStringBuilder.substring(0, idsStringBuilder.length()-1);
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + "show_many.json?ids=" + idsString);
        return JSONParser.parseUserStringForNames(response);
    }
}
