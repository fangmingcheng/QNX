package zgt.com.example.myzq.bean;

/**
 * 课件目录
 * Created by ThinkPad on 2019/6/17.
 */

public class Catalog {
    private String uuid;//精品课程目录主键id
    private String fileid;//精品课程主键id
    private String name;//课程目录名称
    private int isfee;//0为免费观看，1为收费观看
    private String videourl;//视频路径
    private int click;//观看次数

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsfee() {
        return isfee;
    }

    public void setIsfee(int isfee) {
        this.isfee = isfee;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
