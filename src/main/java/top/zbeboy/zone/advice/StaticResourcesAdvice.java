package top.zbeboy.zone.advice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.service.system.SystemConfigureService;

import javax.annotation.Resource;

@ControllerAdvice
public class StaticResourcesAdvice {

    @Resource
    private SystemConfigureService systemConfigureService;

    /**
     * 获取静态资源版本
     *
     * @return 版本号
     */
    @ModelAttribute("staticResourceVersion")
    public String version() {
        SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.STATIC_RESOURCES_VERSION.name());
        return StringUtils.defaultString(systemConfigure.getDataValue());
    }
}
