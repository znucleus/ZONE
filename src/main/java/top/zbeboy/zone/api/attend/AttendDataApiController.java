package top.zbeboy.zone.api.attend;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;
import top.zbeboy.zone.domain.tables.records.WeiXinDeviceRecord;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.attend.AttendDataService;
import top.zbeboy.zone.service.attend.AttendReleaseSubService;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.service.data.WeiXinDeviceService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.vo.attend.data.AttendDataAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AttendDataApiController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    @Resource
    private AttendUsersService attendUsersService;

    @Resource
    private AttendDataService attendDataService;

    @Resource
    private WeiXinDeviceService weiXinDeviceService;

    @Resource
    private StudentService studentService;

    /**
     * 保存
     *
     * @param attendDataAddVo 数据
     * @param bindingResult   校验
     * @param principal       当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/data/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AttendDataAddVo attendDataAddVo, BindingResult bindingResult, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            if (!bindingResult.hasErrors()) {
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        // 根据子表id查询主表id
                        AttendReleaseSub attendReleaseSub = attendReleaseSubService.findById(attendDataAddVo.getAttendReleaseSubId());
                        if (Objects.nonNull(attendReleaseSub)) {
                            // 判断签到时间
                            if (DateTimeUtil.nowBeforeSqlTimestamp(attendReleaseSub.getAttendEndTime()) &&
                                    DateTimeUtil.nowAfterSqlTimestamp(attendReleaseSub.getAttendStartTime())) {
                                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                                if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                                    Optional<AttendUsersRecord> attendUsersRecord = attendUsersService
                                            .findByAttendReleaseIdAndStudentId(attendReleaseSub.getAttendReleaseId(), studentBean.getStudentId());
                                    if (attendUsersRecord.isPresent()) {
                                        AttendUsers attendUsers = attendUsersRecord.get().into(AttendUsers.class);
                                        Optional<AttendDataRecord> attendDataRecord = attendDataService.findByAttendUsersIdAndAttendReleaseSubId(
                                                attendUsers.getAttendUsersId(), attendDataAddVo.getAttendReleaseSubId()
                                        );
                                        if (!attendDataRecord.isPresent()) {
                                            AttendData attendData = new AttendData();
                                            attendData.setAttendUsersId(attendUsers.getAttendUsersId());
                                            attendData.setAttendReleaseSubId(attendDataAddVo.getAttendReleaseSubId());
                                            attendData.setLocation(attendDataAddVo.getLocation());
                                            attendData.setAddress(attendDataAddVo.getAddress());
                                            attendData.setAttendDate(DateTimeUtil.getNowSqlTimestamp());

                                            // 机型处理
                                            Optional<WeiXinDeviceRecord> weiXinDeviceRecord = weiXinDeviceService.findByUsername(users.getUsername());
                                            if (weiXinDeviceRecord.isPresent()) {
                                                WeiXinDevice weiXinDevice = weiXinDeviceRecord.get().into(WeiXinDevice.class);
                                                if (!StringUtils.equals(weiXinDevice.getModel(), attendDataAddVo.getModel()) ||
                                                        Objects.isNull(weiXinDevice.getScreenWidth()) ||
                                                        Objects.isNull(attendDataAddVo.getScreenWidth()) ||
                                                        Math.abs(weiXinDevice.getScreenWidth() - attendDataAddVo.getScreenWidth()) > 30 ||
                                                        Objects.isNull(weiXinDevice.getScreenHeight()) ||
                                                        Objects.isNull(attendDataAddVo.getScreenHeight()) ||
                                                        Math.abs(weiXinDevice.getScreenHeight() - attendDataAddVo.getScreenHeight()) > 30) {
                                                    attendData.setDeviceSame(BooleanUtil.toByte(false));
                                                } else {
                                                    attendData.setDeviceSame(BooleanUtil.toByte(true));
                                                }

                                                weiXinDevice.setBrand(attendDataAddVo.getBrand());
                                                weiXinDevice.setModel(attendDataAddVo.getModel());
                                                weiXinDevice.setVersion(attendDataAddVo.getVersion());
                                                weiXinDevice.setScreenWidth(attendDataAddVo.getScreenWidth());
                                                weiXinDevice.setScreenHeight(attendDataAddVo.getScreenHeight());
                                                weiXinDevice.setSystemInfo(attendDataAddVo.getSystemInfo());
                                                weiXinDevice.setPlatform(attendDataAddVo.getPlatform());
                                                weiXinDevice.setLocationAuthorized(attendDataAddVo.getLocationAuthorized());
                                                weiXinDevice.setNotificationAuthorized(attendDataAddVo.getNotificationAuthorized());
                                                weiXinDevice.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                                                weiXinDeviceService.update(weiXinDevice);
                                            } else {
                                                WeiXinDevice weiXinDevice = new WeiXinDevice();
                                                weiXinDevice.setDeviceId(UUIDUtil.getUUID());
                                                weiXinDevice.setBrand(attendDataAddVo.getBrand());
                                                weiXinDevice.setModel(attendDataAddVo.getModel());
                                                weiXinDevice.setVersion(attendDataAddVo.getVersion());
                                                weiXinDevice.setScreenWidth(attendDataAddVo.getScreenWidth());
                                                weiXinDevice.setScreenHeight(attendDataAddVo.getScreenHeight());
                                                weiXinDevice.setSystemInfo(attendDataAddVo.getSystemInfo());
                                                weiXinDevice.setPlatform(attendDataAddVo.getPlatform());
                                                weiXinDevice.setLocationAuthorized(attendDataAddVo.getLocationAuthorized());
                                                weiXinDevice.setNotificationAuthorized(attendDataAddVo.getNotificationAuthorized());
                                                weiXinDevice.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                                                weiXinDevice.setUsername(users.getUsername());

                                                weiXinDeviceService.save(weiXinDevice);

                                                attendData.setDeviceSame(BooleanUtil.toByte(true));
                                            }

                                            attendDataService.save(attendData);

                                            ajaxUtil.success().msg("签到成功");
                                        } else {
                                            ajaxUtil.fail().msg("已签到");
                                        }
                                    } else {
                                        ajaxUtil.fail().msg("名单中未查询到当前学生信息");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("未查询到学生信息");
                                }
                            } else {
                                ajaxUtil.fail().msg("当前时间不在签到时间范围内");
                            }
                        } else {
                            ajaxUtil.fail().msg("根据ID未查询到签到发布子表数据");
                        }
                    } else {
                        ajaxUtil.fail().msg("仅支持学生类型用户操作");
                    }
                } else {
                    ajaxUtil.fail().msg("根据用户信息未查询到用户类型");
                }
            } else {
                ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除签到记录
     *
     * @param attendReleaseSubId 子表ID
     * @param attendUsersId      名单ID
     * @param principal          当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/data/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("attendReleaseSubId") int attendReleaseSubId,
                                                      @RequestParam("attendUsersId") String attendUsersId, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AttendReleaseSub attendReleaseSub = attendReleaseSubService.findById(attendReleaseSubId);
        if (Objects.nonNull(attendReleaseSub)) {
            Users users = SessionUtil.getUserFromOauth(principal);
            if (SessionUtil.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal) ||
                    (Objects.nonNull(users) && StringUtils.equals(users.getUsername(), attendReleaseSub.getUsername()))) {
                attendDataService.deleteByAttendUsersIdAndAttendReleaseSubId(attendUsersId, attendReleaseSubId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("根据签到子表ID未查询到发布信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
