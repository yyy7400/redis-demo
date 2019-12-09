package common;

import java.io.Serializable;

/**
 * 返回数据
 * @author yangyuyang
 * @date 2019-12-06
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -570224693278831430L;

    /**
     * 响应业务状态
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private transient T data;

    public Response() {
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Response init(BusinessCode respCode, String msg) {
        Response response = new Response();
        response.setCode(respCode.getCode());
        response.setMsg(msg);
        return response;
    }

    public static <T> Response init(BusinessCode respCode, String msg, T data) {
        Response response = new Response();
        response.setCode(respCode.getCode());
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static <T> Response init(String code, String msg, T data) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static Response success() {
        return init(BusinessCode.SUCCESS, BusinessCode.SUCCESS.getMsg());
    }

    public static Response success(String msg) {
        return init(BusinessCode.SUCCESS,msg);
    }

    public static Response success(Object data, String msg) {
        return init(BusinessCode.SUCCESS, msg, data);
    }

    public static Response fail(BusinessCode respCode, String msg) {
        return init(respCode, msg);
    }

    public static Response fail() {
        return init(BusinessCode.FAILD, BusinessCode.FAILD.getMsg());
    }

    public static Response fail(String msg) {
        return init(BusinessCode.FAILD, msg);
    }

    public static boolean isSuccess(Response response) {
        return BusinessCode.SUCCESS.getCode() == response.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
