package zgt.com.example.myzq.bean.lecturer;

public class LecturerDetail {
    private String nickname;//昵称
    private String truename;//真实姓名
    private int sex;//性别
    private String qualification;//资质号
    private String headimg;//老师头像
    private String intro;//老师简介
    private int teacherFileNum;

    public int getTeacherFileNum() {
        return teacherFileNum;
    }

    public void setTeacherFileNum(int teacherFileNum) {
        this.teacherFileNum = teacherFileNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
