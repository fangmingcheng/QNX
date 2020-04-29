package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/23.
 */

public class Interaction {
    private String uuid;//专属直播主键
    private String teachername;//老师名称
    private String theadimg;//老师头像
    private String msgtime;//互动时间
    private String msgtime1;//互动时间
    private String msgcontent;//互动内容
    private String membername;//会员名称
    private String mheadimg;//会员头像
    private String typename;//版本名称
    private String replycontent;//回复内容
    private int flag;//流向 0为会员，1为老师
    private int isdel;//是否删除 0为否，1为是
    private int isreply;//0为未回复，1为已回复
    private String replytime;//回复时间
    private int replystatus;//0为回复未审核，1为回复已审核


    public int getReplystatus() {
        return replystatus;
    }

    public void setReplystatus(int replystatus) {
        this.replystatus = replystatus;
    }

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

    public String getTheadimg() {
        return theadimg;
    }

    public void setTheadimg(String theadimg) {
        this.theadimg = theadimg;
    }

    public String getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(String msgtime) {
        this.msgtime = msgtime;
    }

    public String getMsgtime1() {
        return msgtime1;
    }

    public void setMsgtime1(String msgtime1) {
        this.msgtime1 = msgtime1;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMheadimg() {
        return mheadimg;
    }

    public void setMheadimg(String mheadimg) {
        this.mheadimg = mheadimg;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public int getIsreply() {
        return isreply;
    }

    public void setIsreply(int isreply) {
        this.isreply = isreply;
    }

    public String getReplytime() {
        return replytime;
    }

    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }
}
