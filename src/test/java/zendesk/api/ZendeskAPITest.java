package zendesk.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ZendeskAPITest {
    private String encryptedAuth;
    private String hostURL;

    @BeforeEach
    void setUp() {
    }

    @Test
    void makeGetRequest_Valid_connection() {
        setCredentials();
        String output = "";
        String urlToRead = "https://zccbenkeeney.zendesk.com/api/v2/tickets/1.json";
        try {
            String emailToken = "benjakeen@gmail.com/token:9EpeCkS4igmHxtbRFG1WU8eHv3djyof2Vqy6jEzA";
            String base64Encoded = Base64.getEncoder().encodeToString(emailToken.getBytes());
            System.out.println(base64Encoded);
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Authorization", "Basic " + base64Encoded);
            conn.addRequestProperty("Accept", "application/json");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            output = result.toString();

        } catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(output, "{\"ticket\":{\"url\":\"https://zccbenkeeney.zendesk.com/api/v2/tickets/1.json\",\"id\":1,\"external_id\":null,\"via\":{\"channel\":\"sample_ticket\",\"source\":{\"from\":{},\"to\":{},\"rel\":null}},\"created_at\":\"2021-07-31T22:52:32Z\",\"updated_at\":\"2021-07-31T22:52:33Z\",\"type\":\"incident\",\"subject\":\"Sample ticket: Meet the ticket\",\"raw_subject\":\"Sample ticket: Meet the ticket\",\"description\":\"Hi Ben,\\n\\nThis is your first ticket. Ta-da! Any customer request sent to your supported channels (email, chat, voicemail, web form, and tweet) will become a Support ticket, just like this one. Respond to this ticket by typing a message and clicking Submit. You can also see how an email becomes a ticket by emailing your new account, support@zccbenkeeney.zendesk.com. Your ticket will appear in ticket views.\\n\\nThat's the ticket on tickets. If you want to learn more, check out: \\nhttps://support.zendesk.com/hc/en-us/articles/203691476\\n\",\"priority\":\"normal\",\"status\":\"open\",\"recipient\":null,\"requester_id\":419077069151,\"submitter_id\":419077067791,\"assignee_id\":419077067791,\"organization_id\":null,\"group_id\":360020747591,\"collaborator_ids\":[],\"follower_ids\":[],\"email_cc_ids\":[],\"forum_topic_id\":null,\"problem_id\":null,\"has_incidents\":false,\"is_public\":true,\"due_at\":null,\"tags\":[\"sample\",\"support\",\"zendesk\"],\"custom_fields\":[],\"satisfaction_rating\":null,\"sharing_agreement_ids\":[],\"fields\":[],\"followup_ids\":[],\"ticket_form_id\":360003098091,\"brand_id\":360006808511,\"allow_channelback\":false,\"allow_attachments\":true}}\n");
    }

    @Test
    void setCredentials() {
        encryptedAuth = "";
        hostURL = "";
        try {
            Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/config.properties"));
            String email = "";
            String token = "";
            String subdomain = "";
            while (scanner.hasNext()){
                String[] line = scanner.nextLine().split("=");
                switch (line[0]) {
                    case "email" -> email = line[1];
                    case "token" -> token = line[1];
                    case "subdomain" -> subdomain = line[1];
                }
            }
            String rawAuthorization = email + "/token:" + token;
            this.encryptedAuth = Base64.getEncoder().encodeToString(rawAuthorization.getBytes());
            this.hostURL = "https://" + subdomain + ".zendesk.com";
        } catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(encryptedAuth, "YmVuamFrZWVuQGdtYWlsLmNvbS90b2tlbjo5RXBlQ2tTNGlnbUh4dGJSRkcxV1U4ZUh2M2RqeW9mMlZxeTZqRXpB");
        assertEquals(hostURL, "https://zccbenkeeney.zendesk.com");
    }
}