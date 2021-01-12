package top.zbeboy.zone.web.theory.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Record11;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.theory.attend.TheoryAttendBean;
import top.zbeboy.zbase.bean.theory.attend.TheoryAttendUsersBean;
import top.zbeboy.zbase.bean.theory.release.TheoryConfigureBean;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.domain.tables.records.TheoryAttendRecord;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.ByteUtil;
import top.zbeboy.zbase.tools.web.util.SmallPropsUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ExportInfo;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;
import top.zbeboy.zbase.vo.theory.attend.TheoryAttendAddVo;
import top.zbeboy.zbase.vo.theory.attend.TheoryAttendEditVo;
import top.zbeboy.zone.service.export.TheoryAttendSituationExport;
import top.zbeboy.zone.service.export.TheoryAttendUsersExport;
import top.zbeboy.zone.service.theory.*;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;
import top.zbeboy.zone.web.theory.common.TheoryControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TheoryAttendRestController {

    @Resource
    private TheoryAttendService theoryAttendService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    @Resource
    private TheoryConfigureService theoryConfigureService;

    @Resource
    private TheoryUsersService theoryUsersService;

    @Resource
    private TheoryAttendUsersService theoryAttendUsersService;

    @Resource
    private TheoryAttendMyService theoryAttendMyService;

    @Resource
    private TheoryAttendSituationService theoryAttendSituationService;

    @Resource
    private TheoryControllerCommon theoryControllerCommon;

    @Resource
    private UploadService uploadService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/theory/attend/theory/data")
    public ResponseEntity<Map<String, Object>> theoryData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TheoryReleaseBean> ajaxUtil = theoryControllerCommon.theoryData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/theory/attend/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TheoryAttendBean> ajaxUtil = AjaxUtil.of();
        List<TheoryAttendBean> beans = new ArrayList<>();
        Result<Record> records = theoryAttendService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TheoryAttendBean.class);
            beans.forEach(bean -> bean.setPublishDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getPublishDate())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(theoryConditionCommon.usersCondition(bean.getTheoryReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(theoryAttendService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置数据
     *
     * @return 数据
     */
    @GetMapping("/web/theory/attend/configure/data/{id}")
    public ResponseEntity<Map<String, Object>> configureData(@PathVariable("id") String id) {
        AjaxUtil<TheoryConfigureBean> ajaxUtil = AjaxUtil.of();
        List<TheoryConfigureBean> beans = new ArrayList<>();
        if (theoryConditionCommon.usersCondition(id)) {
            Result<Record> records = theoryConfigureService.findByTheoryReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TheoryConfigureBean.class);
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置发布
     *
     * @param theoryConfigureId 配置id
     * @return true or false
     */
    @PostMapping("/web/theory/attend/configure/release")
    public ResponseEntity<Map<String, Object>> configureRelease(@RequestParam("theoryConfigureId") String theoryConfigureId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TheoryConfigure theoryConfigure = theoryConfigureService.findById(theoryConfigureId);
        if (Objects.nonNull(theoryConfigure)) {
            if (theoryConditionCommon.usersCondition(theoryConfigure.getTheoryReleaseId())) {


                TheoryAttend theoryAttend = new TheoryAttend();
                String theoryAttendId = UUIDUtil.getUUID();
                theoryAttend.setTheoryAttendId(theoryAttendId);
                theoryAttend.setTheoryReleaseId(theoryConfigure.getTheoryReleaseId());

                // 生成考勤日期
                TheoryAttendRecord theoryAttendRecord = theoryAttendService.findByTheoryReleaseIdWithRecentlyAttendDate(theoryConfigure.getTheoryReleaseId());
                if (Objects.nonNull(theoryAttendRecord)) {
                    theoryAttend.setAttendDate(DateTimeUtil.calculationSqlNextWeekDay(theoryAttendRecord.getAttendDate(), theoryConfigure.getWeekDay()));
                } else {
                    theoryAttend.setAttendDate(DateTimeUtil.calculationSqlNextWeekDay(DateTimeUtil.getNowSqlDate(), theoryConfigure.getWeekDay()));
                }

                theoryAttend.setAttendStartTime(theoryConfigure.getStartTime());
                theoryAttend.setAttendEndTime(theoryConfigure.getEndTime());
                theoryAttend.setAttendRoom(theoryConfigure.getSchoolroomId());
                theoryAttend.setPublishDate(DateTimeUtil.getNowSqlTimestamp());
                theoryAttendService.save(theoryAttend);

                Users user = SessionUtil.getUserFromSession();
                List<TheoryUsers> theoryUsers = theoryUsersService.findByTheoryReleaseId(theoryConfigure.getTheoryReleaseId());
                if (Objects.nonNull(theoryUsers)) {
                    List<TheoryAttendUsers> theoryAttendUsers = new ArrayList<>();
                    for (TheoryUsers users : theoryUsers) {
                        TheoryAttendUsers theoryAttendUser = new TheoryAttendUsers();
                        theoryAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        theoryAttendUser.setTheoryAttendId(theoryAttendId);
                        theoryAttendUser.setTheoryUsersId(users.getTheoryUsersId());
                        theoryAttendUser.setOperateUser(user.getUsername());
                        theoryAttendUser.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                        theoryAttendUser.setOperate(ByteUtil.toByte(0));
                        theoryAttendUser.setRemark(users.getRemark());

                        theoryAttendUsers.add(theoryAttendUser);
                    }

                    theoryAttendUsersService.batchSave(theoryAttendUsers);
                }
                ajaxUtil.success().msg("发布成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到理论配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 自定义发布
     *
     * @param theoryAttendAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/theory/attend/release/save")
    public ResponseEntity<Map<String, Object>> releaseSave(@Valid TheoryAttendAddVo theoryAttendAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.usersCondition(theoryAttendAddVo.getTheoryReleaseId())) {
                TheoryAttend theoryAttend = new TheoryAttend();
                String theoryAttendId = UUIDUtil.getUUID();
                theoryAttend.setTheoryAttendId(theoryAttendId);
                theoryAttend.setTheoryReleaseId(theoryAttendAddVo.getTheoryReleaseId());
                theoryAttend.setAttendDate(DateTimeUtil.defaultParseSqlDate(theoryAttendAddVo.getAttendDate()));
                theoryAttend.setAttendStartTime(DateTimeUtil.defaultParseSqlTime(theoryAttendAddVo.getAttendStartTime()));
                theoryAttend.setAttendEndTime(DateTimeUtil.defaultParseSqlTime(theoryAttendAddVo.getAttendEndTime()));
                theoryAttend.setAttendRoom(theoryAttendAddVo.getAttendRoom());
                theoryAttend.setPublishDate(DateTimeUtil.getNowSqlTimestamp());
                theoryAttend.setRemark(theoryAttendAddVo.getRemark());
                theoryAttendService.save(theoryAttend);

                Users user = SessionUtil.getUserFromSession();
                List<TheoryUsers> theoryUsers = theoryUsersService.findByTheoryReleaseId(theoryAttendAddVo.getTheoryReleaseId());
                if (Objects.nonNull(theoryUsers)) {
                    List<TheoryAttendUsers> theoryAttendUsers = new ArrayList<>();
                    for (TheoryUsers users : theoryUsers) {
                        TheoryAttendUsers theoryAttendUser = new TheoryAttendUsers();
                        theoryAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        theoryAttendUser.setTheoryAttendId(theoryAttendId);
                        theoryAttendUser.setTheoryUsersId(users.getTheoryUsersId());
                        theoryAttendUser.setOperateUser(user.getUsername());
                        theoryAttendUser.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                        theoryAttendUser.setOperate(ByteUtil.toByte(0));
                        theoryAttendUser.setRemark(users.getRemark());

                        theoryAttendUsers.add(theoryAttendUser);
                    }

                    theoryAttendUsersService.batchSave(theoryAttendUsers);
                }
                ajaxUtil.success().msg("发布成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 编辑
     *
     * @param theoryAttendEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/theory/attend/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TheoryAttendEditVo theoryAttendEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (theoryConditionCommon.usersCondition(theoryAttendEditVo.getTheoryReleaseId())) {
                TheoryAttend theoryAttend = theoryAttendService.findById(theoryAttendEditVo.getTheoryAttendId());
                theoryAttend.setAttendDate(DateTimeUtil.defaultParseSqlDate(theoryAttendEditVo.getAttendDate()));
                theoryAttend.setAttendStartTime(DateTimeUtil.defaultParseSqlTime(theoryAttendEditVo.getAttendStartTime()));
                theoryAttend.setAttendEndTime(DateTimeUtil.defaultParseSqlTime(theoryAttendEditVo.getAttendEndTime()));
                theoryAttend.setAttendRoom(theoryAttendEditVo.getAttendRoom());
                theoryAttend.setRemark(theoryAttendEditVo.getRemark());
                theoryAttendService.update(theoryAttend);
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
     * @param theoryAttendId 考勤id
     * @return true or false
     */
    @PostMapping("/web/theory/attend/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("theoryAttendId") String theoryAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TheoryAttend theoryAttend = theoryAttendService.findById(theoryAttendId);
        if (Objects.nonNull(theoryAttend)) {
            if (theoryConditionCommon.usersCondition(theoryAttend.getTheoryReleaseId())) {
                theoryAttendService.deleteById(theoryAttendId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到理论考勤数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/theory/attend/users/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("studentNumber");
        headers.add("organizeName");
        headers.add("mobile");
        headers.add("email");
        headers.add("sex");
        headers.add("operate");
        headers.add("remark");
        headers.add("operateUser");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>>
                records = theoryAttendUsersService.findAllByPage(dataTablesUtil);
        List<TheoryAttendUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryAttendUsersBean.class);
            beans.forEach(bean -> {
                if (!theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 6));
                }
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(theoryAttendUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(theoryAttendUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param attendUsersId   人员id
     * @param operate         状态
     * @param theoryReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/web/theory/attend/users/operate")
    public ResponseEntity<Map<String, Object>> operate(@RequestParam("attendUsersId") String attendUsersId, @RequestParam("operate") Byte operate,
                                                       @RequestParam("theoryReleaseId") String theoryReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();

        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            Users users = SessionUtil.getUserFromSession();
            TheoryAttendUsers theoryAttendUsers = theoryAttendUsersService.findById(attendUsersId);
            theoryAttendUsers.setOperate(operate);
            theoryAttendUsers.setOperateUser(users.getUsername());
            theoryAttendUsers.setOperateDate(DateTimeUtil.getNowSqlTimestamp());

            theoryAttendUsersService.update(theoryAttendUsers);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param attendUsersId 人员id
     * @param remark        备注
     * @return true or false
     */
    @PostMapping("/web/theory/attend/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("attendUsersId") String attendUsersId, String remark,
                                                      @RequestParam("theoryReleaseId") String theoryReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            TheoryAttendUsers theoryAttendUsers = theoryAttendUsersService.findById(attendUsersId);
            theoryAttendUsers.setRemark(remark);

            theoryAttendUsersService.update(theoryAttendUsers);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除
     *
     * @param theoryReleaseId 发布id
     * @param attendUsersIds  ids
     * @return true注销成功
     */
    @PostMapping("/web/theory/attend/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("theoryReleaseId") String theoryReleaseId, String attendUsersIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(attendUsersIds)) {
            if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
                theoryAttendUsersService.deleteById(SmallPropsUtil.StringIdsToStringList(attendUsersIds));
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("请选择学生");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新全勤
     *
     * @param theoryReleaseId 发布id
     * @param theoryAttendId  考勤id
     * @return true or false
     */
    @PostMapping("/web/theory/attend/users/all_ok")
    public ResponseEntity<Map<String, Object>> allOk(@RequestParam("theoryReleaseId") String theoryReleaseId, @RequestParam("theoryAttendId") String theoryAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            theoryAttendUsersService.updateOperateByTheoryAttendId(theoryAttendId, ByteUtil.toByte(3));
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 名单重置
     *
     * @param theoryReleaseId 理论发布id
     * @param theoryAttendId  考勤id
     * @return true or false
     */
    @PostMapping("/web/theory/attend/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("theoryReleaseId") String theoryReleaseId, @RequestParam("theoryAttendId") String theoryAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            Result<Record> records = theoryAttendUsersService.findStudentNotExistsUsers(theoryReleaseId, theoryAttendId);
            if (records.isNotEmpty()) {
                Users user = SessionUtil.getUserFromSession();
                List<TheoryUsers> theoryUsers = records.into(TheoryUsers.class);
                List<TheoryAttendUsers> theoryAttendUsers = new ArrayList<>();
                for (TheoryUsers users : theoryUsers) {
                    TheoryAttendUsers au = new TheoryAttendUsers();
                    au.setAttendUsersId(UUIDUtil.getUUID());
                    au.setTheoryAttendId(theoryAttendId);
                    au.setTheoryUsersId(users.getTheoryUsersId());
                    au.setOperate(ByteUtil.toByte(0));
                    au.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                    au.setOperateUser(user.getUsername());

                    theoryAttendUsers.add(au);
                }
                theoryAttendUsersService.batchSave(theoryAttendUsers);
            }

            ajaxUtil.success().msg("重置成功");

        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 名单 数据
     *
     * @param request 请求
     */
    @GetMapping("/web/theory/attend/users/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "理论考勤数据表", Workbook.theoryFilePath());
        List<TheoryAttendUsersBean> beans = new ArrayList<>();
        Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> records = theoryAttendUsersService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryAttendUsersBean.class);
            beans.forEach(bean -> {
                if (!theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 6));
                }
            });
        }

        TheoryAttendUsersExport export = new TheoryAttendUsersExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }

    /**
     * 数据
     *
     * @param tableSawUtil 请求
     * @return 数据
     */
    @GetMapping("/web/theory/attend/my/data")
    public ResponseEntity<Map<String, Object>> myData(TableSawUtil tableSawUtil) {
        AjaxUtil<TheoryAttendUsersBean> ajaxUtil = AjaxUtil.of();
        List<TheoryAttendUsersBean> beans = new ArrayList<>();
        Users users = SessionUtil.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsername(users.getUsername());
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    int studentId = studentBean.getStudentId();
                    tableSawUtil.setSearch("studentId", studentId);
                    Result<Record> records = theoryAttendMyService.findAll(tableSawUtil);
                    if (records.isNotEmpty()) {
                        beans = records.into(TheoryAttendUsersBean.class);
                    }
                }
            }
        }

        tableSawUtil.setTotalSize(beans.size());
        ajaxUtil.success().list(beans).page(tableSawUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/theory/attend/situation/data")
    public ResponseEntity<DataTablesUtil> situationData(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("realName");
        headers.add("studentNumber");
        headers.add("attendDate");
        headers.add("organizeName");
        headers.add("sex");
        headers.add("operate");
        headers.add("remark");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        Result<Record>
                records = theoryAttendSituationService.findAllByPage(dataTablesUtil);
        List<TheoryAttendUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryAttendUsersBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(theoryAttendSituationService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(theoryAttendSituationService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 导出 考勤情况 数据
     *
     * @param request 请求
     */
    @GetMapping("/web/theory/attend/situation/export")
    public void situationExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "理论考勤情况数据表", Workbook.theoryFilePath());
        List<TheoryAttendUsersBean> beans = new ArrayList<>();
        Result<Record> records = theoryAttendSituationService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryAttendUsersBean.class);
        }

        TheoryAttendSituationExport export = new TheoryAttendSituationExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }

}
