package zgt.com.example.myzq.bean;

import java.io.Serializable;

/**
 * Created by ThinkPad on 2019/6/28.
 */

public class MessageList implements Serializable{

    private String uuid;//系统推送消息列表
    private String msgtitle;//系统推送标题
    private String summary;//摘要

    private String url;//链接网址
    private String msgcontent;//内容
    private String createtime;//创建时间

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgtitle() {
        return msgtitle;
    }

    public void setMsgtitle(String msgtitle) {
        this.msgtitle = msgtitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }


}
