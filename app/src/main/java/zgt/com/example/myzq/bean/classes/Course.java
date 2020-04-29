package zgt.com.example.myzq.bean.classes;

import java.io.Serializable;

public class Course implements Serializable {
    private String title;//
    private String uuid;//课程主键
    private String lecturer;//主讲老师
    private int ischarge;//0为免费，1为收费，2为收费会员
    private String picpath;//	图片路径
    private double price;//课程原价
    private double realprice;//课程实际价格
    private int click;//点击次数

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getIscharge() {
        return ischarge;
    }

    public void setIscharge(int ischarge) {
        this.ischarge = ischarge;
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

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public double getRealprice() {
        return realprice;
    }

    public void setRealprice(double realprice) {
        this.realprice = realprice;
    }
}
