package zgt.com.example.myzq.bean.homepage;

/**
 * Created by ThinkPad on 2019/6/13.
 */

public class banner {
    private String uuid;//banner主键
    private String picpath;//图片路径
    private String url;//链接地址
    private int bsort;//banner排序号
    private int status;//链接地址
    private int btype;//状态


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getBsort() {
        return bsort;
    }

    public void setBsort(int bsort) {
        this.bsort = bsort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }
}
