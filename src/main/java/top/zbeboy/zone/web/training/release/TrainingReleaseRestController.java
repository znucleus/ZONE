package top.zbeboy.zone.web.training.release;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.training.release.TrainingAuthoritiesBean;
import top.zbeboy.zbase.bean.training.release.TrainingConfigureBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.training.release.*;
import top.zbeboy.zone.service.training.TrainingAuthoritiesService;
import top.zbeboy.zone.service.training.TrainingConfigureService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.training.TrainingUsersService;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@RestController
public class TrainingReleaseRestController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingConfigureService trainingConfigureService;

    @Resource
    private TrainingAuthoritiesService trainingAuthoritiesService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private StudentService studentService;

    @Resource
    private TrainingUsersService trainingUsersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/release/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingReleaseBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        List<TrainingReleaseBean> beans = new ArrayList<>();
        Result<Record> records = trainingReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingReleaseBean.class);
            beans.forEach(bean -> {
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getReleaseTime()));
                bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.canOperator(bean.getTrainingReleaseId())));
            });
        }
        simplePaginationUtil.setTotalSize(trainingReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param trainingReleaseAddVo 实训
     * @param bindingResult        检验
     * @return true or false
     */
    @PostMapping("/web/training/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid TrainingReleaseAddVo trainingReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TrainingRelease trainingRelease = new TrainingRelease();
            String trainingReleaseId = UUIDUtil.getUUID();
            trainingRelease.setTrainingReleaseId(trainingReleaseId);
            trainingRelease.setTitle(trainingReleaseAddVo.getTitle());
            trainingRelease.setOrganizeId(trainingReleaseAddVo.getOrganizeId());
            trainingRelease.setCourseId(trainingReleaseAddVo.getCourseId());
            trainingRelease.setStartDate(DateTimeUtil.defaultParseLocalDate(trainingReleaseAddVo.getStartDate()));
            trainingRelease.setEndDate(DateTimeUtil.defaultParseLocalDate(trainingReleaseAddVo.getEndDate()));
            trainingRelease.setReleaseTime(DateTimeUtil.getNowLocalDateTime());
            Users users = SessionUtil.getUserFromSession();
            trainingRelease.setPublisher(users.getRealName());
            trainingRelease.setUsername(users.getUsername());
            trainingReleaseService.save(trainingRelease);

            // 生成名单
            Optional<List<StudentBean>> optionalStudentBeans = studentService.findNormalByOrganizeId(trainingReleaseAddVo.getOrganizeId());
            if (optionalStudentBeans.isPresent()) {
                List<TrainingUsers> trainingUsers = new ArrayList<>();
                for (StudentBean student : optionalStudentBeans.get()) {
                    TrainingUsers au = new TrainingUsers();
                    au.setTrainingUsersId(UUIDUtil.getUUID());
                    au.setTrainingReleaseId(trainingReleaseId);
                    au.setStudentId(student.getStudentId());
                    au.setCreateDate(DateTimeUtil.getNowLocalDateTime());
                    trainingUsers.add(au);
                }
                trainingUsersService.batchSave(trainingUsers);
            }

            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param trainingReleaseEditVo 实训
     * @param bindingResult         检验
     * @return true or false
     */
    @PostMapping("/web/training/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingReleaseEditVo trainingReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingReleaseEditVo.getTrainingReleaseId())) {
                TrainingRelease trainingRelease = trainingReleaseService.findById(trainingReleaseEditVo.getTrainingReleaseId());
                trainingRelease.setTitle(trainingReleaseEditVo.getTitle());
                trainingRelease.setCourseId(trainingReleaseEditVo.getCourseId());
                trainingRelease.setStartDate(DateTimeUtil.defaultParseLocalDate(trainingReleaseEditVo.getStartDate()));
                trainingRelease.setEndDate(DateTimeUtil.defaultParseLocalDate(trainingReleaseEditVo.getEndDate()));

                trainingReleaseService.update(trainingRelease);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param trainingReleaseId 实训id
     * @return true or false
     */
    @PostMapping("/web/training/release/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingReleaseId") String trainingReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.canOperator(trainingReleaseId)) {
            trainingReleaseService.deleteById(trainingReleaseId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置数据
     *
     * @return 数据
     */
    @GetMapping("/web/training/release/configure/data/{id}")
    public ResponseEntity<Map<String, Object>> configureData(@PathVariable("id") String id) {
        AjaxUtil<TrainingConfigureBean> ajaxUtil = AjaxUtil.of();
        List<TrainingConfigureBean> beans = new ArrayList<>();
        if (trainingConditionCommon.canOperator(id)) {
            Result<Record> records = trainingConfigureService.findByTrainingReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TrainingConfigureBean.class);
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置保存
     *
     * @param trainingConfigureAddVo 数据
     * @param bindingResult          检验
     * @return true or false
     */
    @PostMapping("/web/training/release/configure/save")
    public ResponseEntity<Map<String, Object>> configureSave(@Valid TrainingConfigureAddVo trainingConfigureAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingConfigureAddVo.getTrainingReleaseId())) {
                TrainingConfigure trainingConfigure = new TrainingConfigure();
                trainingConfigure.setTrainingConfigureId(UUIDUtil.getUUID());
                trainingConfigure.setTrainingReleaseId(trainingConfigureAddVo.getTrainingReleaseId());
                trainingConfigure.setStartTime(DateTimeUtil.defaultParseLocalTime(trainingConfigureAddVo.getStartTime()));
                trainingConfigure.setEndTime(DateTimeUtil.defaultParseLocalTime(trainingConfigureAddVo.getEndTime()));
                trainingConfigure.setWeekDay(trainingConfigureAddVo.getWeekDay());
                trainingConfigure.setSchoolroomId(trainingConfigureAddVo.getSchoolroomId());

                trainingConfigureService.save(trainingConfigure);
                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置更新
     *
     * @param trainingConfigureEditVo 数据
     * @param bindingResult           检验
     * @return true or false
     */
    @PostMapping("/web/training/release/configure/update")
    public ResponseEntity<Map<String, Object>> configureUpdate(@Valid TrainingConfigureEditVo trainingConfigureEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingConfigureEditVo.getTrainingReleaseId())) {
                TrainingConfigure trainingConfigure = trainingConfigureService.findById(trainingConfigureEditVo.getTrainingConfigureId());
                trainingConfigure.setStartTime(DateTimeUtil.defaultParseLocalTime(trainingConfigureEditVo.getStartTime()));
                trainingConfigure.setEndTime(DateTimeUtil.defaultParseLocalTime(trainingConfigureEditVo.getEndTime()));
                trainingConfigure.setWeekDay(trainingConfigureEditVo.getWeekDay());
                trainingConfigure.setSchoolroomId(trainingConfigureEditVo.getSchoolroomId());

                trainingConfigureService.update(trainingConfigure);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置删除
     *
     * @param trainingConfigureId 数据
     * @return true or false
     */
    @PostMapping("/web/training/release/configure/delete")
    public ResponseEntity<Map<String, Object>> configureDelete(@RequestParam("trainingConfigureId") String trainingConfigureId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingConfigure trainingConfigure = trainingConfigureService.findById(trainingConfigureId);
        if (Objects.nonNull(trainingConfigure)) {
            if (trainingConditionCommon.canOperator(trainingConfigure.getTrainingReleaseId())) {
                trainingConfigureService.deleteById(trainingConfigureId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限分配数据
     *
     * @return 数据
     */
    @GetMapping("/web/training/release/authorities/data/{id}")
    public ResponseEntity<Map<String, Object>> authoritiesData(@PathVariable("id") String id) {
        AjaxUtil<TrainingAuthoritiesBean> ajaxUtil = AjaxUtil.of();
        List<TrainingAuthoritiesBean> beans = new ArrayList<>();
        if (trainingConditionCommon.canOperator(id)) {
            Result<Record> records = trainingAuthoritiesService.findByTrainingReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TrainingAuthoritiesBean.class);
                beans.forEach(bean -> {
                    bean.setValidDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getValidDate()));
                    bean.setExpireDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getExpireDate()));
                });
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限保存
     *
     * @param trainingAuthoritiesAddVo 数据
     * @param bindingResult            检验
     * @return true or false
     */
    @PostMapping("/web/training/release/authorities/save")
    public ResponseEntity<Map<String, Object>> authoritiesSave(@Valid TrainingAuthoritiesAddVo trainingAuthoritiesAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingAuthoritiesAddVo.getTrainingReleaseId())) {
                // 系统或管理员不需要添加权限
                List<String> authorities = new ArrayList<>();
                authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
                authorities.add(Workbook.authorities.ROLE_ADMIN.name());
                Optional<List<Authorities>> optionalAuthorities = authorizeService.findByUsernameAndInAuthorities(trainingAuthoritiesAddVo.getUsername(), authorities);
                if (!optionalAuthorities.isPresent()) {
                    // 本人无需添加权限
                    Users users = SessionUtil.getUserFromSession();
                    if (!StringUtils.equals(users.getUsername(), trainingAuthoritiesAddVo.getUsername())) {
                        TrainingAuthorities trainingAuthorities = new TrainingAuthorities();
                        trainingAuthorities.setAuthoritiesId(UUIDUtil.getUUID());
                        trainingAuthorities.setTrainingReleaseId(trainingAuthoritiesAddVo.getTrainingReleaseId());
                        trainingAuthorities.setUsername(trainingAuthoritiesAddVo.getUsername());
                        trainingAuthorities.setValidDate(DateTimeUtil.defaultParseLocalDateTime(trainingAuthoritiesAddVo.getValidDate()));
                        trainingAuthorities.setExpireDate(DateTimeUtil.defaultParseLocalDateTime(trainingAuthoritiesAddVo.getExpireDate()));

                        trainingAuthoritiesService.save(trainingAuthorities);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("本人无需添加权限");
                    }
                } else {
                    ajaxUtil.fail().msg("系统或管理员无需添加权限");
                }

            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限更新
     *
     * @param trainingAuthoritiesEditVo 数据
     * @param bindingResult             检验
     * @return true or false
     */
    @PostMapping("/web/training/release/authorities/update")
    public ResponseEntity<Map<String, Object>> authoritiesUpdate(@Valid TrainingAuthoritiesEditVo trainingAuthoritiesEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingAuthoritiesEditVo.getTrainingReleaseId())) {
                // 系统或管理员不需要添加权限
                List<String> authorities = new ArrayList<>();
                authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
                authorities.add(Workbook.authorities.ROLE_ADMIN.name());
                Optional<List<Authorities>> optionalAuthorities = authorizeService.findByUsernameAndInAuthorities(trainingAuthoritiesEditVo.getUsername(), authorities);
                if (!optionalAuthorities.isPresent()) {
                    // 本人无需添加权限
                    Users users = SessionUtil.getUserFromSession();
                    if (!StringUtils.equals(users.getUsername(), trainingAuthoritiesEditVo.getUsername())) {
                        TrainingAuthorities trainingAuthorities = trainingAuthoritiesService.findById(trainingAuthoritiesEditVo.getAuthoritiesId());
                        trainingAuthorities.setUsername(trainingAuthoritiesEditVo.getUsername());
                        trainingAuthorities.setValidDate(DateTimeUtil.defaultParseLocalDateTime(trainingAuthoritiesEditVo.getValidDate()));
                        trainingAuthorities.setExpireDate(DateTimeUtil.defaultParseLocalDateTime(trainingAuthoritiesEditVo.getExpireDate()));

                        trainingAuthoritiesService.update(trainingAuthorities);
                        ajaxUtil.success().msg("更新成功");
                    } else {
                        ajaxUtil.fail().msg("本人无需添加权限");
                    }
                } else {
                    ajaxUtil.fail().msg("系统或管理员无需添加权限");
                }

            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限删除
     *
     * @param authoritiesId 数据
     * @return true or false
     */
    @PostMapping("/web/training/release/authorities/delete")
    public ResponseEntity<Map<String, Object>> authoritiesDelete(@RequestParam("authoritiesId") String authoritiesId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingAuthorities trainingAuthorities = trainingAuthoritiesService.findById(authoritiesId);
        if (Objects.nonNull(trainingAuthorities)) {
            if (trainingConditionCommon.canOperator(trainingAuthorities.getTrainingReleaseId())) {
                trainingAuthoritiesService.deleteById(authoritiesId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
