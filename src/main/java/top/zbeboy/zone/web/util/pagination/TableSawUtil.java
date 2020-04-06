package top.zbeboy.zone.web.util.pagination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class TableSawUtil extends PaginationUtil {
    private int totalSize;
    private String searchName;
    private String extraSearch;
    private JSONObject search;
    private boolean isToJson;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getExtraSearch() {
        return extraSearch;
    }

    public void setExtraSearch(String extraSearch) {
        this.extraSearch = extraSearch;
    }

    public JSONObject getSearch() {
        if (StringUtils.isNotBlank(this.extraSearch) && BooleanUtils.isFalse(this.isToJson)) {
            this.isToJson = true;
            this.search = JSON.parseObject(this.extraSearch);
        }
        return search;
    }

    public void setSearch(String key, Object value) {
        getSearch();
        if (BooleanUtils.isTrue(this.isToJson)) {
            this.search.put(key, value);
        }
    }
}
