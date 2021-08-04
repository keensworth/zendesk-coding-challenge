package zendesk.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

/**
 * Zendesk API instance, holding a users encrypted authorization and subdomain information.
 * This allows a user to set their credentials, and continue to make raw GET requests without
 * needing to further verify themselves.
 */
public class ZendeskAPI {
    private String encryptedAuth;
    private String hostURL;

    /**
     * Makes a raw GET request, as specified by the input
     *
     * @param request relevant API GET command to submit to the Zendesk API
     * @return JSON String response from Zendesk
     */
    public String makeGetRequest(String request){
        try {
            StringBuilder result = new StringBuilder();
            java.net.URL url = new URL(hostURL + request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.addRequestProperty("Authorization", "Basic " + encryptedAuth);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            } catch(FileNotFoundException e){
                return "ex: FileNotFound";
            }

            return result.toString();

        } catch (Exception e){
            System.out.println("Error verifying credentials! Please ensure config.properties has been configured correctly.");
            System.out.println("Then, execute \"mvn package\" and re-run the program.");
            System.exit(0);
        }
        return null;
    }

    /**
     * Allows a user to set their credentials, and continue to use the API without needing to continuously
     * authorize themselves.
     *
     * @param configPath path to config.properties containing email, API token, and Zendesk subdomain.
     */
    public void setCredentials(String configPath){
        try {
            Scanner scanner = new Scanner(this.getClass().getResourceAsStream(configPath));
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
            System.out.println("Error verifying credentials! Please ensure config.properties has been configured correctly.");
            System.out.println("Then, execute \"mvn package\" and re-run the program.");
        }
    }
}
