package zgt.com.example.myzq.bean.classes;

import java.io.Serializable;

public class CourseDetail implements Serializable {
    private String uuid;
    private String title;//课程标题
    private String lecturer;//主讲老师
    private int ischarge;//0为免费，1为收费，2为收费会员
    private double price;//课程定价（元）(除ios)
    private double realprice;//实际价格（元）(除ios)
    private int isbuy;//0 为未购买，1为已购买
    private String description;//课程介绍
    private boolean isOpen;//是否打开
    private long liveCount;//课程直播数量
    private long videoCount;//课程视频数量
    private long fileCount;//课程课件数量
    private long audioCount;//

    private int pricelimit;//一次购买的最低天数，为0时代表永久
    private String picpath;//图片路径

    public int getPricelimit() {
        return pricelimit;
    }

    public void setPricelimit(int pricelimit) {
        this.pricelimit = pricelimit;
    }

    public long getAudioCount() {
        return audioCount;
    }

    public void setAudioCount(long audioCount) {
        this.audioCount = audioCount;
    }

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

    public long getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(long liveCount) {
        this.liveCount = liveCount;
    }

    public long getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(long videoCount) {
        this.videoCount = videoCount;
    }

    public long getFileCount() {
        return fileCount;
    }

    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public int getIscharge() {
        return ischarge;
    }

    public void setIscharge(int ischarge) {
        this.ischarge = ischarge;
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

    public int getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(int isbuy) {
        this.isbuy = isbuy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
