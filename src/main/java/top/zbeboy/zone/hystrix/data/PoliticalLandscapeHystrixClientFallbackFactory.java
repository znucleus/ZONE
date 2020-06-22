package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.feign.data.PoliticalLandscapeFeignService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.politics.PoliticsAddVo;
import top.zbeboy.zbase.vo.data.politics.PoliticsEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PoliticalLandscapeHystrixClientFallbackFactory implements PoliticalLandscapeFeignService {
    @Override
    public List<PoliticalLandscape> findAll() {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String politicalLandscapeName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(PoliticsAddVo politicsAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int politicalLandscapeId, String politicalLandscapeName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(PoliticsEditVo politicsEditVo) {
        return AjaxUtil.of();
    }
}
