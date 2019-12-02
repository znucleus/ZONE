package top.zbeboy.zone.web.system.application;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.domain.tables.records.ApplicationRecord;
import top.zbeboy.zone.service.data.CollegeApplicationService;
import top.zbeboy.zone.service.platform.RoleApplicationService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.system.ApplicationService;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.system.application.ApplicationBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.application.ApplicationAddVo;
import top.zbeboy.zone.web.vo.system.application.ApplicationEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class SystemApplicationRestController {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private RoleService roleService;

    @Resource
    private RoleApplicationService roleApplicationService;

    @Resource
    private CollegeApplicationService collegeApplicationService;

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
        Result<Record> records = applicationService.findAllByPage(dataTablesUtil);
        List<ApplicationBean> applicationBeen = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            applicationBeen = records.into(ApplicationBean.class);
            applicationBeen.forEach(a -> {
                if (StringUtils.equals("0", a.getApplicationPid())) {
                    a.setApplicationPidName("无");
                } else {
                    Application application = applicationService.findById(a.getApplicationPid());
                    a.setApplicationPidName(application.getApplicationName());
                }
            });
        }
        dataTablesUtil.setData(applicationBeen);
        dataTablesUtil.setiTotalRecords(applicationService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(applicationService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 获取父id对应数据
     *
     * @return 数据
     */
    @GetMapping("/web/system/application/pids")
    public ResponseEntity<Map<String, Object>> pids() {
        AjaxUtil<Application> ajaxUtil = AjaxUtil.of();
        List<Application> applicationPids = new ArrayList<>();
        Application application = new Application();
        application.setApplicationId("0");
        application.setApplicationName("无");
        applicationPids.add(application);
        applicationPids.addAll(applicationService.findByPid("0"));
        ajaxUtil.success().list(applicationPids).msg("获取数据成功");
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationName);
        List<Application> applications = applicationService.findByApplicationName(param);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用名不存在");
        } else {
            ajaxUtil.fail().msg("应用名已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationName);
        Result<ApplicationRecord> applications = applicationService.findByApplicationNameNeApplicationId(param, applicationId);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用名不存在");
        } else {
            ajaxUtil.fail().msg("应用名已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationEnName);
        List<Application> applications = applicationService.findByApplicationEnName(param);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用英文名不存在");
        } else {
            ajaxUtil.fail().msg("应用英文名已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationEnName);
        Result<ApplicationRecord> applications = applicationService.findByApplicationEnNameNeApplicationId(param, applicationId);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用英文名不存在");
        } else {
            ajaxUtil.fail().msg("应用英文名已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationUrl);
        List<Application> applications = applicationService.findByApplicationUrl(param);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用链接不存在");
        } else {
            ajaxUtil.fail().msg("应用链接已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationUrl);
        Result<ApplicationRecord> applications = applicationService.findByApplicationUrlNeApplicationId(param, applicationId);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用链接不存在");
        } else {
            ajaxUtil.fail().msg("应用链接已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationCode);
        List<Application> applications = applicationService.findByApplicationCode(param);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用识别码不存在");
        } else {
            ajaxUtil.fail().msg("应用识别码已存在");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(applicationCode);
        Result<ApplicationRecord> applications = applicationService.findByApplicationCodeNeApplicationId(param, applicationId);
        if (applications.isEmpty()) {
            ajaxUtil.success().msg("应用识别码不存在");
        } else {
            ajaxUtil.fail().msg("应用识别码已存在");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存应用信息
     *
     * @param applicationAddVo 应用
     * @param bindingResult    检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/application/save")
    public ResponseEntity<Map<String, Object>> save(@Valid ApplicationAddVo applicationAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Application application = new Application();
            application.setApplicationId(UUIDUtil.getUUID());
            application.setApplicationName(applicationAddVo.getApplicationName());
            application.setApplicationSort(applicationAddVo.getApplicationSort());
            application.setApplicationPid(applicationAddVo.getApplicationPid());
            application.setApplicationUrl(applicationAddVo.getApplicationUrl());
            application.setApplicationCode(applicationAddVo.getApplicationCode());
            application.setApplicationEnName(applicationAddVo.getApplicationEnName());
            application.setIcon(applicationAddVo.getIcon());
            application.setApplicationDataUrlStartWith(applicationAddVo.getApplicationDataUrlStartWith());

            applicationService.save(application);

            Role role = roleService.findByRoleEnName(Workbook.authorities.ROLE_SYSTEM.name());
            RoleApplication roleApplication = new RoleApplication(role.getRoleId(), application.getApplicationId());
            roleApplicationService.save(roleApplication);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新应用信息
     *
     * @param applicationEditVo 应用
     * @param bindingResult     检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/application/update")
    public ResponseEntity<Map<String, Object>> update(@Valid ApplicationEditVo applicationEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Application application = applicationService.findById(applicationEditVo.getApplicationId());
            application.setApplicationName(applicationEditVo.getApplicationName());
            application.setApplicationSort(applicationEditVo.getApplicationSort());
            application.setApplicationPid(applicationEditVo.getApplicationPid());
            application.setApplicationUrl(applicationEditVo.getApplicationUrl());
            application.setApplicationCode(applicationEditVo.getApplicationCode());
            application.setApplicationEnName(applicationEditVo.getApplicationEnName());
            application.setIcon(applicationEditVo.getIcon());
            application.setApplicationDataUrlStartWith(applicationEditVo.getApplicationDataUrlStartWith());
            applicationService.update(application);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(applicationIds)) {
            List<String> ids = SmallPropsUtil.StringIdsToStringList(applicationIds);
            ids.forEach(id -> {
                roleApplicationService.deleteByApplicationId(id);
                collegeApplicationService.deleteByApplicationId(id);
            });
            applicationService.deletes(ids);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("删除失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
