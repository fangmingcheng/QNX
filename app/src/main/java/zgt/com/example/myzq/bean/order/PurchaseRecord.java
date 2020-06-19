package zgt.com.example.myzq.bean.order;

public class PurchaseRecord {
    private String uuid;//我的订单主键
    private String orderno;//订单号
    private String typeid;//课程id
    private int status;//订单状态 -1已取消，0为未支付，1为已支付， 2已到期
    private String ordertime;//订单生成时间
    private int producttype;//1课程
    private String title;//课程标题
    private double iosprice;//ios课程原价(牵牛币)
    private double iosrealprice;//ios成交价格（元）(牵牛币)
    private double price;//版本定价
    private double realprice;//成交价格
    private String lecturer;//主讲老师
    private String picpath;//课程封面图片路径
    private int amount;//数量
    private int isnewversion ;//是否新版本订单，0老订单，1新订单
    private int paytype;//付款方式，0为未知支付，1为支付宝，2为微信，3为ios虚拟币支付，4为线下支付’


    public double getIosprice() {
        return iosprice;
    }

    public void setIosprice(double iosprice) {
        this.iosprice = iosprice;
    }

    public double getIosrealprice() {
        return iosrealprice;
    }

    public void setIosrealprice(double iosrealprice) {
        this.iosrealprice = iosrealprice;
    }

    public int getIsnewversion() {
        return isnewversion;
    }

    public void setIsnewversion(int isnewversion) {
        this.isnewversion = isnewversion;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public int getProducttype() {
        return producttype;
    }

    public void setProducttype(int producttype) {
        this.producttype = producttype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRealprice() {
        return realprice;
    }

    public void setRealprice(double realprice) {
        this.realprice = realprice;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
}
