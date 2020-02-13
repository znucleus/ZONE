package top.zbeboy.zone.web.internship.review;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.internship.InternshipInfoService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipReviewAuthorizeService;
import top.zbeboy.zone.service.internship.InternshipReviewService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.bean.internship.review.InternshipReviewAuthorizeBean;
import top.zbeboy.zone.web.bean.internship.review.InternshipReviewBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class InternshipReviewRestController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipReviewService internshipReviewService;

    @Resource
    private InternshipReviewAuthorizeService internshipReviewAuthorizeService;

    @Resource
    private InternshipInfoService internshipInfoService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private UsersService usersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/review/internship/data")
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
                    bean.setWaitTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 1));
                    bean.setPassTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 2));
                    bean.setFailTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 3));
                    bean.setBasicApplyTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 4));
                    bean.setCompanyApplyTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 6));
                    bean.setBasicFillTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 5));
                    bean.setCompanyFillTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 7));
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
     * @return 数据
     */
    @GetMapping("/web/internship/review/authorize/data")
    public ResponseEntity<Map<String, Object>> authorizeData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReviewAuthorizeBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReviewAuthorizeBean> beans = new ArrayList<>();
        Result<Record> records = internshipReviewAuthorizeService.findAll(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReviewAuthorizeBean.class);
        }
        simplePaginationUtil.setTotalSize(internshipReviewAuthorizeService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/review/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReviewBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReviewBean> beans = new ArrayList<>();
        Result<Record> records = internshipReviewService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReviewBean.class);
            beans.forEach(bean -> bean.setChangeFillStartTimeStr(Objects.nonNull(bean.getChangeFillStartTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillStartTime()) : ""));
            beans.forEach(bean -> bean.setChangeFillEndTimeStr(Objects.nonNull(bean.getChangeFillEndTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillEndTime()) : ""));
            beans.forEach(bean -> bean.setApplyTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getApplyTime())));
        }
        simplePaginationUtil.setTotalSize(internshipReviewService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取班级数据
     *
     * @param id 实习发布id
     * @return 班级数据
     */
    @GetMapping("/web/internship/review/organize/{id}")
    public ResponseEntity<Map<String, Object>> organize(@PathVariable("id") String id) {
        Select2Data select2Data = Select2Data.of();
        List<Organize> organizes = new ArrayList<>();
        Result<Record2<Integer, String>> records = internshipReviewService.findDistinctOrganize(id);
        if (records.isNotEmpty()) {
            organizes = records.into(Organize.class);
        }
        organizes.forEach(organize -> select2Data.add(organize.getOrganizeId().toString(), organize.getOrganizeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 保存权限
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     * @return true or false
     */
    @PostMapping("/web/internship/review/authorize/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("internshipReleaseId") String internshipReleaseId,
                                                    @RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(username);
        if (internshipConditionCommon.reviewAuthorizeCondition(internshipReleaseId)) {
            // 系统或管理员不需要添加权限
            List<String> authorities = new ArrayList<>();
            authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
            authorities.add(Workbook.authorities.ROLE_ADMIN.name());
            Result<Record> authorityRecord = authoritiesService.findByUsernameAndInAuthorities(param, authorities);
            if (authorityRecord.isEmpty()) {
                // 本人无需添加权限
                Users users = usersService.getUserFromSession();
                if (!StringUtils.equals(users.getUsername(), param)) {
                    Optional<Record> record = internshipReviewAuthorizeService.findByInternshipReleaseIdAndUsername(internshipReleaseId, param);
                    if (!record.isPresent()) {
                        InternshipReviewAuthorize internshipReviewAuthorize = new InternshipReviewAuthorize(internshipReleaseId, param);
                        internshipReviewAuthorizeService.save(internshipReviewAuthorize);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("该账号已有权限");
                    }
                } else {
                    ajaxUtil.fail().msg("本人无需添加权限");
                }
            } else {
                ajaxUtil.fail().msg("系统或管理员无需添加权限");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     * @return true or false
     */
    @PostMapping("/web/internship/review/authorize/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("internshipReleaseId") String internshipReleaseId,
                                                      @RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewAuthorizeCondition(internshipReleaseId)) {
            internshipReviewAuthorizeService.deleteByInternshipReleaseIdAndUsername(internshipReleaseId, username);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 详情数据
     *
     * @param id        实习发布id
     * @param studentId 学生id
     * @return 数据
     */
    @GetMapping("/web/internship/review/detail/{id}/{studentId}")
    public ResponseEntity<Map<String, Object>> data(@PathVariable("id") String id, @PathVariable("studentId") int studentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewCondition(id)) {
            Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(id, studentId);
            if (internshipInfoRecord.isPresent()) {
                InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                ajaxUtil.success().msg("获取数据成功").put("internshipInfo", internshipInfo);
            } else {
                ajaxUtil.fail().msg("获取数据失败");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 实习审核 保存
     *
     * @param internshipReviewBean 数据
     * @return true or false
     */
    @PostMapping("/web/internship/review/save")
    public ResponseEntity<Map<String, Object>> save(InternshipReviewBean internshipReviewBean) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(internshipReviewBean.getInternshipReleaseId()) && Objects.nonNull(internshipReviewBean.getStudentId())) {
            if (internshipConditionCommon.reviewCondition(internshipReviewBean.getInternshipReleaseId())) {
                Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(internshipReviewBean.getInternshipReleaseId(), internshipReviewBean.getStudentId());
                if (internshipInfoRecord.isPresent()) {
                    InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                    internshipInfo.setCommitmentBook(internshipReviewBean.getCommitmentBook());
                    internshipInfo.setSafetyResponsibilityBook(internshipReviewBean.getSafetyResponsibilityBook());
                    internshipInfo.setPracticeAgreement(internshipReviewBean.getPracticeAgreement());
                    internshipInfo.setInternshipApplication(internshipReviewBean.getInternshipApplication());
                    internshipInfo.setPracticeReceiving(internshipReviewBean.getPracticeReceiving());
                    internshipInfo.setSecurityEducationAgreement(internshipReviewBean.getSecurityEducationAgreement());
                    internshipInfo.setParentalConsent(internshipReviewBean.getParentalConsent());
                    internshipInfoService.update(internshipInfo);

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实习数据");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("必要参数为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
