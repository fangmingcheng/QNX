package zgt.com.example.myzq.bean;

/**
 * 我的订单
 * Created by ThinkPad on 2019/6/21.
 */

public class Order {
    private String uuid;//我的订单主键
    private String orderno;//订单号
    private String memberid;//会员主键uuid
    private String teacherid;//老师主键uuid
    private String typeid;//课程id
    private String theadimg;//老师头像
    private String teachername;//老师姓名
    private String typename;//版本名称
    private String price;//版本定价
    private String realprice;//成交价格
    private String startdate;//订单开始日期
    private String enddate;//订单结束日期
    private String ftitle;//
    private int status;//订单状态 -1已取消，0为未支付，1为已支付， 2已到期
    private int paytype;//0为线下付款，1为支付宝，2为微信
    private int producttype;//1为软件，2为精品课程

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

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTheadimg() {
        return theadimg;
    }

    public void setTheadimg(String theadimg) {
        this.theadimg = theadimg;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRealprice() {
        return realprice;
    }

    public void setRealprice(String realprice) {
        this.realprice = realprice;
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

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getProducttype() {
        return producttype;
    }

    public void setProducttype(int producttype) {
        this.producttype = producttype;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getFtitle() {
        return ftitle;
    }

    public void setFtitle(String ftitle) {
        this.ftitle = ftitle;
    }
}
