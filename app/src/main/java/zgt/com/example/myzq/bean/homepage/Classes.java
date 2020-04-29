package zgt.com.example.myzq.bean.homepage;

public class Classes {
    private String uuid;//课程主键
    private String picpath;//缩略图地址
    private String title;//直播标题
    private int ischarge;//0为免费，1为收费，2为收费会员
    private String lecturer;//主讲老师
    private double price;//课程价格
    private double realprice;//实际价格
    private int click;//点击率

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIscharge() {
        return ischarge;
    }

    public void setIscharge(int ischarge) {
        this.ischarge = ischarge;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRealprice() {
        return realprice;
    }

    public void setRealprice(double realprice) {
        this.realprice = realprice;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
