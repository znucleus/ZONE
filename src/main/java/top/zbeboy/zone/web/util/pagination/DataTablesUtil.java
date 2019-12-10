package top.zbeboy.zone.web.util.pagination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class DataTablesUtil extends PaginationUtil {
    /*
    返回的数据
     */
    private List<?> data;

    private int draw;

    /*
    数据总数
     */
    private long iTotalRecords;

    /*
    过滤条件下的总数
     */
    private long iTotalDisplayRecords;

    /*
   哪列排序 是索引
    */
    private int orderColumn;

    /*
    列
     */
    private List<String> headers;

    /*
    额外搜索参数
     */
    private String extraSearch;

    /*
    当前页号
     */
    private int extraPage;

    /*
    object extraSearch
     */
    private JSONObject search;

    public DataTablesUtil(HttpServletRequest request, List<String> headers) {
        String startParam = request.getParameter("start");
        String lengthParam = request.getParameter("length");
        String orderColumnParam = request.getParameter("order[0][column]");
        String orderDirParam = request.getParameter("order[0][dir]");
        String searchValueParam = request.getParameter("search[value]");
        String extraSearchParam = request.getParameter("extra_search");
        String extraPage = request.getParameter("extra_page");
        String dramParam = request.getParameter("draw");

        if (NumberUtils.isDigits(startParam)) {
            this.setStart(NumberUtils.toInt(startParam));
        }

        if (NumberUtils.isDigits(lengthParam)) {
            this.setLength(NumberUtils.toInt(lengthParam));
        }

        if (NumberUtils.isDigits(orderColumnParam)) {
            this.orderColumn = NumberUtils.toInt(orderColumnParam);
        }

        if (Objects.nonNull(headers) && !headers.isEmpty() && headers.size() > this.orderColumn) {
            this.setOrderColumnName(headers.get(this.orderColumn));
            this.headers = headers;
        }

        if (StringUtils.isNotBlank(orderDirParam)) {
            this.setOrderDir(orderDirParam);
        }

        if (StringUtils.isNotBlank(searchValueParam)) {
            this.setSearchValue(searchValueParam);
        }

        if (StringUtils.isNotBlank(extraSearchParam)) {
            this.extraSearch = extraSearchParam;
            this.search = JSON.parseObject(extraSearchParam);
        }

        if (NumberUtils.isDigits(extraPage)) {
            this.extraPage = NumberUtils.toInt(extraPage);
        }

        if (NumberUtils.isDigits(dramParam)) {
            this.draw = NumberUtils.toInt(dramParam);
        }
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(int orderColumn) {
        this.orderColumn = orderColumn;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getExtraSearch() {
        return extraSearch;
    }

    public void setExtraSearch(String extraSearch) {
        this.extraSearch = extraSearch;
    }

    public int getExtraPage() {
        return extraPage;
    }

    public void setExtraPage(int extraPage) {
        this.extraPage = extraPage;
    }

    public JSONObject getSearch() {
        return search;
    }

    public void setSearch(JSONObject search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("data", data)
                .append("draw", draw)
                .append("iTotalRecords", iTotalRecords)
                .append("iTotalDisplayRecords", iTotalDisplayRecords)
                .append("orderColumn", orderColumn)
                .append("headers", headers)
                .append("extraSearch", extraSearch)
                .append("extraPage", extraPage)
                .append("search", search)
                .toString();
    }
}