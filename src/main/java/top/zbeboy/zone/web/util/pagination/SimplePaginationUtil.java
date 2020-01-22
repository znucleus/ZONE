package top.zbeboy.zone.web.util.pagination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.Principal;

public class SimplePaginationUtil extends PaginationUtil {
    private int pageNum;
    private int displayedPages;
    private int totalPages;
    private int totalSize;
    private String searchName;
    private String extraSearch;
    private JSONObject search;
    private boolean isToJson;
    private Principal principal;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getDisplayedPages() {
        if (getTotalPages() > 3 || getTotalPages() == 1) {
            displayedPages = 3;
        } else {
            displayedPages = totalPages;
        }
        return displayedPages;
    }

    public void setDisplayedPages(int displayedPages) {
        this.displayedPages = displayedPages;
    }

    public int getTotalPages() {
        if (totalSize % getLength() == 0) {
            totalPages = totalSize / getLength();
        } else {
            totalPages = totalSize / getLength() + 1;
        }
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

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

    @Override
    public int getStart() {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        return (pageNum - 1) * getLength();
    }

    @Override
    public void setStart(int start) {
        super.setStart(start);
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

    public void setSearch(String key, String value) {
        getSearch();
        if (BooleanUtils.isTrue(this.isToJson)) {
            this.search.put(key, value);
        }
    }

    @JsonIgnore
    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
