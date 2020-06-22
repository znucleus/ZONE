package top.zbeboy.zone.hystrix.register;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zbase.domain.tables.pojos.EpidemicRegisterRelease;
import top.zbeboy.zone.feign.register.RegisterEpidemicService;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterDataBean;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterDataAddVo;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterReleaseAddVo;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterReleaseEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RegisterEpidemicHystrixClientFallbackFactory implements RegisterEpidemicService {
    @Override
    public EpidemicRegisterRelease release(String id) {
        return new EpidemicRegisterRelease();
    }

    @Override
    public EpidemicRegisterData findTodayByUsernameAndEpidemicRegisterReleaseId(String username, String epidemicRegisterReleaseId) {
        return new EpidemicRegisterData();
    }

    @Override
    public Boolean epidemicOperator(String username) {
        return false;
    }

    @Override
    public Boolean epidemicReview(String username) {
        return false;
    }

    @Override
    public AjaxUtil<EpidemicRegisterReleaseBean> data(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(EpidemicRegisterReleaseAddVo epidemicRegisterReleaseAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(EpidemicRegisterReleaseEditVo epidemicRegisterReleaseEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String epidemicRegisterReleaseId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> dataSave(EpidemicRegisterDataAddVo epidemicRegisterDataAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> dataDelete(String username, String epidemicRegisterDataId) {
        return AjaxUtil.of();
    }

    @Override
    public List<EpidemicRegisterDataBean> export(DataTablesUtil dataTablesUtil) {
        return new ArrayList<>();
    }
}
