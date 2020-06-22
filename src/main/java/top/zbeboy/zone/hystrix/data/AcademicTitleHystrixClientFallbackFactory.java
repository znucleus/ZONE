package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.feign.data.AcademicTitleService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.academic.AcademicAddVo;
import top.zbeboy.zbase.vo.data.academic.AcademicEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AcademicTitleHystrixClientFallbackFactory implements AcademicTitleService {
    @Override
    public List<AcademicTitle> findAll() {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String academicTitleName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AcademicAddVo academicAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int academicTitleId, String academicTitleName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(AcademicEditVo academicEditVo) {
        return AjaxUtil.of();
    }
}
