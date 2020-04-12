package top.zbeboy.zone.web.util.pagination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.service.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
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
    /*
    导出数据信息
    */
    private ExportInfo exportInfo;

    public SimplePaginationUtil(){}

    public SimplePaginationUtil(HttpServletRequest request, String orderColumnName, String orderDir, String fileName, String path) {
        String extraSearchParam = request.getParameter("extra_search");
        this.setOrderDir(orderDir);
        this.setOrderColumnName(orderColumnName);
        this.extraSearch = extraSearchParam;
        this.search = JSON.parseObject(extraSearchParam);

        this.exportInfo = JSON.parseObject(request.getParameter("export_info"), ExportInfo.class);
        if (StringUtils.isBlank(exportInfo.getFileName())) {
            exportInfo.setFileName(fileName);
        }

        if (StringUtils.isBlank(exportInfo.getExt())) {
            exportInfo.setExt(Workbook.fileSuffix.xlsx.name());
        }

        exportInfo.setPath(path);
        exportInfo.setFilePath(path + exportInfo.getFileName() + "." + exportInfo.getExt());
        exportInfo.setLastPath(RequestUtil.getRealPath(request) + path);
    }

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

    public ExportInfo getExportInfo() {
        return exportInfo;
    }

    public void setExportInfo(ExportInfo exportInfo) {
        this.exportInfo = exportInfo;
    }
}
