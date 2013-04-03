package cn.edu.gzucm.web.sns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import cn.edu.gzucm.web.sns.sina.SinaError;
import cn.edu.gzucm.web.utils.ClientUtils;

public abstract class ProviderConnectorImpl implements ProviderConnector {

    private String PROTECTED_RESOURCE_URL;

    protected String hash = "hashhash";
    protected Token accessToken;
    protected OAuthService service;

    public ProviderConnectorImpl(String urlPrefix) {

        PROTECTED_RESOURCE_URL = urlPrefix;
    }

    public <T> T makeCall(Verb verb, String api, Class<T> returnType, Map<String, Object> parameters) throws MyApiException {

        OAuthRequest request = new OAuthRequest(verb, PROTECTED_RESOURCE_URL + api);
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                request.addQuerystringParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        service.signRequest(accessToken, request);
        Response response = request.send();
        String result = response.getBody();
        System.out.println(result);
        if (result != null && result.startsWith("{\"error\"")) {
            SinaError error = ClientUtils.jsonToObject(result, SinaError.class);
            throw new MyApiException(error.getError(), error.getError_code());
        } else {
            T profile = ClientUtils.jsonToObject(result, returnType);
            return profile;
        }
    }

    public <T> T makeCall(String api, String fileParam, String filePath, Class<T> returnType, Map<String, Object> parameters) throws MyApiException {

        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL + api);

        service.signRequest(accessToken, request);

        MultipartEntity entity = new MultipartEntity();

        try {
            if (parameters != null) {
                for (Entry<String, Object> entry : parameters.entrySet()) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue().toString(), Charset.forName("UTF-8")));
                }
            }

            entity.addPart(fileParam, new FileBody(new File(filePath))); // THIS IS THE PHOTO TO UPLOAD

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            entity.writeTo(out);

            request.addPayload(out.toByteArray());
            request.addHeader(entity.getContentType().getName(), entity.getContentType().getValue());

            Response response = request.send();
            String result = response.getBody();
            System.out.println(result);
            if (result != null && result.startsWith("{\"error\"")) {
                SinaError error = ClientUtils.jsonToObject(result, SinaError.class);
                throw new MyApiException(error.getError(), error.getError_code());
            } else {
                T profile = ClientUtils.jsonToObject(result, returnType);
                return profile;
            }
        } catch (Exception e) {
            throw new MyApiException(e);
        }
    }
}
