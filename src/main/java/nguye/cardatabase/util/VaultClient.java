package nguye.cardatabase.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
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
public class VaultClient {

    private static final String CLIENT_ID = System.getenv("HCP_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("HCP_CLIENT_SECRET");
    private static final String ORGANIZATION_ID = "50bc9d2b-030e-4531-8a34-9122d6fc7705";
    private static final String PROJECT_ID = "dc39aa7e-057c-424d-a7c5-375f7ee66309";
    private static final String APP_NAME = "sample-app";

    private static final String BASE_URL = "https://api.cloud.hashicorp.com/secrets/2023-06-13/organizations/" +
            ORGANIZATION_ID + "/projects/" +
            PROJECT_ID + "/apps/" +
            APP_NAME + "/";

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

    public static String makeApiGetRequest(String secret) throws Exception {
        String url =  BASE_URL + "open/" + secret;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);

        String token = getToken();
        get.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("API Request Response Code :: " + statusCode);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
        return "Response empty.";
    }

    public static void makeApiPostRequest(String json) throws Exception {
        String url = BASE_URL + "kv";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        String token = getToken();
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");

        StringEntity bodyEntity = new StringEntity(json);
        post.setEntity(bodyEntity);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("API Request Response Code :: " + statusCode);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
    }

    public static void  makeApiDeleteRequest(String secret) throws Exception {
        String url = BASE_URL + "secrets/" + secret;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(url);

        String token = getToken();
        delete.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(delete)) {
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
