package top.zbeboy.zone.web.theory.release;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.theory.release.TheoryAuthoritiesBean;
import top.zbeboy.zbase.bean.theory.release.TheoryConfigureBean;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.theory.release.*;
import top.zbeboy.zone.service.theory.TheoryAuthoritiesService;
import top.zbeboy.zone.service.theory.TheoryConfigureService;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.service.theory.TheoryUsersService;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TheoryReleaseRestController {
    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    @Resource
    private TheoryConfigureService theoryConfigureService;

    @Resource
    private TheoryAuthoritiesService theoryAuthoritiesService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private StudentService studentService;

    @Resource
    private TheoryUsersService theoryUsersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/theory/release/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TheoryReleaseBean> ajaxUtil = AjaxUtil.of();
        List<TheoryReleaseBean> beans = new ArrayList<>();
        Result<Record> records = theoryReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TheoryReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(theoryConditionCommon.canOperator(bean.getTheoryReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(theoryReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param theoryReleaseAddVo 理论
     * @param bindingResult      检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid TheoryReleaseAddVo theoryReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TheoryRelease theoryRelease = new TheoryRelease();
            String theoryReleaseId = UUIDUtil.getUUID();
            theoryRelease.setTheoryReleaseId(theoryReleaseId);
            theoryRelease.setTitle(theoryReleaseAddVo.getTitle());
            theoryRelease.setOrganizeId(theoryReleaseAddVo.getOrganizeId());
            theoryRelease.setCourseId(theoryReleaseAddVo.getCourseId());
            theoryRelease.setStartDate(DateTimeUtil.defaultParseSqlDate(theoryReleaseAddVo.getStartDate()));
            theoryRelease.setEndDate(DateTimeUtil.defaultParseSqlDate(theoryReleaseAddVo.getEndDate()));
            theoryRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
            Users users = SessionUtil.getUserFromSession();
            theoryRelease.setPublisher(users.getRealName());
            theoryRelease.setUsername(users.getUsername());
            theoryReleaseService.save(theoryRelease);

            // 生成名单
            List<StudentBean> studentBeans = studentService.findNormalByOrganizeId(theoryReleaseAddVo.getOrganizeId());
            if (Objects.nonNull(studentBeans) && studentBeans.size() > 0) {
                List<TheoryUsers> theoryUsers = new ArrayList<>();
                for (StudentBean student : studentBeans) {
                    TheoryUsers au = new TheoryUsers();
                    au.setTheoryUsersId(UUIDUtil.getUUID());
                    au.setTheoryReleaseId(theoryReleaseId);
                    au.setStudentId(student.getStudentId());
                    au.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    theoryUsers.add(au);
                }
                theoryUsersService.batchSave(theoryUsers);
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
     * @param theoryReleaseEditVo 理论
     * @param bindingResult       检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TheoryReleaseEditVo theoryReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.canOperator(theoryReleaseEditVo.getTheoryReleaseId())) {
                TheoryRelease theoryRelease = theoryReleaseService.findById(theoryReleaseEditVo.getTheoryReleaseId());
                theoryRelease.setTitle(theoryReleaseEditVo.getTitle());
                theoryRelease.setCourseId(theoryReleaseEditVo.getCourseId());
                theoryRelease.setStartDate(DateTimeUtil.defaultParseSqlDate(theoryReleaseEditVo.getStartDate()));
                theoryRelease.setEndDate(DateTimeUtil.defaultParseSqlDate(theoryReleaseEditVo.getEndDate()));

                theoryReleaseService.update(theoryRelease);
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
     * @param theoryReleaseId 理论id
     * @return true or false
     */
    @PostMapping("/web/theory/release/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("theoryReleaseId") String theoryReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.canOperator(theoryReleaseId)) {
            theoryReleaseService.deleteById(theoryReleaseId);
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
    @GetMapping("/web/theory/release/configure/data/{id}")
    public ResponseEntity<Map<String, Object>> configureData(@PathVariable("id") String id) {
        AjaxUtil<TheoryConfigureBean> ajaxUtil = AjaxUtil.of();
        List<TheoryConfigureBean> beans = new ArrayList<>();
        if (theoryConditionCommon.canOperator(id)) {
            Result<Record> records = theoryConfigureService.findByTheoryReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TheoryConfigureBean.class);
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置保存
     *
     * @param theoryConfigureAddVo 数据
     * @param bindingResult        检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/configure/save")
    public ResponseEntity<Map<String, Object>> configureSave(@Valid TheoryConfigureAddVo theoryConfigureAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.canOperator(theoryConfigureAddVo.getTheoryReleaseId())) {
                TheoryConfigure theoryConfigure = new TheoryConfigure();
                theoryConfigure.setTheoryConfigureId(UUIDUtil.getUUID());
                theoryConfigure.setTheoryReleaseId(theoryConfigureAddVo.getTheoryReleaseId());
                theoryConfigure.setStartTime(DateTimeUtil.defaultParseSqlTime(theoryConfigureAddVo.getStartTime()));
                theoryConfigure.setEndTime(DateTimeUtil.defaultParseSqlTime(theoryConfigureAddVo.getEndTime()));
                theoryConfigure.setWeekDay(theoryConfigureAddVo.getWeekDay());
                theoryConfigure.setSchoolroomId(theoryConfigureAddVo.getSchoolroomId());

                theoryConfigureService.save(theoryConfigure);
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
     * @param theoryConfigureEditVo 数据
     * @param bindingResult         检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/configure/update")
    public ResponseEntity<Map<String, Object>> configureUpdate(@Valid TheoryConfigureEditVo theoryConfigureEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.canOperator(theoryConfigureEditVo.getTheoryReleaseId())) {
                TheoryConfigure theoryConfigure = theoryConfigureService.findById(theoryConfigureEditVo.getTheoryConfigureId());
                theoryConfigure.setStartTime(DateTimeUtil.defaultParseSqlTime(theoryConfigureEditVo.getStartTime()));
                theoryConfigure.setEndTime(DateTimeUtil.defaultParseSqlTime(theoryConfigureEditVo.getEndTime()));
                theoryConfigure.setWeekDay(theoryConfigureEditVo.getWeekDay());
                theoryConfigure.setSchoolroomId(theoryConfigureEditVo.getSchoolroomId());

                theoryConfigureService.update(theoryConfigure);
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
     * @param theoryConfigureId 数据
     * @return true or false
     */
    @PostMapping("/web/theory/release/configure/delete")
    public ResponseEntity<Map<String, Object>> configureDelete(@RequestParam("theoryConfigureId") String theoryConfigureId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TheoryConfigure theoryConfigure = theoryConfigureService.findById(theoryConfigureId);
        if (Objects.nonNull(theoryConfigure)) {
            if (theoryConditionCommon.canOperator(theoryConfigure.getTheoryReleaseId())) {
                theoryConfigureService.deleteById(theoryConfigureId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到理论配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限分配数据
     *
     * @return 数据
     */
    @GetMapping("/web/theory/release/authorities/data/{id}")
    public ResponseEntity<Map<String, Object>> authoritiesData(@PathVariable("id") String id) {
        AjaxUtil<TheoryAuthoritiesBean> ajaxUtil = AjaxUtil.of();
        List<TheoryAuthoritiesBean> beans = new ArrayList<>();
        if (theoryConditionCommon.canOperator(id)) {
            Result<Record> records = theoryAuthoritiesService.findByTheoryReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TheoryAuthoritiesBean.class);
                beans.forEach(bean -> bean.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getValidDate())));
                beans.forEach(bean -> bean.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getExpireDate())));
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限保存
     *
     * @param theoryAuthoritiesAddVo 数据
     * @param bindingResult          检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/authorities/save")
    public ResponseEntity<Map<String, Object>> authoritiesSave(@Valid TheoryAuthoritiesAddVo theoryAuthoritiesAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.canOperator(theoryAuthoritiesAddVo.getTheoryReleaseId())) {
                // 系统或管理员不需要添加权限
                List<String> authorities = new ArrayList<>();
                authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
                authorities.add(Workbook.authorities.ROLE_ADMIN.name());
                List<Authorities> authoritiesList = authorizeService.findByUsernameAndInAuthorities(theoryAuthoritiesAddVo.getUsername(), authorities);
                if (Objects.isNull(authoritiesList) || authoritiesList.isEmpty()) {
                    // 本人无需添加权限
                    Users users = SessionUtil.getUserFromSession();
                    if (!StringUtils.equals(users.getUsername(), theoryAuthoritiesAddVo.getUsername())) {
                        TheoryAuthorities theoryAuthorities = new TheoryAuthorities();
                        theoryAuthorities.setAuthoritiesId(UUIDUtil.getUUID());
                        theoryAuthorities.setTheoryReleaseId(theoryAuthoritiesAddVo.getTheoryReleaseId());
                        theoryAuthorities.setUsername(theoryAuthoritiesAddVo.getUsername());
                        theoryAuthorities.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(theoryAuthoritiesAddVo.getValidDate()));
                        theoryAuthorities.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(theoryAuthoritiesAddVo.getExpireDate()));

                        theoryAuthoritiesService.save(theoryAuthorities);
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
     * @param theoryAuthoritiesEditVo 数据
     * @param bindingResult           检验
     * @return true or false
     */
    @PostMapping("/web/theory/release/authorities/update")
    public ResponseEntity<Map<String, Object>> authoritiesUpdate(@Valid TheoryAuthoritiesEditVo theoryAuthoritiesEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.canOperator(theoryAuthoritiesEditVo.getTheoryReleaseId())) {
                // 系统或管理员不需要添加权限
                List<String> authorities = new ArrayList<>();
                authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
                authorities.add(Workbook.authorities.ROLE_ADMIN.name());
                List<Authorities> authoritiesList = authorizeService.findByUsernameAndInAuthorities(theoryAuthoritiesEditVo.getUsername(), authorities);
                if (Objects.isNull(authoritiesList) || authoritiesList.isEmpty()) {
                    // 本人无需添加权限
                    Users users = SessionUtil.getUserFromSession();
                    if (!StringUtils.equals(users.getUsername(), theoryAuthoritiesEditVo.getUsername())) {
                        TheoryAuthorities theoryAuthorities = theoryAuthoritiesService.findById(theoryAuthoritiesEditVo.getAuthoritiesId());
                        theoryAuthorities.setUsername(theoryAuthoritiesEditVo.getUsername());
                        theoryAuthorities.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(theoryAuthoritiesEditVo.getValidDate()));
                        theoryAuthorities.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(theoryAuthoritiesEditVo.getExpireDate()));

                        theoryAuthoritiesService.update(theoryAuthorities);
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
    @PostMapping("/web/theory/release/authorities/delete")
    public ResponseEntity<Map<String, Object>> authoritiesDelete(@RequestParam("authoritiesId") String authoritiesId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TheoryAuthorities theoryAuthorities = theoryAuthoritiesService.findById(authoritiesId);
        if (Objects.nonNull(theoryAuthorities)) {
            if (theoryConditionCommon.canOperator(theoryAuthorities.getTheoryReleaseId())) {
                theoryAuthoritiesService.deleteById(authoritiesId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到理论配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
