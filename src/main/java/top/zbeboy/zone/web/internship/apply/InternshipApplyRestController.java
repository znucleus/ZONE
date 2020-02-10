package top.zbeboy.zone.web.internship.apply;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.InternshipApply;
import top.zbeboy.zone.domain.tables.pojos.InternshipChangeCompanyHistory;
import top.zbeboy.zone.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.internship.InternshipChangeCompanyHistoryService;
import top.zbeboy.zone.service.internship.InternshipInfoService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.internship.apply.InternshipApplyBean;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyAddVo;
import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@RestController
public class InternshipApplyRestController {

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
    private FilesService filesService;

    @Resource
    private UploadService uploadService;

    @Resource
    private StaffService staffService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/apply/internship/data")
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
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.applyCondition(bean.getInternshipReleaseId()))));
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
    @GetMapping("/web/internship/apply/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipApplyBean> ajaxUtil = AjaxUtil.of();
        List<InternshipApplyBean> beans = new ArrayList<>();
        Result<Record> records = internshipApplyService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipApplyBean.class);
            beans.forEach(bean -> bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime())));
            beans.forEach(bean -> bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime())));
            beans.forEach(bean -> bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime())));
            beans.forEach(bean -> bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime())));
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setApplyTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getApplyTime())));
            beans.forEach(bean -> bean.setChangeFillStartTimeStr(Objects.nonNull(bean.getChangeFillStartTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillStartTime()) : ""));
            beans.forEach(bean -> bean.setChangeFillEndTimeStr(Objects.nonNull(bean.getChangeFillEndTime()) ? DateTimeUtil.defaultFormatSqlTimestamp(bean.getChangeFillEndTime()) : ""));
            beans.forEach(bean -> bean.setCanEdit(BooleanUtil.toByte(internshipConditionCommon.applyEditCondition(bean.getInternshipReleaseId()))));
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
        Files files = filesService.findById(id);
        if (Objects.nonNull(id)) {
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
                Optional<Record> staffRecord = staffService.findByIdRelation(internshipApplyAddVo.getStaffId());
                if (staffRecord.isPresent()) {
                    StaffBean staffBean = staffRecord.get().into(StaffBean.class);
                    internshipApplyAddVo.setHeadmaster(staffBean.getRealName());
                    internshipApplyAddVo.setHeadmasterTel(staffBean.getMobile());

                    String[] schoolGuidanceTeacherArr = internshipApplyAddVo.getSchoolGuidanceTeacher().split(" ");
                    if (schoolGuidanceTeacherArr.length > 1) {
                        internshipApplyAddVo.setSchoolGuidanceTeacher(schoolGuidanceTeacherArr[0]);
                        internshipApplyAddVo.setSchoolGuidanceTeacherTel(schoolGuidanceTeacherArr[1]);
                    }

                    internshipInfoService.saveWithTransaction(internshipApplyAddVo);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到班级任信息");
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
                            Optional<Record> staffRecord = staffService.findByIdRelation(internshipApplyEditVo.getStaffId());
                            if (staffRecord.isPresent()) {
                                StaffBean staffBean = staffRecord.get().into(StaffBean.class);
                                internshipApplyEditVo.setHeadmaster(staffBean.getRealName());
                                internshipApplyEditVo.setHeadmasterTel(staffBean.getMobile());

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
                            internshipInfo.setStartTime(DateTimeUtil.defaultParseSqlDate(internshipApplyEditVo.getStartTime()));
                            internshipInfo.setEndTime(DateTimeUtil.defaultParseSqlDate(internshipApplyEditVo.getEndTime()));

                            internshipInfoService.update(internshipInfo);
                            ajaxUtil.success().msg("更新成功");
                        } else if (internshipApply.getInternshipApplyState() == 7) {// 7：单位信息变更填写中
                            Result<Record> internshipChangeCompanyHistoryRecord = internshipChangeCompanyHistoryService.findByInternshipReleaseIdAndStudentId(internshipApplyEditVo.getInternshipReleaseId(), internshipApplyEditVo.getStudentId());
                            if (internshipChangeCompanyHistoryRecord.isEmpty()) {
                                InternshipChangeCompanyHistory internshipChangeCompanyHistory = new InternshipChangeCompanyHistory();
                                internshipChangeCompanyHistory.setInternshipChangeCompanyHistoryId(UUIDUtil.getUUID());
                                internshipChangeCompanyHistory.setInternshipReleaseId(internshipInfo.getInternshipReleaseId());
                                internshipChangeCompanyHistory.setStudentId(internshipInfo.getStudentId());
                                internshipChangeCompanyHistory.setChangeTime(DateTimeUtil.getNowSqlTimestamp());
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
                            internshipChangeCompanyHistory.setChangeTime(DateTimeUtil.getNowSqlTimestamp());
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
                            internshipInfo.setStartTime(DateTimeUtil.defaultParseSqlDate(internshipApplyEditVo.getStartTime()));
                            internshipInfo.setEndTime(DateTimeUtil.defaultParseSqlDate(internshipApplyEditVo.getEndTime()));
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
}
