package cn.edu.gzucm.web.sns;

public class MyApiException extends Exception {

    private static final long serialVersionUID = 1L;
    private int errorCode;

    public MyApiException() {

        super();
    }

    public MyApiException(String message, int code) {

        super(message);
        this.errorCode = code;
    }

    public MyApiException(String message) {

        super(message);
    }

    public MyApiException(Throwable cause) {

        super(cause);
    }

    public int getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(int errorCode) {

        this.errorCode = errorCode;
    }

}
