package top.zbeboy.zone.web.internship.statistical;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zone.service.internship.*;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.bean.internship.statistical.InternshipChangeCompanyHistoryBean;
import top.zbeboy.zone.web.bean.internship.statistical.InternshipChangeHistoryBean;
import top.zbeboy.zone.web.bean.internship.statistical.InternshipStatisticalBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class InternshipStatisticalRestController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipStatisticalService internshipStatisticalService;

    @Resource
    private InternshipControllerCommon internshipControllerCommon;

    @Resource
    private InternshipChangeHistoryService internshipChangeHistoryService;

    @Resource
    private InternshipChangeCompanyHistoryService internshipChangeCompanyHistoryService;

    @Resource
    private InternshipInfoService internshipInfoService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/statistical/internship/data")
    public ResponseEntity<Map<String, Object>> internshipData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime())));
            beans.forEach(bean -> bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime())));
            beans.forEach(bean -> bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime())));
            beans.forEach(bean -> bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime())));
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> {
                bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.reviewCondition(bean.getInternshipReleaseId())));
                if (BooleanUtil.toBoolean(bean.getCanOperator())) {
                    bean.setSubmittedTotalData(internshipStatisticalService.countSubmitted(bean.getInternshipReleaseId()));
                    bean.setUnsubmittedTotalData(internshipStatisticalService.countUnSubmitted(bean.getInternshipReleaseId()));
                }
            });
            beans.forEach(bean -> bean.setCanAuthorize(BooleanUtil.toByte(internshipConditionCommon.reviewAuthorizeCondition(bean.getInternshipReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/internship/statistical/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("realName");
        headers.add("studentNumber");
        headers.add("scienceName");
        headers.add("organizeName");
        headers.add("internshipApplyState");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = internshipStatisticalService.findAllByPage(dataTablesUtil);
        List<InternshipStatisticalBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(InternshipStatisticalBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(internshipStatisticalService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(internshipStatisticalService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 获取班级数据
     *
     * @param id 实习发布id
     * @return 班级数据
     */
    @GetMapping("/web/internship/statistical/organize/{id}")
    public ResponseEntity<Map<String, Object>> organize(@PathVariable("id") String id) {
        Select2Data select2Data = internshipControllerCommon.internshipApplyOrganizeData(id);
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 申请变更记录数据
     *
     * @param id        实习发布id
     * @param studentId 学生id
     * @return 数据
     */
    @GetMapping("/web/internship/statistical/record/apply/data/{id}/{studentId}")
    public ResponseEntity<Map<String, Object>> changeHistoryData(@PathVariable("id") String id, @PathVariable("studentId") int studentId) {
        AjaxUtil<InternshipChangeHistoryBean> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewCondition(id)) {
            List<InternshipChangeHistoryBean> beans = new ArrayList<>();
            Result<Record> records = internshipChangeHistoryService.findByInternshipReleaseIdAndStudentId(id, studentId);
            if (records.isNotEmpty()) {
                beans = records.into(InternshipChangeHistoryBean.class);
                beans.forEach(bean -> bean.setChangeFillStartTimeStr(Objects.nonNull(bean.getChangeFillStartTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillStartTime()) : ""));
                beans.forEach(bean -> bean.setChangeFillEndTimeStr(Objects.nonNull(bean.getChangeFillEndTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillEndTime()) : ""));
                beans.forEach(bean -> bean.setApplyTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getApplyTime())));
            }
            ajaxUtil.success().msg("获取数据成功").list(beans);
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 单位变更记录数据
     *
     * @param id        实习发布id
     * @param studentId 学生id
     * @return 数据
     */
    @GetMapping("/web/internship/statistical/record/company/data/{id}/{studentId}")
    public ResponseEntity<Map<String, Object>> changeCompanyData(@PathVariable("id") String id, @PathVariable("studentId") int studentId) {
        AjaxUtil<InternshipChangeCompanyHistoryBean> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewCondition(id)) {
            List<InternshipChangeCompanyHistoryBean> beans = new ArrayList<>();
            Result<Record> records = internshipChangeCompanyHistoryService.findByInternshipReleaseIdAndStudentId(id, studentId);
            if (records.isNotEmpty()) {
                beans = records.into(InternshipChangeCompanyHistoryBean.class);
                beans.forEach(bean -> bean.setChangeTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeTime())));
            }
            ajaxUtil.success().msg("获取数据成功").list(beans);
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/internship/statistical/info/data")
    public ResponseEntity<DataTablesUtil> infoData(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("studentName");
        headers.add("organizeName");
        headers.add("studentSex");
        headers.add("studentNumber");
        headers.add("mobile");
        headers.add("qqMailbox");
        headers.add("parentContactPhone");
        headers.add("headmaster");
        headers.add("headmasterTel");
        headers.add("companyName");
        headers.add("companyAddress");
        headers.add("companyContact");
        headers.add("companyMobile");
        headers.add("schoolGuidanceTeacher");
        headers.add("schoolGuidanceTeacherTel");
        headers.add("startTime");
        headers.add("endTime");
        headers.add("commitmentBook");
        headers.add("safetyResponsibilityBook");
        headers.add("practiceAgreement");
        headers.add("internshipApplication");
        headers.add("practiceReceiving");
        headers.add("securityEducationAgreement");
        headers.add("parentalConsent");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = internshipInfoService.findAllByPage(dataTablesUtil);
        List<InternshipInfo> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(InternshipInfo.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(internshipInfoService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(internshipInfoService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
