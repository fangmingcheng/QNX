package zgt.com.example.myzq.bean.homepage;

public class Advert {
    private String uuid;//广告主键
    private int bsort;//广告排序号
    private int btype;//广告类型
    private int status;//状态
    private String picpath;//	广告图片地址
    private String url;//	广告链接地址

    private int redirecttype;//跳转类型 1为h5网页，2为小程序，3为原生交互
    private String apptype;//原生类型交互类型

    public int getRedirecttype() {
        return redirecttype;
    }

    public void setRedirecttype(int redirecttype) {
        this.redirecttype = redirecttype;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getBsort() {
        return bsort;
    }

    public void setBsort(int bsort) {
        this.bsort = bsort;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
