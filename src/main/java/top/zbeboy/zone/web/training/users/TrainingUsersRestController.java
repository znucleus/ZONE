package top.zbeboy.zone.web.training.users;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.bean.training.users.TrainingUsersBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zbase.domain.tables.pojos.TrainingUsers;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.records.TrainingUsersRecord;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.SmallPropsUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ExportInfo;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.export.TrainingUsersExport;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.training.TrainingUsersService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
public class TrainingUsersRestController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingControllerCommon trainingControllerCommon;

    @Resource
    private StudentService studentService;

    @Resource
    private UploadService uploadService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/users/training/paging")
    public ResponseEntity<Map<String, Object>> trainingData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingReleaseBean> ajaxUtil = trainingControllerCommon.trainingData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/training/users/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("studentNumber");
        headers.add("username");
        headers.add("mobile");
        headers.add("email");
        headers.add("sex");
        headers.add("remark");
        headers.add("createDateStr");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        Result<Record> records = trainingUsersService.findAllByPage(dataTablesUtil);
        List<TrainingUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingUsersBean.class);
            beans.forEach(bean -> {
                bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
                if (!trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 6));
                }
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(trainingUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(trainingUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 添加学生
     *
     * @param trainingReleaseId 实训发布id
     * @param studentNumber     学号
     * @return true or false
     */
    @PostMapping("/web/training/users/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("trainingReleaseId") String trainingReleaseId,
                                                    @RequestParam("studentNumber") String studentNumber, String remark) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            String param = StringUtils.deleteWhitespace(studentNumber);
            Optional<StudentBean> optionalStudentBean = studentService.findNormalByStudentNumberRelation(param);
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                Optional<TrainingUsersRecord> trainingUsersRecord = trainingUsersService.findByTrainingReleaseIdAndStudentId(trainingReleaseId, studentBean.getStudentId());
                if (!trainingUsersRecord.isPresent()) {
                    TrainingUsers trainingUsers = new TrainingUsers();
                    trainingUsers.setTrainingUsersId(UUIDUtil.getUUID());
                    trainingUsers.setTrainingReleaseId(trainingReleaseId);
                    trainingUsers.setStudentId(studentBean.getStudentId());
                    trainingUsers.setRemark(remark);
                    trainingUsers.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    trainingUsersService.save(trainingUsers);

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("学生已在名单中");
                }
            } else {
                ajaxUtil.fail().msg("未查询到学生信息或账号状态不正常");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param trainingUsersId 名单id
     * @param remark          备注
     * @return true or false
     */
    @PostMapping("/web/training/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("trainingUsersId") String trainingUsersId, String remark) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingUsers trainingUsers = trainingUsersService.findById(trainingUsersId);
        if (Objects.nonNull(trainingUsers)) {
            if (trainingConditionCommon.usersCondition(trainingUsers.getTrainingReleaseId())) {
                trainingUsers.setRemark(remark);
                trainingUsersService.update(trainingUsers);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("根据ID未查询到名单数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除
     *
     * @param trainingReleaseId 实训发布id
     * @param trainingUsersIds  ids
     * @return true注销成功
     */
    @PostMapping("/web/training/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingReleaseId") String trainingReleaseId, String trainingUsersIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(trainingUsersIds)) {
            if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
                trainingUsersService.deleteById(SmallPropsUtil.StringIdsToStringList(trainingUsersIds));
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
     * 名单重置
     *
     * @param trainingReleaseId 实训发布id
     * @return true or false
     */
    @PostMapping("/web/training/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("trainingReleaseId") String trainingReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.usersCondition(trainingReleaseId)) {
            TrainingRelease trainingRelease = trainingReleaseService.findById(trainingReleaseId);
            if (Objects.nonNull(trainingRelease)) {
                Result<Record> records = trainingUsersService.findStudentNotExistsUsers(trainingReleaseId, trainingRelease.getOrganizeId());
                if (records.isNotEmpty()) {
                    List<Student> students = records.into(Student.class);
                    List<TrainingUsers> trainingUsers = new ArrayList<>();
                    for (Student student : students) {
                        TrainingUsers au = new TrainingUsers();
                        au.setTrainingUsersId(UUIDUtil.getUUID());
                        au.setTrainingReleaseId(trainingReleaseId);
                        au.setStudentId(student.getStudentId());
                        au.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                        trainingUsers.add(au);
                    }
                    trainingUsersService.batchSave(trainingUsers);
                }

                ajaxUtil.success().msg("重置成功");
            } else {
                ajaxUtil.fail().msg("根据ID未查询到实训发布信息");
            }
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
    @GetMapping("/web/training/users/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "实训名单数据表", Workbook.trainingFilePath());
        List<TrainingUsersBean> beans = new ArrayList<>();
        Result<Record> records = trainingUsersService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingUsersBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
            beans.forEach(bean -> {
                if (!trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 6));
                }
            });
        }

        TrainingUsersExport export = new TrainingUsersExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }
}
