package zgt.com.example.myzq.bean;

/**
 * 精品课程
 * Created by ThinkPad on 2019/6/14.
 */

public class Courseware {
    private String uuid;//精品课程主键
    private String teacherid;//老师主键
    private String teachername;//	老师名称
    private String headimg;//老师头像
    private String duties;//老师职位
    private String title;//课程标题
    private String description;//描述
    private String picpath;//缩略图路径
    private String videourl;//课程路径
    private int ischarge;//是否收费 0为免费，1为收费，2为收费会员
    private double price;//课程价格
    private int status;//状态 0未审核，1为已审核
    private String addtime;//发布时间
    private int click;//观看次数
    private int totalnum;//总节数
    private double realprice;//实际价格
    private String intro;//简介
    private int isend;//是否完结

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public int getIscharge() {
        return ischarge;
    }

    public void setIscharge(int ischarge) {
        this.ischarge = ischarge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public double getRealprice() {
        return realprice;
    }

    public void setRealprice(double realprice) {
        this.realprice = realprice;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIsend() {
        return isend;
    }

    public void setIsend(int isend) {
        this.isend = isend;
    }
}
