package top.zbeboy.zone.web.internship.apply;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.internship.apply.InternshipApplyBean;
import top.zbeboy.zbase.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.internship.apply.InternshipApplyAddVo;
import top.zbeboy.zbase.vo.internship.apply.InternshipApplyEditVo;
import top.zbeboy.zone.service.internship.*;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class InternshipApplyRestController {

    private final Logger log = LoggerFactory.getLogger(InternshipApplyRestController.class);

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipControllerCommon internshipControllerCommon;

    @Resource
    private InternshipInfoService internshipInfoService;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private InternshipChangeCompanyHistoryService internshipChangeCompanyHistoryService;

    @Resource
    private InternshipChangeHistoryService internshipChangeHistoryService;

    @Resource
    private FilesService filesService;

    @Resource
    private UploadService uploadService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/apply/internship/paging")
    public ResponseEntity<Map<String, Object>> internshipData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> {
                if (BooleanUtil.toBoolean(bean.getIsTimeLimit())) {
                    bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getTeacherDistributionStartTime()));
                    bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getTeacherDistributionEndTime()));
                    bean.setStartTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getStartTime()));
                    bean.setEndTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getEndTime()));
                }
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getReleaseTime()));
                bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.applyCondition(bean.getInternshipReleaseId())));
            });
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/apply/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipApplyBean> ajaxUtil = AjaxUtil.of();
        List<InternshipApplyBean> beans = new ArrayList<>();
        Result<Record> records = internshipApplyService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipApplyBean.class);
            beans.forEach(bean -> {
                if (BooleanUtil.toBoolean(bean.getIsTimeLimit())) {
                    bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getTeacherDistributionStartTime()));
                    bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getTeacherDistributionEndTime()));
                    bean.setStartTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getStartTime()));
                    bean.setEndTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getEndTime()));
                }
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getReleaseTime()));
                bean.setApplyTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getApplyTime()));
                bean.setChangeFillStartTimeStr(Objects.nonNull(bean.getChangeFillStartTime()) ? DateTimeUtil.defaultFormatLocalDateTime(bean.getChangeFillStartTime()) : "");
                bean.setChangeFillEndTimeStr(Objects.nonNull(bean.getChangeFillEndTime()) ? DateTimeUtil.defaultFormatLocalDateTime(bean.getChangeFillEndTime()) : "");
                bean.setCanEdit(BooleanUtil.toByte(internshipConditionCommon.applyEditCondition(bean.getInternshipReleaseId())));
            });
        }
        simplePaginationUtil.setTotalSize(internshipApplyService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取班主任数据
     *
     * @param id 实习发布id
     * @return 数据
     */
    @GetMapping("/web/internship/apply/staff/{id}")
    public ResponseEntity<Map<String, Object>> staff(@PathVariable("id") String id) {
        Select2Data select2Data = Select2Data.of();
        List<StaffBean> beans = internshipControllerCommon.internshipReleaseStaffData(id);
        beans.forEach(bean -> select2Data.add(bean.getStaffId().toString(), bean.getRealName() + " " + bean.getMobile()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param id       文件id
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/web/internship/apply/download/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        Optional<Files> optionalFiles = filesService.findById(id);
        if (optionalFiles.isPresent()) {
            Files files = optionalFiles.get();
            uploadService.download(files.getOriginalFileName(), files.getRelativePath(), response, request);
        }
    }

    /**
     * 保存
     *
     * @param internshipApplyAddVo 数据
     * @param bindingResult        检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/internship/apply/save")
    public ResponseEntity<Map<String, Object>> save(@Valid InternshipApplyAddVo internshipApplyAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (internshipConditionCommon.applyCondition(internshipApplyAddVo.getInternshipReleaseId())) {
                InternshipRelease internshipRelease = internshipReleaseService.findById(internshipApplyAddVo.getInternshipReleaseId());
                if (Objects.nonNull(internshipRelease)) {
                    Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(internshipApplyAddVo.getStaffId());
                    if (optionalStaffBean.isPresent()) {
                        StaffBean bean = optionalStaffBean.get();
                        internshipApplyAddVo.setHeadmaster(bean.getRealName());
                        internshipApplyAddVo.setHeadmasterTel(bean.getMobile());

                        String[] schoolGuidanceTeacherArr = internshipApplyAddVo.getSchoolGuidanceTeacher().split(" ");
                        if (schoolGuidanceTeacherArr.length > 1) {
                            internshipApplyAddVo.setSchoolGuidanceTeacher(schoolGuidanceTeacherArr[0]);
                            internshipApplyAddVo.setSchoolGuidanceTeacherTel(schoolGuidanceTeacherArr[1]);
                        }

                        boolean isTimeLimit = BooleanUtil.toBoolean(internshipRelease.getIsTimeLimit());
                        if (isTimeLimit) {
                            internshipApplyAddVo.setState(0);
                        } else {
                            internshipApplyAddVo.setState(1);
                        }
                        internshipInfoService.saveWithTransaction(internshipApplyAddVo);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("未查询到班级任信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到实习发布数据");
                }


            } else {
                ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param internshipApplyEditVo 数据
     * @param bindingResult         检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/internship/apply/update")
    public ResponseEntity<Map<String, Object>> update(@Valid InternshipApplyEditVo internshipApplyEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (internshipConditionCommon.applyEditCondition(internshipApplyEditVo.getInternshipReleaseId())) {
                Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipApplyEditVo.getInternshipReleaseId(), internshipApplyEditVo.getStudentId());
                if (internshipApplyRecord.isPresent()) {
                    InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);

                    Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(internshipApplyEditVo.getInternshipReleaseId(), internshipApplyEditVo.getStudentId());
                    if (internshipInfoRecord.isPresent()) {
                        InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                        if (internshipApply.getInternshipApplyState() == 5 ||
                                internshipApply.getInternshipApplyState() == 3 ||
                                internshipApply.getInternshipApplyState() == 0) {
                            Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(internshipApplyEditVo.getStaffId());
                            if (optionalStaffBean.isPresent()) {
                                StaffBean bean = optionalStaffBean.get();
                                internshipApplyEditVo.setHeadmaster(bean.getRealName());
                                internshipApplyEditVo.setHeadmasterTel(bean.getMobile());

                                String[] schoolGuidanceTeacherArr = internshipApplyEditVo.getSchoolGuidanceTeacher().split(" ");
                                if (schoolGuidanceTeacherArr.length > 1) {
                                    internshipApplyEditVo.setSchoolGuidanceTeacher(schoolGuidanceTeacherArr[0]);
                                    internshipApplyEditVo.setSchoolGuidanceTeacherTel(schoolGuidanceTeacherArr[1]);
                                }
                            }
                        }

                        if (internshipApply.getInternshipApplyState() == 5) {// 5：基本信息变更填写中
                            internshipInfo.setStudentName(internshipApplyEditVo.getRealName());
                            internshipInfo.setOrganizeName(internshipApplyEditVo.getOrganizeName());
                            internshipInfo.setStudentSex(internshipApplyEditVo.getStudentSex());
                            internshipInfo.setStudentNumber(internshipApplyEditVo.getStudentNumber());
                            internshipInfo.setMobile(internshipApplyEditVo.getMobile());
                            internshipInfo.setQqMailbox(internshipApplyEditVo.getQqMailbox());
                            internshipInfo.setParentContactPhone(internshipApplyEditVo.getParentContactPhone());
                            internshipInfo.setHeadmaster(internshipApplyEditVo.getHeadmaster());
                            internshipInfo.setHeadmasterTel(internshipApplyEditVo.getHeadmasterTel());
                            internshipInfo.setSchoolGuidanceTeacher(internshipApplyEditVo.getSchoolGuidanceTeacher());
                            internshipInfo.setSchoolGuidanceTeacherTel(internshipApplyEditVo.getSchoolGuidanceTeacherTel());
                            internshipInfo.setStartTime(DateTimeUtil.defaultParseLocalDate(internshipApplyEditVo.getStartTime()));
                            internshipInfo.setEndTime(DateTimeUtil.defaultParseLocalDate(internshipApplyEditVo.getEndTime()));

                            internshipInfoService.update(internshipInfo);
                            ajaxUtil.success().msg("更新成功");
                        } else if (internshipApply.getInternshipApplyState() == 7) {// 7：单位信息变更填写中
                            Result<Record> internshipChangeCompanyHistoryRecord = internshipChangeCompanyHistoryService.findByInternshipReleaseIdAndStudentId(internshipApplyEditVo.getInternshipReleaseId(), internshipApplyEditVo.getStudentId());
                            if (internshipChangeCompanyHistoryRecord.isEmpty()) {
                                InternshipChangeCompanyHistory internshipChangeCompanyHistory = new InternshipChangeCompanyHistory();
                                internshipChangeCompanyHistory.setInternshipChangeCompanyHistoryId(UUIDUtil.getUUID());
                                internshipChangeCompanyHistory.setInternshipReleaseId(internshipInfo.getInternshipReleaseId());
                                internshipChangeCompanyHistory.setStudentId(internshipInfo.getStudentId());
                                internshipChangeCompanyHistory.setChangeTime(DateTimeUtil.getNowLocalDateTime());
                                internshipChangeCompanyHistory.setCompanyName(internshipInfo.getCompanyName());
                                internshipChangeCompanyHistory.setCompanyAddress(internshipInfo.getCompanyAddress());
                                internshipChangeCompanyHistory.setCompanyContacts(internshipInfo.getCompanyContact());
                                internshipChangeCompanyHistory.setCompanyTel(internshipInfo.getCompanyMobile());
                                internshipChangeCompanyHistoryService.save(internshipChangeCompanyHistory);
                            }

                            internshipInfo.setCompanyName(internshipApplyEditVo.getCompanyName());
                            internshipInfo.setCompanyAddress(internshipApplyEditVo.getCompanyAddress());
                            internshipInfo.setCompanyContact(internshipApplyEditVo.getCompanyContact());
                            internshipInfo.setCompanyMobile(internshipApplyEditVo.getCompanyMobile());
                            internshipInfoService.update(internshipInfo);
                            ajaxUtil.success().msg("更新成功");

                            InternshipChangeCompanyHistory internshipChangeCompanyHistory = new InternshipChangeCompanyHistory();
                            internshipChangeCompanyHistory.setInternshipChangeCompanyHistoryId(UUIDUtil.getUUID());
                            internshipChangeCompanyHistory.setInternshipReleaseId(internshipInfo.getInternshipReleaseId());
                            internshipChangeCompanyHistory.setStudentId(internshipInfo.getStudentId());
                            internshipChangeCompanyHistory.setChangeTime(DateTimeUtil.getNowLocalDateTime());
                            internshipChangeCompanyHistory.setCompanyName(internshipInfo.getCompanyName());
                            internshipChangeCompanyHistory.setCompanyAddress(internshipInfo.getCompanyAddress());
                            internshipChangeCompanyHistory.setCompanyContacts(internshipInfo.getCompanyContact());
                            internshipChangeCompanyHistory.setCompanyTel(internshipInfo.getCompanyMobile());
                            internshipChangeCompanyHistoryService.save(internshipChangeCompanyHistory);
                        } else if (internshipApply.getInternshipApplyState() == 3 || internshipApply.getInternshipApplyState() == 0) { // 未通过
                            if (internshipApply.getInternshipApplyState() == 3) { // 未通过
                                internshipApply.setInternshipApplyState(1); // 更新为申请中
                                internshipApplyService.update(internshipApply);
                            }

                            internshipInfo.setStudentName(internshipApplyEditVo.getRealName());
                            internshipInfo.setOrganizeName(internshipApplyEditVo.getOrganizeName());
                            internshipInfo.setStudentSex(internshipApplyEditVo.getStudentSex());
                            internshipInfo.setStudentNumber(internshipApplyEditVo.getStudentNumber());
                            internshipInfo.setMobile(internshipApplyEditVo.getMobile());
                            internshipInfo.setQqMailbox(internshipApplyEditVo.getQqMailbox());
                            internshipInfo.setParentContactPhone(internshipApplyEditVo.getParentContactPhone());
                            internshipInfo.setHeadmaster(internshipApplyEditVo.getHeadmaster());
                            internshipInfo.setHeadmasterTel(internshipApplyEditVo.getHeadmasterTel());
                            internshipInfo.setCompanyName(internshipApplyEditVo.getCompanyName());
                            internshipInfo.setCompanyAddress(internshipApplyEditVo.getCompanyAddress());
                            internshipInfo.setCompanyContact(internshipApplyEditVo.getCompanyContact());
                            internshipInfo.setCompanyMobile(internshipApplyEditVo.getCompanyMobile());
                            internshipInfo.setSchoolGuidanceTeacher(internshipApplyEditVo.getSchoolGuidanceTeacher());
                            internshipInfo.setSchoolGuidanceTeacherTel(internshipApplyEditVo.getSchoolGuidanceTeacherTel());
                            internshipInfo.setStartTime(DateTimeUtil.defaultParseLocalDate(internshipApplyEditVo.getStartTime()));
                            internshipInfo.setEndTime(DateTimeUtil.defaultParseLocalDate(internshipApplyEditVo.getEndTime()));
                            internshipInfoService.update(internshipInfo);
                            ajaxUtil.success().msg("更新成功");
                        } else {
                            ajaxUtil.fail().msg("当前状态不允许更新");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到实习数据");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请信息");
                }
            } else {
                ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 撤消状态
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    @PostMapping("/web/internship/apply/recall")
    public ResponseEntity<Map<String, Object>> recall(@RequestParam("id") String internshipReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
        if (optionalStudentBean.isPresent()) {
            StudentBean studentBean = optionalStudentBean.get();
            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
            if (internshipApplyRecord.isPresent()) {
                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                int internshipApplyState = internshipApply.getInternshipApplyState();
                // 处于以下状态不允许撤消 2：已通过；5：基本信息变更填写中；7：单位信息变更填写中
                if (internshipApplyState == 2 || internshipApplyState == 5 ||
                        internshipApplyState == 7) {
                    ajaxUtil.fail().msg("当前状态不允许撤消");
                } else if (internshipApplyState == 1 || internshipApplyState == 0) {
                    // 处于 0：未提交申请 1：申请中 允许撤消 该状态下的撤消将会删除所有相关实习信息
                    internshipInfoService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                    internshipApplyService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                    internshipChangeHistoryService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                    internshipChangeCompanyHistoryService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                    ajaxUtil.success().msg("撤消申请成功");
                } else if (internshipApplyState == 4 || internshipApplyState == 6) {
                    // 处于4：基本信息变更申请中 6：单位信息变更申请中 在这两个状态下将返回已通过状态
                    internshipApply.setInternshipApplyState(2);
                    internshipApplyService.update(internshipApply);
                    ajaxUtil.success().msg("撤消申请成功");

                    InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                    internshipChangeHistory.setState(-1);
                    if (internshipApplyState == 4) {
                        internshipChangeHistory.setReason("撤消基本信息变更申请");
                    }
                    if (internshipApplyState == 6) {
                        internshipChangeHistory.setReason("撤消单位信息变更申请");
                    }
                    internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                    internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
                    internshipChangeHistory.setStudentId(studentBean.getStudentId());
                    internshipChangeHistory.setApplyTime(DateTimeUtil.getNowLocalDateTime());
                    internshipChangeHistoryService.save(internshipChangeHistory);
                } else {
                    ajaxUtil.fail().msg("当前状态异常");
                }
            } else {
                ajaxUtil.fail().msg("未查询到实习申请信息");
            }
        } else {
            ajaxUtil.fail().msg("未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 基础信息变更 单位信息变更申请
     *
     * @param reason              原因
     * @param state               状态
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    @PostMapping("/web/internship/apply/state")
    public ResponseEntity<Map<String, Object>> state(@RequestParam("reason") String reason, @RequestParam("internshipApplyState") int state,
                                                     @RequestParam("internshipReleaseId") String internshipReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
        if (optionalStudentBean.isPresent()) {
            StudentBean studentBean = optionalStudentBean.get();
            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
            if (internshipApplyRecord.isPresent()) {
                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                int internshipApplyState = internshipApply.getInternshipApplyState();
                // 处于 2：已通过 才可变更申请
                if (internshipApplyState == 2) {
                    LocalDateTime now = DateTimeUtil.getNowLocalDateTime();
                    internshipApply.setInternshipApplyState(state);
                    internshipApply.setReason(reason);
                    internshipApply.setApplyTime(now);
                    internshipApplyService.update(internshipApply);
                    ajaxUtil.success().msg("申请成功");

                    InternshipChangeHistory internshipChangeHistory = new InternshipChangeHistory();
                    internshipChangeHistory.setInternshipChangeHistoryId(UUIDUtil.getUUID());
                    internshipChangeHistory.setInternshipReleaseId(internshipReleaseId);
                    internshipChangeHistory.setStudentId(studentBean.getStudentId());
                    internshipChangeHistory.setState(state);
                    internshipChangeHistory.setApplyTime(now);
                    internshipChangeHistory.setReason(reason);
                    internshipChangeHistoryService.save(internshipChangeHistory);

                } else {
                    ajaxUtil.fail().msg("当前状态，无法进行变更申请");
                }
            } else {
                ajaxUtil.fail().msg("未查询到实习申请信息");
            }
        } else {
            ajaxUtil.fail().msg("未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存实习申请资料
     *
     * @param internshipReleaseId 实习发布id
     * @param request             请求
     * @return true or false
     */
    @PostMapping("/web/internship/apply/upload/file")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("internshipReleaseId") String internshipReleaseId,
                                                          MultipartHttpServletRequest request) {
        AjaxUtil<FileBean> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                if (internshipApplyRecord.isPresent()) {
                    InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                    String path = Workbook.internshipFilePath();
                    List<FileBean> fileBeens = uploadService.upload(request,
                            RequestUtil.getRealPath(request) + path, RequestUtil.getIpAddress(request));
                    for (FileBean fileBean : fileBeens) {
                        if (StringUtils.isNotBlank(internshipApply.getFileId())) {
                            Optional<Files> optionalFiles = filesService.findById(internshipApply.getFileId());
                            if (optionalFiles.isPresent()) {
                                Files oldFile = optionalFiles.get();
                                FilesUtil.deleteFile(RequestUtil.getRealPath(request) + oldFile.getRelativePath());
                                filesService.deleteById(oldFile.getFileId());
                            }
                        }
                        String fileId = UUIDUtil.getUUID();
                        Files files = new Files();
                        files.setFileId(fileId);
                        files.setExt(fileBean.getExt());
                        files.setNewName(fileBean.getNewName());
                        files.setOriginalFileName(fileBean.getOriginalFileName());
                        files.setFileSize(fileBean.getFileSize());
                        files.setRelativePath(path + fileBean.getNewName());
                        filesService.save(files);
                        internshipApply.setFileId(fileId);
                        internshipApplyService.update(internshipApply);
                    }
                    ajaxUtil.success().msg("上传成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }
        } catch (Exception e) {
            log.error("Upload file error, error is {}", e);
            ajaxUtil.fail().msg("上传文件失败： " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除电子材料
     *
     * @param internshipReleaseId 实习发布id
     * @param request             请求
     * @return true or false
     */
    @PostMapping("/web/internship/apply/delete/file")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam("id") String internshipReleaseId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
        if (optionalStudentBean.isPresent()) {
            StudentBean studentBean = optionalStudentBean.get();
            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
            if (internshipApplyRecord.isPresent()) {
                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                if (StringUtils.isNotBlank(internshipApply.getFileId())) {
                    String fileId = internshipApply.getFileId();
                    Optional<Files> optionalFiles = filesService.findById(fileId);
                    if (optionalFiles.isPresent()) {
                        Files files = optionalFiles.get();
                        internshipApply.setFileId("");
                        internshipApplyService.update(internshipApply);
                        FilesUtil.deleteFile(RequestUtil.getRealPath(request) + files.getRelativePath());
                        filesService.deleteById(fileId);
                        ajaxUtil.success().msg("删除成功");
                    } else {
                        ajaxUtil.fail().msg("未查询到文件信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到实习申请文件信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到实习申请信息");
            }
        } else {
            ajaxUtil.fail().msg("未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
