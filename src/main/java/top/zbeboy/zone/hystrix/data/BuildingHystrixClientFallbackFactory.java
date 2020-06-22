package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Building;
import top.zbeboy.zone.feign.data.BuildingService;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.building.BuildingAddVo;
import top.zbeboy.zbase.vo.data.building.BuildingEditVo;
import top.zbeboy.zbase.vo.data.building.BuildingSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BuildingHystrixClientFallbackFactory implements BuildingService {
    @Override
    public BuildingBean findByIdRelation(int id) {
        return new BuildingBean();
    }

    @Override
    public List<Building> findByCollegeIdAndBuildingIsDel(BuildingSearchVo buildingSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String buildingName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(BuildingAddVo buildingAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int buildingId, String buildingName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(BuildingEditVo buildingEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String buildingIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
