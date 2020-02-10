package top.zbeboy.zone.web.internship.apply;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.internship.InternshipInfoService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
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
}
