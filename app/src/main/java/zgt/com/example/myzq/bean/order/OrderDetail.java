package zgt.com.example.myzq.bean.order;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String uuid;//	订单id
    private String orderno;//	订单编号
    private String typeid;//	课程id
    private int producttype;//	1为课程
    private double price;//	课程原价
    private double realprice;//实际价格（元）
    private double iosrealprice;//ios成交价格（元）(牵牛币)
    private int amount;//	数量
    private double realmoney;//	支付金额

    private double iosrealmoney;//	ios支付金额

    private int pricelimit;//	价格期限 0为永久 ，其他为多少天
    private String relationno;//	关联订单号 支付宝微信传此订单号
    private String startdate;//	订单开始日期
    private String enddate;//订单结束日期
    private int status;//订单状态 -1已取消，0为未支付，1为已支付， 2已签约
    private String ordertime;//	订单时间
    private int paytype;//支付方式
    private String paytime;//	订单支付时间
    private String payid;//第三方支付订单号

    private String picpath;//	课程图片路径
    private String title;//	课程标题
    private String lecturer;//	讲师

    public double getIosrealmoney() {
        return iosrealmoney;
    }

    public void setIosrealmoney(double iosrealmoney) {
        this.iosrealmoney = iosrealmoney;
    }

    public double getIosrealprice() {
        return iosrealprice;
    }

    public void setIosrealprice(double iosrealprice) {
        this.iosrealprice = iosrealprice;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
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

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public int getProducttype() {
        return producttype;
    }

    public void setProducttype(int producttype) {
        this.producttype = producttype;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(double realmoney) {
        this.realmoney = realmoney;
    }

    public int getPricelimit() {
        return pricelimit;
    }

    public void setPricelimit(int pricelimit) {
        this.pricelimit = pricelimit;
    }

    public String getRelationno() {
        return relationno;
    }

    public void setRelationno(String relationno) {
        this.relationno = relationno;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
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

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }
}
