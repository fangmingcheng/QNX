package zgt.com.example.myzq.bean.stock;

public class AiStockPrice {
    private String aiStockId;//智能选股栏目主键
    private String name;//栏目名称
    private double price;//	定价
    private double realprice;//	实际价格
    private int pricenum;//	售价数量
    private int priceunit;//售价单位，0按天，1按月，2季度，3半年，2按年
    private int isbuy;//是否购买，0未购买，1已购买

    public String getAiStockId() {
        return aiStockId;
    }

    public void setAiStockId(String aiStockId) {
        this.aiStockId = aiStockId;
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

    public int getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(int isbuy) {
        this.isbuy = isbuy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
