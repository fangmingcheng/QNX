package zgt.com.example.myzq.bean.stock;

public class FeaturedStock {
    private String uuid;//特色选股主键
    private String stockcode;//	股票代码
    private String stockname;//股票名称
    private String historyrecord;//历史涨幅
    private String intro;//	简介
    private String chosendate;//	简介
    private String stockpoolname;//	简介

    public String getChosendate() {
        return chosendate;
    }

    public void setChosendate(String chosendate) {
        this.chosendate = chosendate;
    }

    public String getStockpoolname() {
        return stockpoolname;
    }

    public void setStockpoolname(String stockpoolname) {
        this.stockpoolname = stockpoolname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getHistoryrecord() {
        return historyrecord;
    }

    public void setHistoryrecord(String historyrecord) {
        this.historyrecord = historyrecord;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
