package top.zbeboy.zone.web.internship.release;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zbase.bean.data.science.ScienceBean;
import top.zbeboy.zbase.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.ScienceService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.ByteUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.internship.release.InternshipReleaseAddVo;
import top.zbeboy.zbase.vo.internship.release.InternshipReleaseEditVo;
import top.zbeboy.zone.service.internship.InternshipFileService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipTypeService;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class InternshipReleaseRestController {

    private final Logger log = LoggerFactory.getLogger(InternshipReleaseRestController.class);

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipFileService internshipFileService;

    @Resource
    private InternshipTypeService internshipTypeService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private UploadService uploadService;

    @Resource
    private FilesService filesService;

    @Resource
    private ScienceService scienceService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/release/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> {
                if (BooleanUtil.toBoolean(bean.getIsTimeLimit())) {
                    bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime()));
                    bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime()));
                    bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime()));
                    bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime()));
                }
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime()));
                bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.canOperator(bean.getInternshipReleaseId())));
            });
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 上传实习文件
     *
     * @param request 数据
     * @return true or false
     */
    @PostMapping("/web/internship/release/upload/file")
    public ResponseEntity<Map<String, Object>> uploadFile(MultipartHttpServletRequest request) {
        AjaxUtil<FileBean> ajaxUtil = AjaxUtil.of();
        try {
            String path = Workbook.internshipFilePath();
            List<FileBean> fileBeen = uploadService.upload(request,
                    RequestUtil.getRealPath(request) + path, RequestUtil.getIpAddress(request));
            fileBeen.forEach(file -> file.setRelativePath(path + file.getNewName()));
            ajaxUtil.success().list(fileBeen).msg("上传成功");
        } catch (Exception e) {
            log.error("Upload file exception,is {}", e);
            ajaxUtil.fail().msg("上传文件失败： " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除实习附件
     *
     * @param filePath            路径
     * @param fileId              id
     * @param internshipReleaseId 发布id
     * @param request             数据
     * @return true or false
     */
    @PostMapping("/web/internship/release/delete/file")
    public ResponseEntity<Map<String, Object>> deleteFile(String filePath, String fileId, String internshipReleaseId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(internshipReleaseId)) {
            internshipFileService.deleteByFileIdAndInternshipReleaseId(fileId, internshipReleaseId);
        }

        if (StringUtils.isNotBlank(fileId)) {
            filesService.deleteById(fileId);
        }

        if (StringUtils.isNotBlank(filePath)) {
            FilesUtil.deleteFile(RequestUtil.getRealPath(request) + filePath);
        }
        ajaxUtil.success().msg("删除成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param internshipReleaseAddVo 实习
     * @param bindingResult          检验
     * @return true or false
     */
    @PostMapping("/web/internship/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid InternshipReleaseAddVo internshipReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            String internshipReleaseId = UUIDUtil.getUUID();
            boolean isTimeLimit = BooleanUtil.toBoolean(internshipReleaseAddVo.getIsTimeLimit());
            String teacherDistributionTime = internshipReleaseAddVo.getTeacherDistributionTime();
            String time = internshipReleaseAddVo.getTime();
            String files = internshipReleaseAddVo.getFiles();
            String title = "";
            Users users = SessionUtil.getUserFromSession();

            ScienceBean scienceBean = scienceService.findByIdRelation(internshipReleaseAddVo.getScienceId());
            if (Objects.nonNull(scienceBean.getScienceId()) && scienceBean.getScienceId() > 0) {
                title = scienceBean.getSchoolName()
                        + "-" + scienceBean.getCollegeName()
                        + "-" + scienceBean.getDepartmentName()
                        + "-" + scienceBean.getScienceName()
                        + " " + DateTimeUtil.getNowYear() + "年";
                InternshipType internshipType = internshipTypeService.findById(internshipReleaseAddVo.getInternshipTypeId());
                if (Objects.nonNull(internshipType)) {
                    title += internshipType.getInternshipTypeName();
                }
            }

            InternshipRelease internshipRelease = new InternshipRelease();
            internshipRelease.setInternshipReleaseId(internshipReleaseId);
            internshipRelease.setInternshipTitle(title);
            internshipRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
            internshipRelease.setUsername(users.getUsername());
            internshipRelease.setPublisher(users.getRealName());
            internshipRelease.setScienceId(internshipReleaseAddVo.getScienceId());
            internshipRelease.setInternshipReleaseIsDel(ByteUtil.toByte(1).equals(internshipReleaseAddVo.getInternshipReleaseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            internshipRelease.setInternshipTypeId(internshipReleaseAddVo.getInternshipTypeId());
            internshipRelease.setIsTimeLimit(internshipReleaseAddVo.getIsTimeLimit());

            if (isTimeLimit) {
                if (StringUtils.isNotBlank(teacherDistributionTime) && StringUtils.isNotBlank(time)) {
                    // 需要时间限制
                    saveOrUpdateTime(internshipRelease, teacherDistributionTime, time);
                    internshipReleaseService.save(internshipRelease);
                    saveOrUpdateFiles(files, internshipReleaseId);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("指导教师分配时间与实习申请时间不能为空");
                }
            } else {
                internshipReleaseService.save(internshipRelease);
                saveOrUpdateFiles(files, internshipReleaseId);
                ajaxUtil.success().msg("保存成功");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param internshipReleaseEditVo 实习
     * @param bindingResult           检验
     * @return true or false
     */
    @PostMapping("/web/internship/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid InternshipReleaseEditVo internshipReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (internshipConditionCommon.canOperator(internshipReleaseEditVo.getInternshipReleaseId())) {
                String internshipReleaseId = internshipReleaseEditVo.getInternshipReleaseId();
                boolean isTimeLimit = BooleanUtil.toBoolean(internshipReleaseEditVo.getIsTimeLimit());
                String teacherDistributionTime = internshipReleaseEditVo.getTeacherDistributionTime();
                String time = internshipReleaseEditVo.getTime();
                String files = internshipReleaseEditVo.getFiles();
                InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
                if (Objects.nonNull(internshipRelease)) {
                    internshipRelease.setInternshipTitle(internshipReleaseEditVo.getInternshipTitle());
                    internshipRelease.setIsTimeLimit(internshipReleaseEditVo.getIsTimeLimit());
                    internshipRelease.setInternshipReleaseIsDel(ByteUtil.toByte(1).equals(internshipReleaseEditVo.getInternshipReleaseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                    if (isTimeLimit) {
                        if (StringUtils.isNotBlank(teacherDistributionTime) && StringUtils.isNotBlank(time)) {
                            // 需要时间限制
                            saveOrUpdateTime(internshipRelease, teacherDistributionTime, time);
                            internshipReleaseService.update(internshipRelease);
                            Result<Record> records = internshipFileService.findByInternshipReleaseId(internshipReleaseId);
                            if (records.isNotEmpty()) {
                                internshipFileService.deleteByInternshipReleaseId(internshipReleaseId);
                                List<InternshipFile> internshipFiles = records.into(InternshipFile.class);
                                internshipFiles.forEach(file -> filesService.deleteById(file.getFileId()));
                            }
                            saveOrUpdateFiles(files, internshipReleaseId);
                            ajaxUtil.success().msg("更新成功");
                        } else {
                            ajaxUtil.fail().msg("指导教师分配时间与实习申请时间不能为空");
                        }
                    } else {
                        internshipRelease.setTeacherDistributionStartTime(null);
                        internshipRelease.setTeacherDistributionEndTime(null);
                        internshipRelease.setStartTime(null);
                        internshipRelease.setEndTime(null);
                        internshipReleaseService.update(internshipRelease);
                        Result<Record> records = internshipFileService.findByInternshipReleaseId(internshipReleaseId);
                        if (records.isNotEmpty()) {
                            internshipFileService.deleteByInternshipReleaseId(internshipReleaseId);
                            List<InternshipFile> internshipFiles = records.into(InternshipFile.class);
                            internshipFiles.forEach(file -> filesService.deleteById(file.getFileId()));
                        }
                        saveOrUpdateFiles(files, internshipReleaseId);
                        ajaxUtil.success().msg("更新成功");
                    }
                } else {
                    ajaxUtil.fail().msg("根据实习发布ID未查询到实习发布数据");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新实习发布状态
     *
     * @param internshipReleaseId 实习发布id
     * @param isDel               注销参数
     * @return true or false
     */
    @PostMapping("/web/internship/release/status")
    public ResponseEntity<Map<String, Object>> status(@RequestParam("internshipReleaseId") String internshipReleaseId, @RequestParam("isDel") Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.canOperator(internshipReleaseId)) {
            InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
            if (Objects.nonNull(internshipRelease)) {
                internshipRelease.setInternshipReleaseIsDel(isDel);
                internshipReleaseService.update(internshipRelease);
                ajaxUtil.success().msg("更新状态成功");
            } else {
                ajaxUtil.fail().msg("根据实习发布ID未查询到实习发布数据");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新或保存时间
     *
     * @param internshipRelease       实习
     * @param teacherDistributionTime 教师分配时间
     * @param time                    申请时间
     */
    private void saveOrUpdateTime(InternshipRelease internshipRelease, String teacherDistributionTime, String time) {
        if (teacherDistributionTime.contains("至")) {
            String[] teacherDistributionArr = teacherDistributionTime.split(" 至 ");
            internshipRelease.setTeacherDistributionStartTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionArr[0] + " 00:00:00"));
            internshipRelease.setTeacherDistributionEndTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionArr[1] + " 23:59:59"));
        } else {
            internshipRelease.setTeacherDistributionStartTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionTime + " 00:00:00"));
            internshipRelease.setTeacherDistributionEndTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionTime + " 23:59:59"));
        }

        if (time.contains("至")) {
            String[] timeArr = time.split(" 至 ");
            internshipRelease.setStartTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[0] + " 00:00:00"));
            internshipRelease.setEndTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[1] + " 23:59:59"));
        } else {
            internshipRelease.setStartTime(DateTimeUtil.defaultParseSqlTimestamp(time + " 00:00:00"));
            internshipRelease.setEndTime(DateTimeUtil.defaultParseSqlTimestamp(time + " 23:59:59"));
        }

    }

    /**
     * 更新或保存文件
     *
     * @param files               文件json
     * @param internshipReleaseId 实习id
     */
    private void saveOrUpdateFiles(String files, String internshipReleaseId) {
        if (StringUtils.isNotBlank(files)) {
            List<Files> filesList = JSON.parseArray(files, Files.class);
            for (Files f : filesList) {
                f.setFileId(UUIDUtil.getUUID());
                filesService.save(f);
                InternshipFile internshipFile = new InternshipFile(internshipReleaseId, f.getFileId());
                internshipFileService.save(internshipFile);
            }
        }
    }
}
