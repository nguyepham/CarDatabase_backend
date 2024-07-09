package nguye.cardatabase.vault;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Client {

    private static final String CLIENT_ID = "yE7DfgHBgIqUwclsKTTJ5GqIIeyG8dAZ";
    private static final String CLIENT_SECRET = "aygliPfG9XpIn9GwupLmEqZgj1oPDxEQ6xafPHvO-5NP5EHLdX1laUihzrNLm25P";

    public static void main(String[] args) throws Exception {
        String token = getToken();
        makeApiRequest(token);
    }

    private static String getToken() throws Exception {
        String url = "https://auth.idp.hashicorp.com/oauth2/token";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=client_credentials" +
                "&audience=https://api.hashicorp.cloud";

        post.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Token Request Response Code :: " + statusCode);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(result);
                return rootNode.get("access_token").asText();
            }
        }

        return null;
    }

    private static void makeApiRequest(String token) throws Exception {
        String url = "https://api.cloud.hashicorp.com/secrets/2023-06-13/organizations/50bc9d2b-030e-4531-8a34-9122d6fc7705/projects/dc39aa7e-057c-424d-a7c5-375f7ee66309/apps/sample-app/open";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);

        get.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("API Request Response Code :: " + statusCode);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
    }
}
