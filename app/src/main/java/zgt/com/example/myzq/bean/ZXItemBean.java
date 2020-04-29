package zgt.com.example.myzq.bean;

public class ZXItemBean {
    private String uuid;//资讯栏目id，10 盘面解读，11 头条，12 内参，13 行业 ，14新股
    private String name;//name

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
