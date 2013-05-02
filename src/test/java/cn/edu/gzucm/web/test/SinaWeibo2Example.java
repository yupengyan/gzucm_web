package cn.edu.gzucm.web.test;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class SinaWeibo2Example {
    private static final String NETWORK_NAME = "SinaWeibo";
    private static final String PROTECTED_RESOURCE_URL = "https://api.weibo.com/2/account/get_uid.json";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args) {

        String apiKey = "3971669613";
        String apiSecret = "c16509133f90c5f59ec1297ca5326643";
        OAuthService service = new ServiceBuilder().provider(SinaWeiboApi20.class).apiKey(apiKey).apiSecret(apiSecret).callback("http://apps.weibo.com/gzucmjtian").build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verifier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
    }
}
