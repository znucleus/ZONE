package top.zbeboy.zone.api.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.AttendWxStudentSubscribeRecord;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.attend.AttendDataService;
import top.zbeboy.zone.service.attend.AttendReleaseSubService;
import top.zbeboy.zone.service.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zone.service.cache.attend.AttendWxCacheService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;

@RestController
public class AttendReleaseSubApiController {

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    @Resource
    private AttendDataService attendDataService;

    @Resource
    private AttendWxStudentSubscribeService attendWxStudentSubscribeService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleService roleService;

    @Resource
    private AttendWxCacheService attendWxCacheService;

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
            beans.forEach(bean -> bean.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getValidDate())));
            beans.forEach(bean -> bean.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getExpireDate())));

            Users users = usersService.getUserFromOauth(principal);
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
                    if (studentRecord.isPresent()) {
                        Student student = studentRecord.get().into(Student.class);
                        for (AttendReleaseSubBean bean : beans) {
                            Optional<Record> record = attendDataService
                                    .findByStudentIdAndAttendReleaseIdAndAttendReleaseSubId(student.getStudentId(), bean.getAttendReleaseId(), bean.getAttendReleaseSubId());
                            bean.setIsAttend(BooleanUtil.toByte(record.isPresent()));

                            Optional<AttendWxStudentSubscribeRecord> subRecord = attendWxStudentSubscribeService.findByAttendReleaseIdAndStudentId(
                                    bean.getAttendReleaseId(), studentRecord.get().getStudentId());
                            bean.setIsSubscribe(BooleanUtil.toByte(subRecord.isPresent()));
                        }
                    }

                }
            }
        }
        simplePaginationUtil.setTotalSize(attendReleaseSubService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 通过id查询子表数据
     *
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    @GetMapping("/api/attend/sub/query/{id}")
    public ResponseEntity<Map<String, Object>> subQuery(@PathVariable("id") int attendReleaseSubId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<Record> record = attendReleaseSubService.findByIdRelation(attendReleaseSubId);
        if (record.isPresent()) {
            AttendReleaseSubBean attendReleaseSub = record.get().into(AttendReleaseSubBean.class);
            attendReleaseSub.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(attendReleaseSub.getReleaseTime()));
            attendReleaseSub.setAttendStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(attendReleaseSub.getAttendStartTime()));
            attendReleaseSub.setAttendEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(attendReleaseSub.getAttendEndTime()));
            attendReleaseSub.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(attendReleaseSub.getValidDate()));
            attendReleaseSub.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(attendReleaseSub.getExpireDate()));
            ajaxUtil.success().msg("查询数据成功").put("attendReleaseSub", attendReleaseSub);
        } else {
            ajaxUtil.fail().msg("根据ID未查询到签到发布子表数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据id删除子表数据
     *
     * @param attendReleaseSubId 子表数据
     * @return 数据
     */
    @PostMapping("/api/attend/sub/delete")
    public ResponseEntity<Map<String, Object>> subDelete(@RequestParam("id") int attendReleaseSubId, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AttendReleaseSub attendReleaseSub = attendReleaseSubService.findById(attendReleaseSubId);
        if (Objects.nonNull(attendReleaseSub)) {
            Users users = usersService.getUserFromOauth(principal);
            if (roleService.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                    (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendReleaseSub.getUsername()))) {
                attendReleaseSubService.deleteById(attendReleaseSubId);

                // 删除当天订阅下发
                attendWxCacheService.deleteAttendWxSubscribe(attendReleaseSub.getAttendReleaseId());
                ajaxUtil.success().msg("删除数据成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("根据ID未查询到签到发布子表数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
