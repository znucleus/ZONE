package top.zbeboy.zone.api.attend.release;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.attend.AttendReleaseService;
import top.zbeboy.zone.service.attend.AttendReleaseSubService;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.attend.AttendReleaseBean;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.attend.release.AttendReleaseAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AttendReleaseApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private AttendReleaseService attendReleaseService;

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    @Resource
    private AttendUsersService attendUsersService;

    @Resource
    private StudentService studentService;

    /**
     * 保存
     *
     * @param attendReleaseAddVo 数据
     * @param bindingResult      校验
     * @param principal          当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AttendReleaseAddVo attendReleaseAddVo, BindingResult bindingResult, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            if (!bindingResult.hasErrors()) {
                // 保存主表
                String attendReleaseId = UUIDUtil.getUUID();
                AttendRelease attendRelease = new AttendRelease();
                attendRelease.setAttendReleaseId(attendReleaseId);
                attendRelease.setUsername(users.getUsername());
                attendRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
                attendRelease.setTitle(attendReleaseAddVo.getTitle());
                attendRelease.setOrganizeId(attendReleaseAddVo.getOrganizeId());
                attendRelease.setIsAuto(ByteUtil.toByte(1).equals(attendReleaseAddVo.getIsAuto()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));

                // 选择自动生成
                if (BooleanUtil.toBoolean(attendReleaseAddVo.getIsAuto())) {
                    // 如果生效时间是未来，这个签到开始，结束时间，日期必须是生效时间日期
                    String attendStartTimeSuffix = attendReleaseAddVo.getAttendStartTime().split(" ")[1];
                    String attendEndTimeSuffix = attendReleaseAddVo.getAttendEndTime().split(" ")[1];
                    String validDatePrefix = attendReleaseAddVo.getValidDate().split(" ")[0];

                    attendRelease.setAttendStartTime(DateTimeUtil.defaultParseSqlTimestamp(validDatePrefix + " " + attendStartTimeSuffix));
                    attendRelease.setAttendEndTime(DateTimeUtil.defaultParseSqlTimestamp(validDatePrefix + " " + attendEndTimeSuffix));

                    attendRelease.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getValidDate()));
                    attendRelease.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getExpireDate()));
                } else {
                    // 非自动生成，立即生效且失效
                    attendRelease.setValidDate(DateTimeUtil.getNowSqlTimestamp());
                    attendRelease.setExpireDate(DateTimeUtil.getNowSqlTimestamp());
                    attendRelease.setAttendStartTime(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getAttendStartTime()));
                    attendRelease.setAttendEndTime(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getAttendEndTime()));
                }

                attendReleaseService.save(attendRelease);

                // 操作子表，直接复制主表
                attendReleaseSubService.copyAttendRelease(attendReleaseId);

                // 生成名单
                Result<Record> studentRecords = studentService.findByOrganizeId(attendReleaseAddVo.getOrganizeId());
                if (studentRecords.isNotEmpty()) {
                    List<Student> students = studentRecords.into(Student.class);
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

                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @GetMapping("/api/attend/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        AjaxUtil<AttendReleaseBean> ajaxUtil = AjaxUtil.of();
        simplePaginationUtil.setPrincipal(principal);
        List<AttendReleaseBean> beans = new ArrayList<>();
        Result<Record> records = attendReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(AttendReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setAttendStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getAttendStartTime())));
            beans.forEach(bean -> bean.setAttendEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getAttendEndTime())));
        }
        simplePaginationUtil.setTotalSize(attendReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @GetMapping("/api/attend/sub/data")
    public ResponseEntity<Map<String, Object>> subData(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        AjaxUtil<AttendReleaseSubBean> ajaxUtil = AjaxUtil.of();
        simplePaginationUtil.setPrincipal(principal);
        List<AttendReleaseSubBean> beans = new ArrayList<>();
        Result<Record> records = attendReleaseSubService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(AttendReleaseSubBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setAttendStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getAttendStartTime())));
            beans.forEach(bean -> bean.setAttendEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getAttendEndTime())));
        }
        simplePaginationUtil.setTotalSize(attendReleaseSubService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
