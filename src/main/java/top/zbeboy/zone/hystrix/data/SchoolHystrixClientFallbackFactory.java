package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.School;
import top.zbeboy.zone.feign.data.SchoolService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.school.SchoolAddVo;
import top.zbeboy.zbase.vo.data.school.SchoolEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SchoolHystrixClientFallbackFactory implements SchoolService {
    @Override
    public School findById(int id) {
        return new School();
    }

    @Override
    public List<School> findNormal() {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String schoolName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(SchoolAddVo schoolAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int id, String schoolName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(SchoolEditVo schoolEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String schoolIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
