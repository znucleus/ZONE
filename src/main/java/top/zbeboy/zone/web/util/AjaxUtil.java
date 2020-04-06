package top.zbeboy.zone.web.util;

import top.zbeboy.zone.web.util.pagination.PaginationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxUtil<T> {
    private Boolean state;
    private String msg;
    private Map<String, T> mapResult = new HashMap<>();
    private List<T> listResult = new ArrayList<>();
    private Map<String, Object> result = new HashMap<>();
    private PaginationUtil paginationUtil;

    public static <T> AjaxUtil<T> of() {
        return new AjaxUtil<>();
    }

    public AjaxUtil<T> success() {
        this.state = true;
        return this;
    }

    public AjaxUtil<T> fail() {
        this.state = false;
        return this;
    }

    public AjaxUtil<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public AjaxUtil<T> list(List<T> listResult) {
        this.listResult = listResult;
        return this;
    }

    public AjaxUtil<T> map(Map<String, T> mapResult) {
        this.mapResult = mapResult;
        return this;
    }

    public AjaxUtil<T> put(String attribute, Object value) {
        this.result.put(attribute, value);
        return this;
    }

    public AjaxUtil<T> page(PaginationUtil paginationUtil) {
        this.paginationUtil = paginationUtil;
        return this;
    }

    public Map<String, Object> send() {
        this.result.put("state", this.state);
        this.result.put("msg", this.msg);
        this.result.put("mapResult", this.mapResult);
        this.result.put("listResult", this.listResult);
        this.result.put("page", this.paginationUtil);
        return this.result;
    }

    public Boolean getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, T> getMapResult() {
        return mapResult;
    }

    public List<T> getListResult() {
        return listResult;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public PaginationUtil getPaginationUtil() {
        return paginationUtil;
    }
}
