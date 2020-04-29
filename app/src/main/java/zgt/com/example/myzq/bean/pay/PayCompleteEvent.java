package zgt.com.example.myzq.bean.pay;

public class PayCompleteEvent {

    public static final int PAY_SUCCESS = 1;

    public static final int PAY_EORROR = 0;

    public int payType;

    public boolean isSuccess;

    //自定义消息
    public String msg;

    //自定义code
    public String code;

    public PayCompleteEvent(int payType) {
        this.payType = payType;
    }

    public PayCompleteEvent(int payType, int isSuccess) {
        this.payType = payType;
        this.isSuccess = isSuccess == PAY_SUCCESS;
    }
}
