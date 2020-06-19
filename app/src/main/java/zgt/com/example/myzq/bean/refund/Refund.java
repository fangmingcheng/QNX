package zgt.com.example.myzq.bean.refund;

public class Refund {
    private String orderid;//订单主键
    private String title;//标题(课程)
    private String lecturer ;//	讲师（课程）
    private String picpath;//课程图片路径
    private int pricenum;//售价数量
    private int priceunit;//售价单位，0按天，1按月，2季度，3半年，2按年
    private int pricelimit;//价格期限
    private String orderno;//订单号
    private double iosprice;//iOS实际价格 （牵牛币
    private double realmoney;
    private double price;//实际价格
    private int producttype;//订单类型，为课程，2为软件，3为套餐
    private int isnewversion;//是否新版本订单，0老订单，1新订单

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(double realmoney) {
        this.realmoney = realmoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public int getPricenum() {
        return pricenum;
    }

    public void setPricenum(int pricenum) {
        this.pricenum = pricenum;
    }

    public int getPriceunit() {
        return priceunit;
    }

    public void setPriceunit(int priceunit) {
        this.priceunit = priceunit;
    }

    public int getPricelimit() {
        return pricelimit;
    }

    public void setPricelimit(int pricelimit) {
        this.pricelimit = pricelimit;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public double getIosprice() {
        return iosprice;
    }

    public void setIosprice(double iosprice) {
        this.iosprice = iosprice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProducttype() {
        return producttype;
    }

    public void setProducttype(int producttype) {
        this.producttype = producttype;
    }

    public int getIsnewversion() {
        return isnewversion;
    }

    public void setIsnewversion(int isnewversion) {
        this.isnewversion = isnewversion;
    }
}
