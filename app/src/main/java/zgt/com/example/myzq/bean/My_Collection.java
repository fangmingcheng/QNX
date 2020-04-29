package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/8/5.
 */

public class My_Collection {
    private String uuid;//观点收藏主键id
    private String publicviewid;//公共观点id
    private String privateviewid;//专属观点id
    private String memberid;//会员主键id
    private int type;//1公共观点 2专属观点
    private String collectiontime;//收藏时间
    private String content;//内容
    private String htmlContent;
    private String summary;//摘要
    private String addtime;//发布时间
    private int click;//点击率
    private String teachername;//老师名称
    private String teacherheadimg;//老师头像

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPublicviewid() {
        return publicviewid;
    }

    public void setPublicviewid(String publicviewid) {
        this.publicviewid = publicviewid;
    }

    public String getPrivateviewid() {
        return privateviewid;
    }

    public void setPrivateviewid(String privateviewid) {
        this.privateviewid = privateviewid;
    }

    public String getMemberid() {
        return memberid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCollectiontime() {
        return collectiontime;
    }

    public void setCollectiontime(String collectiontime) {
        this.collectiontime = collectiontime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacherheadimg() {
        return teacherheadimg;
    }

    public void setTeacherheadimg(String teacherheadimg) {
        this.teacherheadimg = teacherheadimg;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
