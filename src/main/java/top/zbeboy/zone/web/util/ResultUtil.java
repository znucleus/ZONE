package top.zbeboy.zone.web.util;


import top.zbeboy.zone.web.util.pagination.PaginationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtil {
    private Boolean state;
    private String msg;
    private Map<String, Object> result = new HashMap<>();
    private List<?> listResult = new ArrayList<>();
    private PaginationUtil paginationUtil;

    public static ResultUtil of() {
        return new ResultUtil();
    }

    public ResultUtil success() {
        this.state = true;
        return this;
    }

    public ResultUtil fail() {
        this.state = false;
        return this;
    }

    public ResultUtil msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultUtil list(List<?> listResult) {
        this.listResult = listResult;
        return this;
    }

    public ResultUtil put(String attribute, Object value) {
        this.result.put(attribute, value);
        return this;
    }

    public ResultUtil page(PaginationUtil paginationUtil) {
        this.paginationUtil = paginationUtil;
        return this;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public List<?> getListResult() {
        return listResult;
    }

    public void setListResult(List<?> listResult) {
        this.listResult = listResult;
    }

    public PaginationUtil getPaginationUtil() {
        return paginationUtil;
    }

    public void setPaginationUtil(PaginationUtil paginationUtil) {
        this.paginationUtil = paginationUtil;
    }
}
