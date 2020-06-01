package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.CollegeApplication;
import top.zbeboy.zone.feign.data.CollegeService;
import top.zbeboy.zone.web.bean.data.college.CollegeBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.college.CollegeAddVo;
import top.zbeboy.zone.web.vo.data.college.CollegeEditVo;
import top.zbeboy.zone.web.vo.data.college.CollegeSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CollegeHystrixClientFallbackFactory implements CollegeService {
    @Override
    public College findById(int id) {
        return new College();
    }

    @Override
    public CollegeBean findByIdRelation(int id) {
        return new CollegeBean();
    }

    @Override
    public List<College> anyoneData(CollegeSearchVo collegeSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String collegeName, int schoolId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddCode(String collegeCode) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(CollegeAddVo collegeAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int collegeId, String collegeName, int schoolId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditCode(int collegeId, String collegeCode) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(CollegeEditVo collegeEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String collegeIds, Byte isDel) {
        return AjaxUtil.of();
    }

    @Override
    public List<TreeViewData> applicationJson() {
        return new ArrayList<>();
    }

    @Override
    public List<CollegeApplication> collegeApplicationData(int collegeId) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> mount(int collegeId, String applicationIds) {
        return AjaxUtil.of();
    }
}