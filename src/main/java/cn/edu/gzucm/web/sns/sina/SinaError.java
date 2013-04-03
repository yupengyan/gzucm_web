package cn.edu.gzucm.web.sns.sina;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SinaError {

    private String error;
    private int error_code;
    private String request;

    public String getError() {

        return error;
    }

    public void setError(String error) {

        this.error = error;
    }

    public int getError_code() {

        return error_code;
    }

    public void setError_code(int error_code) {

        this.error_code = error_code;
    }

    public String getRequest() {

        return request;
    }

    public void setRequest(String request) {

        this.request = request;
    }
}
