package zgt.com.example.myzq.bean.stock;

import java.util.ArrayList;
import java.util.List;

public class StockProduct {
    private String uuid;//分类主键
    private String pname;//分类名称
    private String intro;//
    private String picpath;
    private String url;

    private List<HotStock> list = new ArrayList<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public List<HotStock> getList() {
        return list;
    }

    public void setList(List<HotStock> list) {
        this.list.addAll(list);
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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
