package top.zbeboy.zone.web.system.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Application;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.ApplicationService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.system.application.ApplicationAddVo;
import top.zbeboy.zbase.vo.system.application.ApplicationEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SystemApplicationRestController {

    @Resource
    private ApplicationService applicationService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/application/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("applicationName");
        headers.add("applicationEnName");
        headers.add("applicationPid");
        headers.add("applicationUrl");
        headers.add("icon");
        headers.add("applicationSort");
        headers.add("applicationCode");
        headers.add("applicationDataUrlStartWith");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(applicationService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 获取父id对应数据
     *
     * @return 数据
     */
    @GetMapping("/web/system/application/pids")
    public ResponseEntity<Map<String, Object>> pids() {
        AjaxUtil<Application> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(applicationService.pids()).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验保存时应用名是否重复
     *
     * @param applicationName 应用名
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("applicationName") String applicationName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkAddName(applicationName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验更新时应用名是否重复
     *
     * @param applicationName 应用名
     * @param applicationId   应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("applicationName") String applicationName,
                                                             @RequestParam("applicationId") String applicationId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkEditName(applicationName, applicationId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验保存时应用英文名是否重复
     *
     * @param applicationEnName 应用英文名
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/add/en_name")
    public ResponseEntity<Map<String, Object>> checkAddEnName(@RequestParam("applicationEnName") String applicationEnName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkAddEnName(applicationEnName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验更新时应用英文名是否重复
     *
     * @param applicationEnName 应用英文名
     * @param applicationId     应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/edit/en_name")
    public ResponseEntity<Map<String, Object>> checkEditEnName(@RequestParam("applicationEnName") String applicationEnName,
                                                               @RequestParam("applicationId") String applicationId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkEditEnName(applicationEnName, applicationId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验保存时应用链接是否重复
     *
     * @param applicationUrl 应用链接
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/add/url")
    public ResponseEntity<Map<String, Object>> checkAddUrl(@RequestParam("applicationUrl") String applicationUrl) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkAddUrl(applicationUrl);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验更新时应用链接是否重复
     *
     * @param applicationUrl 应用链接
     * @param applicationId  应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/edit/url")
    public ResponseEntity<Map<String, Object>> checkEditUrl(@RequestParam("applicationUrl") String applicationUrl,
                                                            @RequestParam("applicationId") String applicationId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkEditUrl(applicationUrl, applicationId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验保存时应用识别码是否重复
     *
     * @param applicationCode 应用识别码
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/add/code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("applicationCode") String applicationCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkAddCode(applicationCode);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验更新时应用识别码是否重复
     *
     * @param applicationCode 应用识别码
     * @param applicationId   应用id
     * @return true 不重复 false重复
     */
    @PostMapping("/web/system/application/check/edit/code")
    public ResponseEntity<Map<String, Object>> checkEditCode(@RequestParam("applicationCode") String applicationCode,
                                                             @RequestParam("applicationId") String applicationId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.checkEditCode(applicationCode, applicationId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存应用信息
     *
     * @param applicationAddVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/application/save")
    public ResponseEntity<Map<String, Object>> save(ApplicationAddVo applicationAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.save(applicationAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新应用信息
     *
     * @param applicationEditVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/application/update")
    public ResponseEntity<Map<String, Object>> update(ApplicationEditVo applicationEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.update(applicationEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除应用
     *
     * @param applicationIds 应用ids
     * @return true删除成功
     */
    @PostMapping("/web/system/application/status")
    public ResponseEntity<Map<String, Object>> status(String applicationIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = applicationService.status(applicationIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
