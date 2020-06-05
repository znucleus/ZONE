package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.hystrix.system.ApplicationHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.application.ApplicationAddVo;
import top.zbeboy.zone.web.vo.system.application.ApplicationEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = ApplicationHystrixClientFallbackFactory.class)
public interface ApplicationService {

    /**
     * 获取应用
     *
     * @param id 应用主键
     * @return 数据
     */
    @GetMapping("/base/system/application/{id}")
    Application findById(@PathVariable("id") String id);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/application/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 获取父id对应数据
     *
     * @return 数据
     */
    @GetMapping("/base/system/application/pids")
    List<Application> pids();

    /**
     * 检验保存时应用名是否重复
     *
     * @param applicationName 应用名
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("applicationName") String applicationName);

    /**
     * 检验更新时应用名是否重复
     *
     * @param applicationName 应用名
     * @param applicationId   应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("applicationName") String applicationName, @RequestParam("applicationId") String applicationId);

    /**
     * 检验保存时应用英文名是否重复
     *
     * @param applicationEnName 应用英文名
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/add/en_name")
    AjaxUtil<Map<String, Object>> checkAddEnName(@RequestParam("applicationEnName") String applicationEnName);

    /**
     * 检验更新时应用英文名是否重复
     *
     * @param applicationEnName 应用英文名
     * @param applicationId     应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/edit/en_name")
    AjaxUtil<Map<String, Object>> checkEditEnName(@RequestParam("applicationEnName") String applicationEnName, @RequestParam("applicationId") String applicationId);

    /**
     * 检验保存时应用链接是否重复
     *
     * @param applicationUrl 应用链接
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/add/url")
    AjaxUtil<Map<String, Object>> checkAddUrl(@RequestParam("applicationUrl") String applicationUrl);

    /**
     * 检验更新时应用链接是否重复
     *
     * @param applicationUrl 应用链接
     * @param applicationId  应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/edit/url")
    AjaxUtil<Map<String, Object>> checkEditUrl(@RequestParam("applicationUrl") String applicationUrl, @RequestParam("applicationId") String applicationId);

    /**
     * 检验保存时应用识别码是否重复
     *
     * @param applicationCode 应用识别码
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/add/code")
    AjaxUtil<Map<String, Object>> checkAddCode(@RequestParam("applicationCode") String applicationCode);

    /**
     * 检验更新时应用识别码是否重复
     *
     * @param applicationCode 应用识别码
     * @param applicationId   应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/base/system/application/check/edit/code")
    AjaxUtil<Map<String, Object>> checkEditCode(@RequestParam("applicationCode") String applicationCode, @RequestParam("applicationId") String applicationId);

    /**
     * 保存应用信息
     *
     * @param applicationAddVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/system/application/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody ApplicationAddVo applicationAddVo);

    /**
     * 更新应用信息
     *
     * @param applicationEditVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/system/application/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody ApplicationEditVo applicationEditVo);

    /**
     * 批量删除应用
     *
     * @param applicationIds 应用ids
     * @return true删除成功
     */
    @PostMapping("/base/system/application/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "applicationIds", required = false) String applicationIds);
}
