package zgt.com.example.myzq.bean.order;

public class Price {
    private String uuid;//价格主键
    private int isdefault;//是否为默认价格，0否，1是
    private int price;//产品定价
    private int pricelimit;//价格期限，0为永久，其他为数字多少天
    private int pricenum;//售价数量
    private int pricesort;//排序
    private int pricestatus;//状态0为不启用，1为启用
    private int priceunit;//	售价单位，0按天，1按月，2季度，3半年，4按年
    private String productid;//产品id
    private int ptype;//类型，1为课程，2为软件，3为聚缘产品
    private int realprice;//通用实际价格

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPricelimit() {
        return pricelimit;
    }

    public void setPricelimit(int pricelimit) {
        this.pricelimit = pricelimit;
    }

    public int getPricenum() {
        return pricenum;
    }

    public void setPricenum(int pricenum) {
        this.pricenum = pricenum;
    }

    public int getPricesort() {
        return pricesort;
    }

    public void setPricesort(int pricesort) {
        this.pricesort = pricesort;
    }

    public int getPricestatus() {
        return pricestatus;
    }

    public void setPricestatus(int pricestatus) {
        this.pricestatus = pricestatus;
    }

    public int getPriceunit() {
        return priceunit;
    }

    public void setPriceunit(int priceunit) {
        this.priceunit = priceunit;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public int getRealprice() {
        return realprice;
    }

    public void setRealprice(int realprice) {
        this.realprice = realprice;
    }
}
