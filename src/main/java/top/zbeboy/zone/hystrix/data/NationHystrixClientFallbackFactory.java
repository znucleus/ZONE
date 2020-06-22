package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Nation;
import top.zbeboy.zone.feign.data.NationService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.nation.NationAddVo;
import top.zbeboy.zbase.vo.data.nation.NationEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NationHystrixClientFallbackFactory implements NationService {
    @Override
    public List<Nation> findAll() {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String nationName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(NationAddVo nationAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int nationId, String nationName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(NationEditVo nationEditVo) {
        return AjaxUtil.of();
    }
}
