package zgt.com.example.myzq.bean.stock;

import java.util.ArrayList;
import java.util.List;

public class AllOProductModule {
    private String uuid;
    private List<FeaturedStock> list = new ArrayList<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<FeaturedStock> getList() {
        return list;
    }

    public void setList(List<FeaturedStock> list) {
        this.list.addAll(list);
    }
}
