package zgt.com.example.myzq.bean.classes;

public class CourseContent {
    private String uuid;//课程内容主键
    private String menuid;//目录id
    private String kname;//内容标题
    private int ktype;//课程类别 1为直播，2为视频，3为课件
    private String livetime;//直播时间
    private int livestatus;//直播状态 0未开播，1为直播中，2为已结束
    private int flag;//字体颜色 0为灰色，1为黑色，2为蓝色
    private int click;//观看次数

    private String teacherid;
    private String teachername;
    private String videoduration;

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

    public String getVideoduration() {
        return videoduration;
    }

    public void setVideoduration(String videoduration) {
        this.videoduration = videoduration;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public int getKtype() {
        return ktype;
    }

    public void setKtype(int ktype) {
        this.ktype = ktype;
    }

    public String getLivetime() {
        return livetime;
    }

    public void setLivetime(String livetime) {
        this.livetime = livetime;
    }

    public int getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(int livestatus) {
        this.livestatus = livestatus;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
