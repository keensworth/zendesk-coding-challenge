package zendesk.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

public class ZendeskAPI {
    private String encryptedAuth;
    private String hostURL;

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
            }
            return result.toString();

        } catch (MalformedURLException e){
            System.out.println( "NONSENSE");
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
            System.out.println("config.properties not found!");
            e.printStackTrace();
        }
    }
}
