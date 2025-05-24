package love.linyi.exception;

public class SystemException extends RuntimeException{
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SystemException(int code, String message ) {
        super(message);
        this.code = code;
    }

    public SystemException( int code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }



}
