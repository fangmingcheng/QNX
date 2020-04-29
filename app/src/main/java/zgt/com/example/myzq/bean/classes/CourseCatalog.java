package zgt.com.example.myzq.bean.classes;

import java.util.ArrayList;
import java.util.List;

public class CourseCatalog {
    private String uuid;//课程目录id
//    private String teacherid;//讲师id
//    private String teachername;//讲师姓名
    private String fileid;//课程id
    private String name;//目录标题
    private int isfee;//0为本课免费，1为本课收费
    private int click;//观看次数
    private boolean isOpen;//是否打开

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private List<CourseContent> list = new ArrayList<>();

    public List<CourseContent> getList() {
        return list;
    }

    public void setList(List<CourseContent> list) {
        this.list = list;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

//    public String getTeacherid() {
//        return teacherid;
//    }
//
//    public void setTeacherid(String teacherid) {
//        this.teacherid = teacherid;
//    }
//
//    public String getTeachername() {
//        return teachername;
//    }
//
//    public void setTeachername(String teachername) {
//        this.teachername = teachername;
//    }

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

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
