package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Schoolroom;
import top.zbeboy.zone.feign.data.SchoolroomService;
import top.zbeboy.zone.web.bean.data.schoolroom.SchoolroomBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomAddVo;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomEditVo;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SchoolroomHystrixClientFallbackFactory implements SchoolroomService {
    @Override
    public SchoolroomBean findByIdRelation(int id) {
        return new SchoolroomBean();
    }

    @Override
    public List<Schoolroom> findByBuildingIdAndSchoolroomIsDel(SchoolroomSearchVo schoolroomSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddCode(String buildingCode, int buildingId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(SchoolroomAddVo schoolroomAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditCode(int schoolroomId, String buildingCode, int buildingId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(SchoolroomEditVo schoolroomEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String schoolroomIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
