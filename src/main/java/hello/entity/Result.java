package hello.entity;

public class Result{
    String status;
    String msg;
    boolean isLogin;
    Object data;

    private Result(String status, String msg, boolean isLogin) {
        this(status,msg,isLogin,null);
    }

    public Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public static Result failure(String msg){
        return new Result("fail",msg,false);
    }

    public static Result success (String msg){
        return new Result("ok",msg,true);
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}
