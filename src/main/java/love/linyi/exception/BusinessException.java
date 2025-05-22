package love.linyi.exception;

public class BusinessException extends RuntimeException{
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BusinessException(int code, String message ) {
        super(message);
        this.code = code;
    }

    public BusinessException(int codeString, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }



}
