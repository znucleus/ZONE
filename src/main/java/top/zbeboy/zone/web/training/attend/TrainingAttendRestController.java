package top.zbeboy.zone.web.training.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Record11;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.training.attend.TrainingAttendBean;
import top.zbeboy.zbase.bean.training.attend.TrainingAttendUsersBean;
import top.zbeboy.zbase.bean.training.release.TrainingConfigureBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.domain.tables.records.TrainingAttendRecord;
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
import top.zbeboy.zbase.vo.training.attend.TrainingAttendAddVo;
import top.zbeboy.zbase.vo.training.attend.TrainingAttendEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.export.TrainingAttendSituationExport;
import top.zbeboy.zone.service.export.TrainingAttendUsersExport;
import top.zbeboy.zone.service.training.*;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
public class TrainingAttendRestController {

    @Resource
    private TrainingAttendService trainingAttendService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingConfigureService trainingConfigureService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingAttendUsersService trainingAttendUsersService;

    @Resource
    private TrainingAttendMyService trainingAttendMyService;

    @Resource
    private TrainingAttendSituationService trainingAttendSituationService;

    @Resource
    private TrainingControllerCommon trainingControllerCommon;

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
    @ApiLoggingRecord(remark = "实训考勤发布数据", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/training/attend/training/paging")
    public ResponseEntity<Map<String, Object>> trainingData(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<TrainingReleaseBean> ajaxUtil = trainingControllerCommon.trainingData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "实训考勤列表数据", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/training/attend/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        AjaxUtil<TrainingAttendBean> ajaxUtil = AjaxUtil.of();
        List<TrainingAttendBean> beans = new ArrayList<>();
        Result<Record> records = trainingAttendService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingAttendBean.class);
            beans.forEach(bean -> {
                bean.setPublishDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getPublishDate()));
                bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())));
            });
        }
        simplePaginationUtil.setTotalSize(trainingAttendService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置数据
     *
     * @return 数据
     */
    @GetMapping("/web/training/attend/configure/data/{id}")
    public ResponseEntity<Map<String, Object>> configureData(@PathVariable("id") String id) {
        AjaxUtil<TrainingConfigureBean> ajaxUtil = AjaxUtil.of();
        List<TrainingConfigureBean> beans = new ArrayList<>();
        if (trainingConditionCommon.usersCondition(id)) {
            Result<Record> records = trainingConfigureService.findByTrainingReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TrainingConfigureBean.class);
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置发布
     *
     * @param trainingConfigureId 配置id
     * @return true or false
     */
    @PostMapping("/web/training/attend/configure/release")
    public ResponseEntity<Map<String, Object>> configureRelease(@RequestParam("trainingConfigureId") String trainingConfigureId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingConfigure trainingConfigure = trainingConfigureService.findById(trainingConfigureId);
        if (Objects.nonNull(trainingConfigure)) {
            if (trainingConditionCommon.usersCondition(trainingConfigure.getTrainingReleaseId())) {


                TrainingAttend trainingAttend = new TrainingAttend();
                String trainingAttendId = UUIDUtil.getUUID();
                trainingAttend.setTrainingAttendId(trainingAttendId);
                trainingAttend.setTrainingReleaseId(trainingConfigure.getTrainingReleaseId());

                // 生成考勤日期
                TrainingAttendRecord trainingAttendRecord = trainingAttendService.findByTrainingReleaseIdWithRecentlyAttendDate(trainingConfigure.getTrainingReleaseId());
                if (Objects.nonNull(trainingAttendRecord)) {
                    trainingAttend.setAttendDate(DateTimeUtil.calculationNextWeekDay(trainingAttendRecord.getAttendDate(), trainingConfigure.getWeekDay()));
                } else {
                    trainingAttend.setAttendDate(DateTimeUtil.calculationNextWeekDay(DateTimeUtil.getNowLocalDate(), trainingConfigure.getWeekDay()));
                }

                trainingAttend.setAttendStartTime(trainingConfigure.getStartTime());
                trainingAttend.setAttendEndTime(trainingConfigure.getEndTime());
                trainingAttend.setAttendRoom(trainingConfigure.getSchoolroomId());
                trainingAttend.setPublishDate(DateTimeUtil.getNowLocalDateTime());
                trainingAttendService.save(trainingAttend);

                Users user = SessionUtil.getUserFromSession();
                List<TrainingUsers> trainingUsers = trainingUsersService.findByTrainingReleaseId(trainingConfigure.getTrainingReleaseId());
                if (Objects.nonNull(trainingUsers)) {
                    List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                    for (TrainingUsers users : trainingUsers) {
                        TrainingAttendUsers trainingAttendUser = new TrainingAttendUsers();
                        trainingAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        trainingAttendUser.setTrainingAttendId(trainingAttendId);
                        trainingAttendUser.setTrainingUsersId(users.getTrainingUsersId());
                        trainingAttendUser.setOperateUser(user.getUsername());
                        trainingAttendUser.setOperateDate(DateTimeUtil.getNowLocalDateTime());
                        trainingAttendUser.setOperate(ByteUtil.toByte(0));
                        trainingAttendUser.setRemark(users.getRemark());

                        trainingAttendUsers.add(trainingAttendUser);
                    }

                    trainingAttendUsersService.batchSave(trainingAttendUsers);
                }
                ajaxUtil.success().msg("发布成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 自定义发布
     *
     * @param trainingAttendAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/attend/release/save")
    public ResponseEntity<Map<String, Object>> releaseSave(@Valid TrainingAttendAddVo trainingAttendAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.usersCondition(trainingAttendAddVo.getTrainingReleaseId())) {
                TrainingAttend trainingAttend = new TrainingAttend();
                String trainingAttendId = UUIDUtil.getUUID();
                trainingAttend.setTrainingAttendId(trainingAttendId);
                trainingAttend.setTrainingReleaseId(trainingAttendAddVo.getTrainingReleaseId());
                trainingAttend.setAttendDate(DateTimeUtil.defaultParseLocalDate(trainingAttendAddVo.getAttendDate()));
                trainingAttend.setAttendStartTime(DateTimeUtil.defaultParseLocalTime(trainingAttendAddVo.getAttendStartTime()));
                trainingAttend.setAttendEndTime(DateTimeUtil.defaultParseLocalTime(trainingAttendAddVo.getAttendEndTime()));
                trainingAttend.setAttendRoom(trainingAttendAddVo.getAttendRoom());
                trainingAttend.setPublishDate(DateTimeUtil.getNowLocalDateTime());
                trainingAttend.setRemark(trainingAttendAddVo.getRemark());
                trainingAttendService.save(trainingAttend);

                Users user = SessionUtil.getUserFromSession();
                List<TrainingUsers> trainingUsers = trainingUsersService.findByTrainingReleaseId(trainingAttendAddVo.getTrainingReleaseId());
                if (Objects.nonNull(trainingUsers)) {
                    List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                    for (TrainingUsers users : trainingUsers) {
                        TrainingAttendUsers trainingAttendUser = new TrainingAttendUsers();
                        trainingAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        trainingAttendUser.setTrainingAttendId(trainingAttendId);
                        trainingAttendUser.setTrainingUsersId(users.getTrainingUsersId());
                        trainingAttendUser.setOperateUser(user.getUsername());
                        trainingAttendUser.setOperateDate(DateTimeUtil.getNowLocalDateTime());
                        trainingAttendUser.setOperate(ByteUtil.toByte(0));
                        trainingAttendUser.setRemark(users.getRemark());

                        trainingAttendUsers.add(trainingAttendUser);
                    }

                    trainingAttendUsersService.batchSave(trainingAttendUsers);
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
     * @param trainingAttendEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/attend/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingAttendEditVo trainingAttendEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.usersCondition(trainingAttendEditVo.getTrainingReleaseId())) {
                TrainingAttend trainingAttend = trainingAttendService.findById(trainingAttendEditVo.getTrainingAttendId());
                trainingAttend.setAttendDate(DateTimeUtil.defaultParseLocalDate(trainingAttendEditVo.getAttendDate()));
                trainingAttend.setAttendStartTime(DateTimeUtil.defaultParseLocalTime(trainingAttendEditVo.getAttendStartTime()));
                trainingAttend.setAttendEndTime(DateTimeUtil.defaultParseLocalTime(trainingAttendEditVo.getAttendEndTime()));
                trainingAttend.setAttendRoom(trainingAttendEditVo.getAttendRoom());
                trainingAttend.setRemark(trainingAttendEditVo.getRemark());
                trainingAttendService.update(trainingAttend);
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
     * @param trainingAttendId 考勤id
     * @return true or false
     */
    @PostMapping("/web/training/attend/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingAttendId") String trainingAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingAttend trainingAttend = trainingAttendService.findById(trainingAttendId);
        if (Objects.nonNull(trainingAttend)) {
            if (trainingConditionCommon.usersCondition(trainingAttend.getTrainingReleaseId())) {
                trainingAttendService.deleteById(trainingAttendId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训考勤数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/training/attend/users/paging")
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
                records = trainingAttendUsersService.findAllByPage(dataTablesUtil);
        List<TrainingAttendUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingAttendUsersBean.class);
            beans.forEach(bean -> {
                if (!trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 7));
                }
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(trainingAttendUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(trainingAttendUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param attendUsersId     人员id
     * @param operate           状态
     * @param trainingReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/web/training/attend/users/operate")
    public ResponseEntity<Map<String, Object>> operate(@RequestParam("attendUsersId") String attendUsersId, @RequestParam("operate") Byte operate,
                                                       @RequestParam("trainingReleaseId") String trainingReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();

        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            Users users = SessionUtil.getUserFromSession();
            TrainingAttendUsers trainingAttendUsers = trainingAttendUsersService.findById(attendUsersId);
            trainingAttendUsers.setOperate(operate);
            trainingAttendUsers.setOperateUser(users.getUsername());
            trainingAttendUsers.setOperateDate(DateTimeUtil.getNowLocalDateTime());

            trainingAttendUsersService.update(trainingAttendUsers);
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
    @PostMapping("/web/training/attend/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("attendUsersId") String attendUsersId, String remark,
                                                      @RequestParam("trainingReleaseId") String trainingReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            TrainingAttendUsers trainingAttendUsers = trainingAttendUsersService.findById(attendUsersId);
            trainingAttendUsers.setRemark(remark);

            trainingAttendUsersService.update(trainingAttendUsers);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除
     *
     * @param trainingReleaseId 发布id
     * @param attendUsersIds    ids
     * @return true注销成功
     */
    @PostMapping("/web/training/attend/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingReleaseId") String trainingReleaseId, String attendUsersIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(attendUsersIds)) {
            if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
                trainingAttendUsersService.deleteById(SmallPropsUtil.StringIdsToStringList(attendUsersIds));
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
     * @param trainingReleaseId 发布id
     * @param trainingAttendId  考勤id
     * @return true or false
     */
    @PostMapping("/web/training/attend/users/full-attendance")
    public ResponseEntity<Map<String, Object>> allOk(@RequestParam("trainingReleaseId") String trainingReleaseId, @RequestParam("trainingAttendId") String trainingAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            trainingAttendUsersService.updateOperateByTrainingAttendId(trainingAttendId, ByteUtil.toByte(3));
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 名单重置
     *
     * @param trainingReleaseId 实训发布id
     * @param trainingAttendId  考勤id
     * @return true or false
     */
    @PostMapping("/web/training/attend/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("trainingReleaseId") String trainingReleaseId, @RequestParam("trainingAttendId") String trainingAttendId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            Result<Record> records = trainingAttendUsersService.findStudentNotExistsUsers(trainingReleaseId, trainingAttendId);
            if (records.isNotEmpty()) {
                Users user = SessionUtil.getUserFromSession();
                List<TrainingUsers> trainingUsers = records.into(TrainingUsers.class);
                List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                for (TrainingUsers users : trainingUsers) {
                    TrainingAttendUsers au = new TrainingAttendUsers();
                    au.setAttendUsersId(UUIDUtil.getUUID());
                    au.setTrainingAttendId(trainingAttendId);
                    au.setTrainingUsersId(users.getTrainingUsersId());
                    au.setOperate(ByteUtil.toByte(0));
                    au.setOperateDate(DateTimeUtil.getNowLocalDateTime());
                    au.setOperateUser(user.getUsername());

                    trainingAttendUsers.add(au);
                }
                trainingAttendUsersService.batchSave(trainingAttendUsers);
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
    @GetMapping("/web/training/attend/users/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "实训考勤数据表", Workbook.trainingFilePath());
        List<TrainingAttendUsersBean> beans = new ArrayList<>();
        Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> records = trainingAttendUsersService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingAttendUsersBean.class);
            beans.forEach(bean -> {
                if (!trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 7));
                }
            });
        }

        TrainingAttendUsersExport export = new TrainingAttendUsersExport(beans);
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
    @GetMapping("/web/training/attend/my/paging")
    public ResponseEntity<Map<String, Object>> myData(TableSawUtil tableSawUtil) {
        AjaxUtil<TrainingAttendUsersBean> ajaxUtil = AjaxUtil.of();
        List<TrainingAttendUsersBean> beans = new ArrayList<>();
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    int studentId = optionalStudentBean.get().getStudentId();
                    tableSawUtil.setSearch("studentId", studentId);
                    Result<Record> records = trainingAttendMyService.findAll(tableSawUtil);
                    if (records.isNotEmpty()) {
                        beans = records.into(TrainingAttendUsersBean.class);
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
    @GetMapping("/web/training/attend/situation/paging")
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
                records = trainingAttendSituationService.findAllByPage(dataTablesUtil);
        List<TrainingAttendUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingAttendUsersBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(trainingAttendSituationService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(trainingAttendSituationService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 导出 考勤情况 数据
     *
     * @param request 请求
     */
    @GetMapping("/web/training/attend/situation/export")
    public void situationExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "实训考勤情况数据表", Workbook.trainingFilePath());
        List<TrainingAttendUsersBean> beans = new ArrayList<>();
        Result<Record> records = trainingAttendSituationService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingAttendUsersBean.class);
        }

        TrainingAttendSituationExport export = new TrainingAttendSituationExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }

}
