package zgt.com.example.myzq.bean.homepage;

public class Advert {
    private String uuid;//广告主键
    private int bsort;//广告排序号
    private int btype;//广告类型
    private int status;//状态
    private String picpath;//	广告图片地址
    private String url;//	广告链接地址


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
