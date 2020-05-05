package top.zbeboy.zone.web.internship.review;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.internship.*;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.bean.internship.review.InternshipReviewAuthorizeBean;
import top.zbeboy.zone.web.bean.internship.review.InternshipReviewBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class InternshipReviewRestController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipControllerCommon internshipControllerCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipReviewService internshipReviewService;

    @Resource
    private InternshipReviewAuthorizeService internshipReviewAuthorizeService;

    @Resource
    private InternshipInfoService internshipInfoService;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private InternshipChangeHistoryService internshipChangeHistoryService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private UsersService usersService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private UserNotifyService userNotifyService;

    @Resource
    private StudentService studentService;

    @Resource
    private FilesService filesService;

    @Resource
    private UploadService uploadService;

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
            beans.forEach(bean->{
                if(BooleanUtil.toBoolean(bean.getIsTimeLimit())){
                    bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime()));
                    bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime()));
                    bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime()));
                    bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime()));
                }
            });
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
        Select2Data select2Data = internshipControllerCommon.internshipApplyOrganizeData(id);
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
                        Users checkUser = usersService.findByUsername(param);
                        if (Objects.nonNull(checkUser)) {
                            InternshipReviewAuthorize internshipReviewAuthorize = new InternshipReviewAuthorize(internshipReleaseId, param);
                            internshipReviewAuthorizeService.save(internshipReviewAuthorize);
                            ajaxUtil.success().msg("保存成功");
                        } else {
                            ajaxUtil.fail().msg("未查询到账号信息");
                        }
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

    /**
     * 实习审核 通过
     *
     * @param internshipReviewBean 数据
     * @return true or false
     */
    @PostMapping("/web/internship/review/pass")
    public ResponseEntity<Map<String, Object>> pass(InternshipReviewBean internshipReviewBean, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(internshipReviewBean.getInternshipReleaseId()) && Objects.nonNull(internshipReviewBean.getStudentId())) {
            if (internshipConditionCommon.reviewCondition(internshipReviewBean.getInternshipReleaseId())) {
                Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReviewBean.getInternshipReleaseId(), internshipReviewBean.getStudentId());
                if (internshipApplyRecord.isPresent()) {
                    InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                    internshipApply.setReason(internshipReviewBean.getReason());
                    internshipApply.setInternshipApplyState(internshipReviewBean.getInternshipApplyState());
                    internshipApplyService.update(internshipApply);

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
                    }

                    InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                    internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                    internshipChangeHistory.setInternshipReleaseId(internshipReviewBean.getInternshipReleaseId());
                    internshipChangeHistory.setStudentId(internshipReviewBean.getStudentId());
                    internshipChangeHistory.setState(internshipReviewBean.getInternshipApplyState());
                    internshipChangeHistory.setApplyTime(DateTimeUtil.getNowSqlTimestamp());
                    internshipChangeHistoryService.save(internshipChangeHistory);

                    InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReviewBean.getInternshipReleaseId());
                    if (Objects.nonNull(internshipRelease)) {
                        Users sendUser = usersService.getUserFromSession();
                        Optional<Record> studentRecord = studentService.findByIdRelation(internshipReviewBean.getStudentId());
                        if (studentRecord.isPresent()) {
                            Users acceptUsers = studentRecord.get().into(Users.class);

                            String notify = "您的实习 " + internshipRelease.getInternshipTitle() + " 申请已通过。";
                            // 检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                systemMailService.sendNotifyMail(acceptUsers, RequestUtil.getBaseUrl(request), notify);
                            }

                            UserNotify userNotify = new UserNotify();
                            userNotify.setUserNotifyId(UUIDUtil.getUUID());
                            userNotify.setSendUser(sendUser.getUsername());
                            userNotify.setAcceptUser(acceptUsers.getUsername());
                            userNotify.setIsSee(BooleanUtil.toByte(false));
                            userNotify.setNotifyType(Workbook.notifyType.success.name());
                            userNotify.setNotifyTitle("实习审核");
                            userNotify.setNotifyContent(notify);
                            userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                            userNotifyService.save(userNotify);
                        }
                    }

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请信息");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("必要参数为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 实习审核 不通过
     *
     * @param reason               原因
     * @param internshipApplyState 实习审核状态
     * @param internshipReleaseId  实习发布id
     * @param studentId            学生id
     * @return true or false
     */
    @PostMapping("/web/internship/review/fail")
    public ResponseEntity<Map<String, Object>> fail(@RequestParam("reason") String reason, @RequestParam("internshipApplyState") int internshipApplyState,
                                                    @RequestParam("internshipReleaseId") String internshipReleaseId, @RequestParam("studentId") int studentId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewCondition(internshipReleaseId)) {
            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
            if (internshipApplyRecord.isPresent()) {
                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                internshipApply.setInternshipApplyState(internshipApplyState);
                internshipApply.setReason(reason);
                internshipApplyService.update(internshipApply);

                InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
                internshipChangeHistory.setStudentId(studentId);
                internshipChangeHistory.setState(internshipApplyState);
                internshipChangeHistory.setApplyTime(DateTimeUtil.getNowSqlTimestamp());
                internshipChangeHistory.setReason(reason);
                internshipChangeHistoryService.save(internshipChangeHistory);

                InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
                if (Objects.nonNull(internshipRelease)) {
                    Users sendUser = usersService.getUserFromSession();
                    Optional<Record> studentRecord = studentService.findByIdRelation(studentId);
                    if (studentRecord.isPresent()) {
                        Users acceptUsers = studentRecord.get().into(Users.class);

                        String notify = "您的实习 " + internshipRelease.getInternshipTitle() + " 申请未通过。原因：" + reason;
                        // 检查邮件推送是否被关闭
                        SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                        if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                            systemMailService.sendNotifyMail(acceptUsers, RequestUtil.getBaseUrl(request), notify);
                        }

                        UserNotify userNotify = new UserNotify();
                        userNotify.setUserNotifyId(UUIDUtil.getUUID());
                        userNotify.setSendUser(sendUser.getUsername());
                        userNotify.setAcceptUser(acceptUsers.getUsername());
                        userNotify.setIsSee(BooleanUtil.toByte(false));
                        userNotify.setNotifyType(Workbook.notifyType.danger.name());
                        userNotify.setNotifyTitle("实习审核");
                        userNotify.setNotifyContent(notify);
                        userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                        userNotifyService.save(userNotify);
                    }
                }

                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("未查询到实习申请信息");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除申请记录
     *
     * @param internshipReleaseId 实习发布id
     * @param studentId           学生id
     * @return true or false
     */
    @PostMapping("/web/internship/review/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("internshipReleaseId") String internshipReleaseId, @RequestParam("studentId") int studentId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewCondition(internshipReleaseId)) {
            internshipInfoService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
            internshipApplyService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);

            InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
            internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
            internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
            internshipChangeHistory.setStudentId(studentId);
            internshipChangeHistory.setState(-1);
            internshipChangeHistory.setApplyTime(DateTimeUtil.getNowSqlTimestamp());
            internshipChangeHistory.setReason("实习申请删除");
            internshipChangeHistoryService.save(internshipChangeHistory);

            InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
            if (Objects.nonNull(internshipRelease)) {
                Users sendUser = usersService.getUserFromSession();
                Optional<Record> studentRecord = studentService.findByIdRelation(studentId);
                if (studentRecord.isPresent()) {
                    Users acceptUsers = studentRecord.get().into(Users.class);

                    String notify = "您的实习 " + internshipRelease.getInternshipTitle() + " 申请已被删除！本次申请将完全废除，若您有任何疑问，请及时联系指导教师。";
                    // 检查邮件推送是否被关闭
                    SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                    if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                        systemMailService.sendNotifyMail(acceptUsers, RequestUtil.getBaseUrl(request), notify);
                    }

                    UserNotify userNotify = new UserNotify();
                    userNotify.setUserNotifyId(UUIDUtil.getUUID());
                    userNotify.setSendUser(sendUser.getUsername());
                    userNotify.setAcceptUser(acceptUsers.getUsername());
                    userNotify.setIsSee(BooleanUtil.toByte(false));
                    userNotify.setNotifyType(Workbook.notifyType.danger.name());
                    userNotify.setNotifyTitle("实习审核");
                    userNotify.setNotifyContent(notify);
                    userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    userNotifyService.save(userNotify);
                }
            }

            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param id       文件id
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/web/internship/review/download/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        Files files = filesService.findById(id);
        if (Objects.nonNull(files)) {
            uploadService.download(files.getOriginalFileName(), files.getRelativePath(), response, request);
        }
    }

    /**
     * 实习审核 同意  基本信息修改申请 单位信息修改申请
     *
     * @param internshipReviewBean 数据
     * @return true or false
     */
    @PostMapping("/web/internship/review/agree")
    public ResponseEntity<Map<String, Object>> agree(InternshipReviewBean internshipReviewBean, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String internshipReleaseId = internshipReviewBean.getInternshipReleaseId();
        int studentId = internshipReviewBean.getStudentId();
        if (StringUtils.isNotBlank(internshipReleaseId) && Objects.nonNull(studentId)) {
            if (internshipConditionCommon.reviewCondition(internshipReleaseId)) {
                Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
                if (internshipApplyRecord.isPresent()) {
                    InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                    internshipApply.setReason(internshipReviewBean.getReason());
                    internshipApply.setInternshipApplyState(internshipReviewBean.getInternshipApplyState());
                    if (StringUtils.isNotBlank(internshipReviewBean.getFillTime())) {
                        if (internshipReviewBean.getFillTime().contains("至")) {
                            String[] timeArr = internshipReviewBean.getFillTime().split(" 至 ");
                            internshipApply.setChangeFillStartTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[0] + " 00:00:00"));
                            internshipApply.setChangeFillEndTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[1] + " 23:59:59"));
                        } else {
                            internshipApply.setChangeFillStartTime(DateTimeUtil.defaultParseSqlTimestamp(internshipReviewBean.getFillTime() + " 00:00:00"));
                            internshipApply.setChangeFillEndTime(DateTimeUtil.defaultParseSqlTimestamp(internshipReviewBean.getFillTime() + " 23:59:59"));
                        }

                    }
                    internshipApplyService.update(internshipApply);

                    // 若同意进入 7：单位信息变更填写中 需要删除提交材料的状态
                    if (internshipReviewBean.getInternshipApplyState() == 7) {
                        Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
                        if (internshipInfoRecord.isPresent()) {
                            InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                            Byte b = ByteUtil.toByte(0);
                            internshipInfo.setCommitmentBook(b);
                            internshipInfo.setSafetyResponsibilityBook(b);
                            internshipInfo.setPracticeAgreement(b);
                            internshipInfo.setInternshipApplication(b);
                            internshipInfo.setPracticeReceiving(b);
                            internshipInfo.setSecurityEducationAgreement(b);
                            internshipInfo.setParentalConsent(b);
                            internshipInfoService.update(internshipInfo);
                        }
                    }

                    InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                    internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                    internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
                    internshipChangeHistory.setStudentId(studentId);
                    internshipChangeHistory.setState(internshipReviewBean.getInternshipApplyState());
                    internshipChangeHistory.setApplyTime(DateTimeUtil.getNowSqlTimestamp());
                    internshipChangeHistory.setReason(internshipReviewBean.getReason());
                    internshipChangeHistory.setChangeFillStartTime(internshipApply.getChangeFillStartTime());
                    internshipChangeHistory.setChangeFillEndTime(internshipApply.getChangeFillEndTime());
                    internshipChangeHistoryService.save(internshipChangeHistory);

                    InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
                    if (Objects.nonNull(internshipRelease)) {
                        Users sendUser = usersService.getUserFromSession();
                        Optional<Record> studentRecord = studentService.findByIdRelation(studentId);
                        if (studentRecord.isPresent()) {
                            Users acceptUsers = studentRecord.get().into(Users.class);

                            String notify = "您的实习 " + internshipRelease.getInternshipTitle() + " 变更申请已通过。请尽快登录系统在填写时间范围内变更您的内容。";
                            // 检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                systemMailService.sendNotifyMail(acceptUsers, RequestUtil.getBaseUrl(request), notify);
                            }

                            UserNotify userNotify = new UserNotify();
                            userNotify.setUserNotifyId(UUIDUtil.getUUID());
                            userNotify.setSendUser(sendUser.getUsername());
                            userNotify.setAcceptUser(acceptUsers.getUsername());
                            userNotify.setIsSee(BooleanUtil.toByte(false));
                            userNotify.setNotifyType(Workbook.notifyType.success.name());
                            userNotify.setNotifyTitle("实习审核");
                            userNotify.setNotifyContent(notify);
                            userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                            userNotifyService.save(userNotify);
                        }
                    }

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请信息");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("必要参数为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 实习审核 拒绝  基本信息修改申请 单位信息修改申请
     *
     * @param internshipReviewBean 数据
     * @return true or false
     */
    @PostMapping("/web/internship/review/disagree")
    public ResponseEntity<Map<String, Object>> disagree(InternshipReviewBean internshipReviewBean, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String internshipReleaseId = internshipReviewBean.getInternshipReleaseId();
        int studentId = internshipReviewBean.getStudentId();
        if (StringUtils.isNotBlank(internshipReleaseId) && Objects.nonNull(studentId)) {
            if (internshipConditionCommon.reviewCondition(internshipReleaseId)) {
                Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
                if (internshipApplyRecord.isPresent()) {
                    InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                    internshipApply.setReason(internshipReviewBean.getReason());
                    internshipApply.setInternshipApplyState(internshipReviewBean.getInternshipApplyState());
                    internshipApplyService.update(internshipApply);
                    InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                    internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                    internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
                    internshipChangeHistory.setStudentId(studentId);
                    internshipChangeHistory.setState(internshipReviewBean.getInternshipApplyState());
                    internshipChangeHistory.setApplyTime(DateTimeUtil.getNowSqlTimestamp());
                    internshipChangeHistory.setReason(internshipReviewBean.getReason());
                    internshipChangeHistoryService.save(internshipChangeHistory);

                    InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
                    if (Objects.nonNull(internshipRelease)) {
                        Users sendUser = usersService.getUserFromSession();
                        Optional<Record> studentRecord = studentService.findByIdRelation(studentId);
                        if (studentRecord.isPresent()) {
                            Users acceptUsers = studentRecord.get().into(Users.class);

                            String notify = "您的实习 " + internshipRelease.getInternshipTitle() + " 变更申请被拒绝！若您有任何疑问，请及时联系指导教师。";
                            // 检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                systemMailService.sendNotifyMail(acceptUsers, RequestUtil.getBaseUrl(request), notify);
                            }

                            UserNotify userNotify = new UserNotify();
                            userNotify.setUserNotifyId(UUIDUtil.getUUID());
                            userNotify.setSendUser(sendUser.getUsername());
                            userNotify.setAcceptUser(acceptUsers.getUsername());
                            userNotify.setIsSee(BooleanUtil.toByte(false));
                            userNotify.setNotifyType(Workbook.notifyType.danger.name());
                            userNotify.setNotifyTitle("实习审核");
                            userNotify.setNotifyContent(notify);
                            userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                            userNotifyService.save(userNotify);
                        }
                    }

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请信息");
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
