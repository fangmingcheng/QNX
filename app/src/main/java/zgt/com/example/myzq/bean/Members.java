package zgt.com.example.myzq.bean;

/**
 * Created by ThinkPad on 2019/6/19.
 */

public class Members {
    private String uuid;//版本主键id
    private String typename;//版本名称
    private String intro;//版本介绍
    private String modulenamestr;//模块名称串
    private int price;//版本价格

    private String startDate;
    private String endDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getModulenamestr() {
        return modulenamestr;
    }

    public void setModulenamestr(String modulenamestr) {
        this.modulenamestr = modulenamestr;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
