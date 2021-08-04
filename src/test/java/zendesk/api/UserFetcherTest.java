package zendesk.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zendesk.util.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserFetcherTest {
    ZendeskAPI zendeskAPI;

    private static final String USER_REQUEST = "/api/v2/users/";

    @BeforeEach
    void setUp() {
        zendeskAPI = new ZendeskAPI();
        zendeskAPI.setCredentials("/config/properties");
    }

    @Test
    void fetchNameFromId() {
        long id = 419077067791L;
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + id + ".json");
        String name = JSONParser.parseUserStringForName(response);

        assertEquals("Ben Keeney", name);
    }

    @Test
    void fetchNamesFromIds() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(419077067791L);
        ids.add(419077069151L);

        StringBuilder idsStringBuilder = new StringBuilder();
        for (long id : ids){
            idsStringBuilder.append(id).append(",");
        }
        String idsString = idsStringBuilder.substring(0, idsStringBuilder.length()-1);
        String response = zendeskAPI.makeGetRequest(USER_REQUEST + "show_many.json?ids=" + idsString);
        HashMap<Long,String> out = JSONParser.parseUserStringForNames(response);

        assertNotNull(out.get(419077067791L));
        assertNotNull(out.get(419077069151L));
        assertEquals(out.get(419077067791L), "Ben Keeney");
        assertEquals(out.get(419077069151L), "The Customer");
    }
}