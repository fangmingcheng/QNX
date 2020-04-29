package zgt.com.example.myzq.bean;

import java.io.Serializable;

public class Review implements Serializable {
    private String name;//回访名称
    private String url;//回访地址

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
