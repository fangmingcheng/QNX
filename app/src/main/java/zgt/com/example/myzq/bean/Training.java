package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/13.
 */

public class Training {
    private String uuid;//新闻主键
    private String nclassid;//栏目
    private String title;//标题
    private String ftitle;//副标题
    private String summary;//摘要
    private String author;//作者
    private String picpath;//	图片路径
    private String videoflag;//视频来源
    private String videopath;//视频路径
    private String content;//内容
    private String htmlContent;//html内容
    private String createtime;//创建时间
    private String click;//	浏览次数

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNclassid() {
        return nclassid;
    }

    public void setNclassid(String nclassid) {
        this.nclassid = nclassid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFtitle() {
        return ftitle;
    }

    public void setFtitle(String ftitle) {
        this.ftitle = ftitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getVideoflag() {
        return videoflag;
    }

    public void setVideoflag(String videoflag) {
        this.videoflag = videoflag;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
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

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
