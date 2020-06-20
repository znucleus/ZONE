package top.zbeboy.zone.hystrix.register;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.feign.register.RegisterLeaverService;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterDataVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RegisterLeaverHystrixClientFallbackFactory implements RegisterLeaverService {
    @Override
    public LeaverRegisterRelease release(String id) {
        return new LeaverRegisterRelease();
    }

    @Override
    public Boolean leaverReview(String username, String leaverRegisterReleaseId) {
        return false;
    }

    @Override
    public Boolean leaverRegister(String username, String leaverRegisterReleaseId) {
        return false;
    }

    @Override
    public Boolean leaverOperator(String username, String leaverRegisterReleaseId) {
        return null;
    }

    @Override
    public AjaxUtil<LeaverRegisterReleaseBean> data(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> optionDelete(String leaverRegisterOptionId, String leaverRegisterReleaseId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> optionUpdate(String leaverRegisterOptionId, String leaverRegisterReleaseId, String optionContent, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String leaverRegisterReleaseId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> dataSave(LeaverRegisterDataVo leaverRegisterDataVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> dataDelete(String leaverRegisterReleaseId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> dataListDelete(String leaverRegisterReleaseId, String leaverRegisterDataId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<LeaverRegisterDataBean> dataList(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public List<LeaverRegisterDataBean> export(SimplePaginationUtil simplePaginationUtil) {
        return new ArrayList<>();
    }

    @Override
    public List<LeaverRegisterOption> leaverRegisterOptions(String LeaverRegisterReleaseId) {
        return new ArrayList<>();
    }

    @Override
    public List<LeaverRegisterScopeBean> leaverRegisterScopes(String LeaverRegisterReleaseId) {
        return new ArrayList<>();
    }
}
