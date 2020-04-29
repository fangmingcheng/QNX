package zgt.com.example.myzq.bean.basic;

public class BasicInformation {
    private String truename;
    private String idcardno;
    private String occupation;
    private String education;
    private String idtype;
    private String tel;
    private String idcardfrontpath;
    private String idcardbackpath;
    private String creditrecord;
    private int ismodifyInformation;//是否允许修改会员信息0不允许，1允许


    public int getIsmodifyInformation() {
        return ismodifyInformation;
    }

    public void setIsmodifyInformation(int ismodifyInformation) {
        this.ismodifyInformation = ismodifyInformation;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdcardfrontpath() {
        return idcardfrontpath;
    }

    public void setIdcardfrontpath(String idcardfrontpath) {
        this.idcardfrontpath = idcardfrontpath;
    }

    public String getIdcardbackpath() {
        return idcardbackpath;
    }

    public void setIdcardbackpath(String idcardbackpath) {
        this.idcardbackpath = idcardbackpath;
    }

    public String getCreditrecord() {
        return creditrecord;
    }

    public void setCreditrecord(String creditrecord) {
        this.creditrecord = creditrecord;
    }


}
