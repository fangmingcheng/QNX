package zgt.com.example.myzq.bean.homepage;

public class Recommendedtoday {
    private String uuid;//课程主键
    private String picpath;//缩略图地址
    private String title;//直播标题
    private int click;//点击率

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
