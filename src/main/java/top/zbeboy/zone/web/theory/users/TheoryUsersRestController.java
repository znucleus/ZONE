package top.zbeboy.zone.web.theory.users;

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
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.bean.theory.users.TheoryUsersBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.TheoryRelease;
import top.zbeboy.zbase.domain.tables.pojos.TheoryUsers;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.records.TheoryUsersRecord;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.SmallPropsUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ExportInfo;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.export.TheoryUsersExport;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.service.theory.TheoryUsersService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;
import top.zbeboy.zone.web.theory.common.TheoryControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
public class TheoryUsersRestController {

    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryUsersService theoryUsersService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    @Resource
    private TheoryControllerCommon theoryControllerCommon;

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
    @GetMapping("/web/theory/users/theory/paging")
    public ResponseEntity<Map<String, Object>> theoryData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TheoryReleaseBean> ajaxUtil = theoryControllerCommon.theoryData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/theory/users/paging")
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
        Result<Record> records = theoryUsersService.findAllByPage(dataTablesUtil);
        List<TheoryUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryUsersBean.class);
            beans.forEach(bean -> {
                bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
                if (!theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 7));
                }
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(theoryUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(theoryUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 添加学生
     *
     * @param theoryReleaseId 理论发布id
     * @param studentNumber   学号
     * @return true or false
     */
    @PostMapping("/web/theory/users/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("theoryReleaseId") String theoryReleaseId,
                                                    @RequestParam("studentNumber") String studentNumber, String remark) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            String param = StringUtils.deleteWhitespace(studentNumber);
            Optional<StudentBean> optionalStudentBean = studentService.findNormalByStudentNumberRelation(param);
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                Optional<TheoryUsersRecord> theoryUsersRecord = theoryUsersService.findByTheoryReleaseIdAndStudentId(theoryReleaseId, studentBean.getStudentId());
                if (!theoryUsersRecord.isPresent()) {
                    TheoryUsers theoryUsers = new TheoryUsers();
                    theoryUsers.setTheoryUsersId(UUIDUtil.getUUID());
                    theoryUsers.setTheoryReleaseId(theoryReleaseId);
                    theoryUsers.setStudentId(studentBean.getStudentId());
                    theoryUsers.setRemark(remark);
                    theoryUsers.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    theoryUsersService.save(theoryUsers);

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
     * @param theoryUsersId 名单id
     * @param remark        备注
     * @return true or false
     */
    @PostMapping("/web/theory/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("theoryUsersId") String theoryUsersId, String remark) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TheoryUsers theoryUsers = theoryUsersService.findById(theoryUsersId);
        if (Objects.nonNull(theoryUsers)) {
            if (theoryConditionCommon.usersCondition(theoryUsers.getTheoryReleaseId())) {
                theoryUsers.setRemark(remark);
                theoryUsersService.update(theoryUsers);
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
     * @param theoryReleaseId 理论发布id
     * @param theoryUsersIds  ids
     * @return true注销成功
     */
    @PostMapping("/web/theory/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("theoryReleaseId") String theoryReleaseId, String theoryUsersIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(theoryUsersIds)) {
            if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
                theoryUsersService.deleteById(SmallPropsUtil.StringIdsToStringList(theoryUsersIds));
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
     * @param theoryReleaseId 理论发布id
     * @return true or false
     */
    @PostMapping("/web/theory/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("theoryReleaseId") String theoryReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (theoryConditionCommon.usersCondition(theoryReleaseId)) {
            TheoryRelease theoryRelease = theoryReleaseService.findById(theoryReleaseId);
            if (Objects.nonNull(theoryRelease)) {
                Result<Record> records = theoryUsersService.findStudentNotExistsUsers(theoryReleaseId, theoryRelease.getOrganizeId());
                if (records.isNotEmpty()) {
                    List<Student> students = records.into(Student.class);
                    List<TheoryUsers> theoryUsers = new ArrayList<>();
                    for (Student student : students) {
                        TheoryUsers au = new TheoryUsers();
                        au.setTheoryUsersId(UUIDUtil.getUUID());
                        au.setTheoryReleaseId(theoryReleaseId);
                        au.setStudentId(student.getStudentId());
                        au.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                        theoryUsers.add(au);
                    }
                    theoryUsersService.batchSave(theoryUsers);
                }

                ajaxUtil.success().msg("重置成功");
            } else {
                ajaxUtil.fail().msg("根据ID未查询到理论发布信息");
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
    @GetMapping("/web/theory/users/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "理论名单数据表", Workbook.theoryFilePath());
        List<TheoryUsersBean> beans = new ArrayList<>();
        Result<Record> records = theoryUsersService.export(dataTablesUtil);
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TheoryUsersBean.class);
            beans.forEach(bean -> {
                bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
                if (!theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 7));
                }
            });
        }

        TheoryUsersExport export = new TheoryUsersExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }
}
