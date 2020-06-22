package top.zbeboy.zone.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.feign.system.SystemConfigureService;

import javax.annotation.Resource;
import java.util.Objects;

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
        return Objects.nonNull(systemConfigure) ? systemConfigure.getDataValue() : "";
    }
}
