package common;

/**
 * 基础业务异常类
 * @author yangyuyang
 * @date 2019-12-06
 */
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;
    private Throwable cause;
    public BusinessException(){
    }

    public BusinessException(String msg) {
        super(msg);
        this.setCode(BusinessCode.FAILD.getCode());
        this.setMsg(msg);
    }

    public BusinessException(BusinessCode businessCode) {
        super(businessCode.getMsg());
        this.setCode(businessCode.getCode());
        this.setMsg(businessCode.getMsg());
    }

    public BusinessException(String msg, Throwable e) {
        super(msg);
        this.setCode(BusinessCode.FAILD.getCode());
        this.setMsg( msg);
        this.cause = e;
    }

    public BusinessException(BusinessCode businessCode, String msg) {
        super(msg);
        this.setCode(businessCode.getCode());
        this.setMsg(msg);
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

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
