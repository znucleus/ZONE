package top.zbeboy.zone.api.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Record11;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;
import top.zbeboy.zone.service.attend.AttendReleaseService;
import top.zbeboy.zone.service.attend.AttendReleaseSubService;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.attend.AttendUsersBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.users.AttendUsersAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class AttendUsersApiController {

    @Resource
    private AttendUsersService attendUsersService;

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    @Resource
    private AttendReleaseService attendReleaseService;

    @Resource
    private StudentService studentService;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    /**
     * 获取签到名单数据
     *
     * @param attendReleaseSubId 签到子表ID
     * @param type               数据类型 1:已签到 2:未签到 0:全部
     * @return 数据
     */
    @GetMapping("/api/attend/users/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("attendReleaseSubId") int attendReleaseSubId, int type) {
        AjaxUtil<AttendUsersBean> ajaxUtil = AjaxUtil.of();
        // 根据子表id查询主表id
        AttendReleaseSub attendReleaseSub = attendReleaseSubService.findById(attendReleaseSubId);
        if (Objects.nonNull(attendReleaseSub)) {
            List<AttendUsersBean> attendUsers = new ArrayList<>();
            // 已签到数据
            if (type == 1) {
                Result<Record> records = attendUsersService.findHasAttendedStudent(attendReleaseSub.getAttendReleaseId(), attendReleaseSubId);
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsersBean.class);
                }
            } else if (type == 2) {
                // 未签到数据
                Result<Record> records = attendUsersService.findNotAttendedStudent(attendReleaseSub.getAttendReleaseId(), attendReleaseSubId);
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsersBean.class);
                }
            } else if(type == 3){
                // 统计中数据
                Result<Record11<String, String, Timestamp, String, Integer, String, String, String, String, Timestamp, String>> records = attendUsersService.findByAttendReleaseIdAndAttendReleaseSubId(attendReleaseSub.getAttendReleaseId(), attendReleaseSubId);
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsersBean.class);
                }
            } else {
                Result<Record> records = attendUsersService.findByAttendReleaseIdRelation(attendReleaseSub.getAttendReleaseId());
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsersBean.class);
                }
            }

            ajaxUtil.success().list(attendUsers).msg("获取数据成功");
        } else {
            ajaxUtil.fail().msg("根据ID未查询到签到发布子表数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param attendUsersAddVo 数据
     * @param bindingResult    校验
     * @return true or false
     */
    @PostMapping("/api/attend/users/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AttendUsersAddVo attendUsersAddVo, BindingResult bindingResult, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Student student = studentService.findByStudentNumber(attendUsersAddVo.getStudentNumber());
            if (Objects.nonNull(student)) {
                AttendRelease attendRelease = attendReleaseService.findById(attendUsersAddVo.getAttendReleaseId());
                if (Objects.nonNull(attendRelease)) {
                    Users users = usersService.getUserFromOauth(principal);
                    if (roleService.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                            (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendRelease.getUsername()))) {
                        if (Objects.equals(student.getOrganizeId(), attendRelease.getOrganizeId())) {
                            Optional<AttendUsersRecord> attendUsersRecord = attendUsersService
                                    .findByAttendReleaseIdAndStudentId(attendUsersAddVo.getAttendReleaseId(), student.getStudentId());
                            if (!attendUsersRecord.isPresent()) {
                                AttendUsers attendUsers = new AttendUsers();
                                attendUsers.setAttendUsersId(UUIDUtil.getUUID());
                                attendUsers.setAttendReleaseId(attendUsersAddVo.getAttendReleaseId());
                                attendUsers.setStudentId(student.getStudentId());
                                attendUsers.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                                attendUsersService.save(attendUsers);

                                ajaxUtil.success().msg("保存成功");
                            } else {
                                ajaxUtil.fail().msg("学生已在名单中");
                            }
                        } else {
                            ajaxUtil.fail().msg("当前学生不属于发布所属班级");
                        }
                    } else {
                        ajaxUtil.fail().msg("您无权限操作");
                    }
                } else {
                    ajaxUtil.fail().msg("根据签到主表ID未查询到发布信息");
                }
            } else {
                ajaxUtil.fail().msg("根据学号未查询到学生信息");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除人员
     *
     * @param attendUsersId 名单ID
     * @return true or false
     */
    @PostMapping("/api/attend/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("attendUsersId") String attendUsersId, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AttendUsers attendUsers = attendUsersService.findById(attendUsersId);
        if (Objects.nonNull(attendUsers)) {
            AttendRelease attendRelease = attendReleaseService.findById(attendUsers.getAttendReleaseId());
            if (Objects.nonNull(attendRelease)) {
                Users users = usersService.getUserFromOauth(principal);
                if (roleService.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                        (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendRelease.getUsername()))) {
                    attendUsersService.deleteById(attendUsersId);
                    ajaxUtil.success().msg("删除成功");
                } else {
                    ajaxUtil.fail().msg("您无权限操作");
                }
            } else {
                ajaxUtil.fail().msg("根据签到主表ID未查询到发布信息");
            }
        } else {
            ajaxUtil.fail().msg("根据ID未查询到名单数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 名单重置
     *
     * @param attendReleaseId 发布表ID
     * @return true or false
     */
    @PostMapping("/api/attend/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("attendReleaseId") String attendReleaseId, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AttendRelease attendRelease = attendReleaseService.findById(attendReleaseId);
        if (Objects.nonNull(attendRelease)) {
            Users users = usersService.getUserFromOauth(principal);
            if (roleService.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                    (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendRelease.getUsername()))) {
                Result<Record> records = attendUsersService.findStudentNotExistsAttendUsers(attendRelease.getAttendReleaseId(), attendRelease.getOrganizeId());
                if (records.isNotEmpty()) {
                    List<Student> students = records.into(Student.class);
                    List<AttendUsers> attendUsers = new ArrayList<>();
                    for (Student student : students) {
                        AttendUsers au = new AttendUsers();
                        au.setAttendUsersId(UUIDUtil.getUUID());
                        au.setAttendReleaseId(attendReleaseId);
                        au.setStudentId(student.getStudentId());
                        au.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                        attendUsers.add(au);
                    }
                    attendUsersService.batchSave(attendUsers);
                }

                ajaxUtil.success().msg("重置成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("根据发布表ID未查询到发布数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param attendUsersId 名单ID
     * @param remark        备注
     * @return true or false
     */
    @PostMapping("/api/attend/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("attendUsersId") String attendUsersId, String remark, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AttendUsers attendUsers = attendUsersService.findById(attendUsersId);
        if (Objects.nonNull(attendUsers)) {
            AttendRelease attendRelease = attendReleaseService.findById(attendUsers.getAttendReleaseId());
            if (Objects.nonNull(attendRelease)) {
                Users users = usersService.getUserFromOauth(principal);
                if (roleService.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                        (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendRelease.getUsername()))) {
                    attendUsers.setRemark(remark);
                    attendUsersService.update(attendUsers);
                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("您无权限操作");
                }
            } else {
                ajaxUtil.fail().msg("根据签到主表ID未查询到发布信息");
            }
        } else {
            ajaxUtil.fail().msg("根据名单ID未查询到名单数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
