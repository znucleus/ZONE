package top.zbeboy.zone.web.util.pagination;

public class PaginationUtil {
    private int start = 0;
    private int length = 10;
    private String orderColumnName;
    private String orderDir = "asc";
    private String searchValue;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getOrderColumnName() {
        return orderColumnName;
    }

    public void setOrderColumnName(String orderColumnName) {
        this.orderColumnName = orderColumnName;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
