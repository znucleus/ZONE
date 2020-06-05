package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.feign.system.ApplicationService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.application.ApplicationAddVo;
import top.zbeboy.zone.web.vo.system.application.ApplicationEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationHystrixClientFallbackFactory implements ApplicationService {
    @Override
    public Application findById(String id) {
        return new Application();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public List<Application> pids() {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String applicationName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(String applicationName, String applicationId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddEnName(String applicationEnName) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditEnName(String applicationEnName, String applicationId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddUrl(String applicationUrl) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditUrl(String applicationUrl, String applicationId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddCode(String applicationCode) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditCode(String applicationCode, String applicationId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(ApplicationAddVo applicationAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(ApplicationEditVo applicationEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String applicationIds) {
        return AjaxUtil.of();
    }
}
