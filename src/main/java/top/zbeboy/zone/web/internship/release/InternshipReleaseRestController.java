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
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.ScienceService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.internship.InternshipFileService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipTypeService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.FilesUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.internship.release.InternshipReleaseAddVo;
import top.zbeboy.zone.web.vo.internship.release.InternshipReleaseEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

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
    private UploadService uploadService;

    @Resource
    private FilesService filesService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersTypeService usersTypeService;

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
    @GetMapping("/web/internship/release/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
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
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(canOperator(bean.getInternshipReleaseId()))));
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
                    RequestUtil.getRealPath(request) + path, request.getRemoteAddr());
            fileBeen.forEach(file->file.setRelativePath(path + file.getNewName()));
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
            String teacherDistributionTime = internshipReleaseAddVo.getTeacherDistributionTime();
            String time = internshipReleaseAddVo.getTime();
            String files = internshipReleaseAddVo.getFiles();
            String title = "";
            Users users = usersService.getUserFromSession();

            Optional<Record> scienceRecord = scienceService.findByIdRelation(internshipReleaseAddVo.getScienceId());
            if (scienceRecord.isPresent()) {
                ScienceBean scienceBean = scienceRecord.get().into(ScienceBean.class);
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
            saveOrUpdateTime(internshipRelease, teacherDistributionTime, time);
            internshipRelease.setScienceId(internshipReleaseAddVo.getScienceId());
            internshipRelease.setInternshipReleaseIsDel(ByteUtil.toByte(1).equals(internshipReleaseAddVo.getInternshipReleaseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            internshipRelease.setInternshipTypeId(internshipReleaseAddVo.getInternshipTypeId());
            internshipReleaseService.save(internshipRelease);
            saveOrUpdateFiles(files, internshipReleaseId);
            ajaxUtil.success().msg("保存成功");
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
            if (canOperator(internshipReleaseEditVo.getInternshipReleaseId())) {
                String internshipReleaseId = internshipReleaseEditVo.getInternshipReleaseId();
                String teacherDistributionTime = internshipReleaseEditVo.getTeacherDistributionTime();
                String time = internshipReleaseEditVo.getTime();
                String files = internshipReleaseEditVo.getFiles();
                InternshipRelease internshipRelease = internshipReleaseService.findById(internshipReleaseId);
                if (Objects.nonNull(internshipRelease)) {
                    internshipRelease.setInternshipTitle(internshipReleaseEditVo.getInternshipTitle());
                    saveOrUpdateTime(internshipRelease, teacherDistributionTime, time);
                    internshipRelease.setInternshipReleaseIsDel(ByteUtil.toByte(1).equals(internshipReleaseEditVo.getInternshipReleaseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
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
        if (canOperator(internshipReleaseId)) {
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
        String[] teacherDistributionArr = teacherDistributionTime.split(" 至 ");
        internshipRelease.setTeacherDistributionStartTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionArr[0] + " 00:00:00"));
        internshipRelease.setTeacherDistributionEndTime(DateTimeUtil.defaultParseSqlTimestamp(teacherDistributionArr[1] + " 23:59:59"));

        String[] timeArr = time.split(" 至 ");
        internshipRelease.setStartTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[0] + " 00:00:00"));
        internshipRelease.setEndTime(DateTimeUtil.defaultParseSqlTimestamp(timeArr[1] + " 23:59:59"));
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

    /**
     * 是否可操作
     *
     * @param internshipReleaseId 发布id
     * @return true or false
     */
    private boolean canOperator(String internshipReleaseId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);

                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    int collegeId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            College college = record.get().into(College.class);
                            collegeId = college.getCollegeId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            College college = record.get().into(College.class);
                            collegeId = college.getCollegeId();
                        }
                    }

                    canOperator = bean.getCollegeId() == collegeId;
                }
            }
        } else {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);
                Users users = usersService.getUserFromSession();
                canOperator = StringUtils.equals(bean.getUsername(), users.getUsername());
            }
        }

        return canOperator;
    }
}
