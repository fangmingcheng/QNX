package zgt.com.example.myzq.bean.classes;

import java.io.Serializable;

public class Mycourse implements Serializable {
    private String uuid;//我的课程主键
    private String memberid;//会员id
    private String typeid;//课程id
    private int producttype;//1课程
    private String expiredate;//到期日期
    private String title;//课程标题
    private String picpath;//课程封面图片路径
    private double price;//	版本定价
    private double realprice;//成交价格
    private String lecturer;//主讲老师
    private int pricelimit;//一次购买的最低天数，为0时代表永久
    private int status;//课程状态 ，0已到期，1未到期

    public int getPricelimit() {
        return pricelimit;
    }

    public void setPricelimit(int pricelimit) {
        this.pricelimit = pricelimit;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
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

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
