package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/19.
 */

public class MyMember {
    private String typename;//版本名称
    private int tsort;//版本排序优先级
    private int type;//-1非会员，1基础版，2博弈版，3至尊版，4钻石版，5vip版
    private String teacherid;//老师主键id
    private String startdate;//订单开始时间
    private String enddate;//订单结束时间

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getTsort() {
        return tsort;
    }

    public void setTsort(int tsort) {
        this.tsort = tsort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
