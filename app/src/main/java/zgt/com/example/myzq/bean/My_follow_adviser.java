package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/4.
 */

public class My_follow_adviser {
    private String uuid;//关注主键
    private String teachername;//	老师姓名
    private String orderno;//订单号
    private String memberid;//会员主键uuid
    private String teacherid;//老师主键uuid
    private String theadimg;//	老师头像
    private String duties;//	老师职务
    private int fans;//	老师粉丝数量
    private String intro;//	老师简介

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

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTheadimg() {
        return theadimg;
    }

    public void setTheadimg(String theadimg) {
        this.theadimg = theadimg;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
