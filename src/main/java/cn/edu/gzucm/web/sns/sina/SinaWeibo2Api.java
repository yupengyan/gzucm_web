package cn.edu.gzucm.web.sns.sina;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.Preconditions;

public class SinaWeibo2Api extends DefaultApi20

{

    private static final String AUTHORIZATION_URL = "https://api.weibo.com/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {

        return "https://api.weibo.com/oauth2/access_token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {

        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. SinaWeibo2 does not support OOB");
        try {
            return String.format(AUTHORIZATION_URL, config.getApiKey(), URLEncoder.encode(config.getCallback(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public Verb getAccessTokenVerb() {

        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {

        return new JsonTokenExtractor();
    }

}