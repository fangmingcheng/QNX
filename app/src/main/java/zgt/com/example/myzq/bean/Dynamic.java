package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/13.
 */

public class Dynamic {
    private String uuid;//信息主键
    private String teachername;//老师名称
    private String headimg;//老师头像
    private int fans;//老师粉丝数量
    private String createtime;//直播创建时间
    private String title;//标题
    private String time;//时间
    private String picpath;//图片路径
    private String ischarge;//是否收费
    private int click;//点击次数
    private int type;//1 直播，2公共观点，3精品课程，4精品视频
    private int livestatus;//0 未开播，1直播，2已结束
    private String content;//内容
    private String summary;//摘要
    private String htmlContent;//带html标签内容

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getIscharge() {
        return ischarge;
    }

    public void setIscharge(String ischarge) {
        this.ischarge = ischarge;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(int livestatus) {
        this.livestatus = livestatus;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
