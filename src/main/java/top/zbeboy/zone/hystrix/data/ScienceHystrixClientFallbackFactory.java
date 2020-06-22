package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Science;
import top.zbeboy.zone.feign.data.ScienceService;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.science.ScienceAddVo;
import top.zbeboy.zbase.vo.data.science.ScienceEditVo;
import top.zbeboy.zbase.vo.data.science.ScienceSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ScienceHystrixClientFallbackFactory implements ScienceService {
    @Override
    public Science findById(int id) {
        return new Science();
    }

    @Override
    public ScienceBean findByIdRelation(int id) {
        return new ScienceBean();
    }

    @Override
    public List<Science> findByDepartmentIdAndScienceIsDel(ScienceSearchVo scienceSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String scienceName, int departmentId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddCode(String scienceCode) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(ScienceAddVo scienceAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int scienceId, String scienceName, int departmentId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditCode(int scienceId, String scienceCode) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(ScienceEditVo scienceEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String scienceIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
