package love.linyi.controller;

public class Result {
    private Integer code;
    private String msg;
    private Object data;
    public Result(Integer code, Object data,String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Result(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }
    public Result() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
