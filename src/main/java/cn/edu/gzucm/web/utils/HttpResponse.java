package cn.edu.gzucm.web.utils;

import java.io.Serializable;
import java.util.Map;

public class HttpResponse<T> implements Serializable {

    private Map<String, Object> headers;
    private T body;
    private int responseCode;
    private String url;

    public HttpResponse() {

    }

    public HttpResponse(Map<String, Object> headers, T body, int responseCode) {

        this.headers = headers;
        this.body = body;
        this.responseCode = responseCode;
    }

    public Map<String, Object> getHeaders() {

        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {

        this.headers = headers;
    }

    public T getBody() {

        return body;
    }

    public void setBody(T body) {

        this.body = body;
    }

    public int getResponseCode() {

        return responseCode;
    }

    public void setResponseCode(int responseCode) {

        this.responseCode = responseCode;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }
}
