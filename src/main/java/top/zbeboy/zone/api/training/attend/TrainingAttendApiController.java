package top.zbeboy.zone.api.training.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.schoolroom.SchoolroomBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.training.attend.TrainingAttendBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.config.WeiXinAppBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttend;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttendUsers;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.domain.tables.records.TrainingAttendUsersRecord;
import top.zbeboy.zbase.domain.tables.records.TrainingUsersRecord;
import top.zbeboy.zbase.feign.data.SchoolroomService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.data.WeiXinService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.MapUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.training.TrainingAttendService;
import top.zbeboy.zone.service.training.TrainingAttendUsersService;
import top.zbeboy.zone.service.training.TrainingUsersService;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class TrainingAttendApiController {

    @Resource
    private TrainingControllerCommon trainingControllerCommon;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingAttendService trainingAttendService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingAttendUsersService trainingAttendUsersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private SchoolroomService schoolroomService;

    @Resource
    private WeiXinService weiXinService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "实训考勤发布数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/training/attend/training/paging")
    public ResponseEntity<Map<String, Object>> trainingData(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<TrainingReleaseBean> ajaxUtil = trainingControllerCommon.trainingData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "实训考勤列表数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/training/attend/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        AjaxUtil<TrainingAttendBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        List<TrainingAttendBean> beans = new ArrayList<>();
        Result<Record> records = trainingAttendService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingAttendBean.class);
            for (TrainingAttendBean bean : beans) {
                bean.setPublishDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getPublishDate()));
                bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())));

                Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (optionalStudentBean.isPresent()) {
                            int studentId = optionalStudentBean.get().getStudentId();
                            Optional<TrainingUsersRecord> optionalTrainingUsersRecord = trainingUsersService.findByTrainingReleaseIdAndStudentId(bean.getTrainingReleaseId(), studentId);
                            if (optionalTrainingUsersRecord.isPresent()) {
                                TrainingUsersRecord trainingUsersRecord = optionalTrainingUsersRecord.get();
                                Optional<TrainingAttendUsersRecord> optionalTrainingAttendUsersRecord =
                                        trainingAttendUsersService.findByTrainingAttendIdAndTrainingUsersId(bean.getTrainingAttendId(), trainingUsersRecord.getTrainingUsersId());
                                if (optionalTrainingAttendUsersRecord.isPresent()) {
                                    TrainingAttendUsersRecord trainingAttendUsersRecord = optionalTrainingAttendUsersRecord.get();
                                    bean.setOperate(trainingAttendUsersRecord.getOperate());
                                }
                            }
                        }
                    }
                }
            }
        }
        simplePaginationUtil.setTotalSize(trainingAttendService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生更新状态
     *
     * @param trainingAttendId 考勤id
     * @param operate          状态
     * @param location         经纬度
     * @param appId            app id
     * @param resCode          微信code
     * @return true or false
     */
    @ApiLoggingRecord(remark = "实训考勤学生签到数据", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/training/attend/student/operate")
    public ResponseEntity<Map<String, Object>> operate(@RequestParam("trainingAttendId") String trainingAttendId, @RequestParam("operate") Byte operate,
                                                       String location, String resCode, String appId, String remark, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 0:缺席,1:请假,2:迟到,3:正常签到
        // 学生类型判断
        Users users = SessionUtil.getUserFromOauth(principal);
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    int studentId = optionalStudentBean.get().getStudentId();
                    TrainingAttend trainingAttend = trainingAttendService.findById(trainingAttendId);
                    if (Objects.nonNull(trainingAttend)) {
                        Optional<TrainingUsersRecord> optionalTrainingUsersRecord = trainingUsersService.findByTrainingReleaseIdAndStudentId(trainingAttend.getTrainingReleaseId(), studentId);
                        if (optionalTrainingUsersRecord.isPresent()) {
                            TrainingUsersRecord trainingUsersRecord = optionalTrainingUsersRecord.get();
                            Optional<TrainingAttendUsersRecord> optionalTrainingAttendUsersRecord =
                                    trainingAttendUsersService.findByTrainingAttendIdAndTrainingUsersId(trainingAttend.getTrainingAttendId(), trainingUsersRecord.getTrainingUsersId());
                            if (optionalTrainingAttendUsersRecord.isPresent()) {
                                TrainingAttendUsersRecord trainingAttendUsersRecord = optionalTrainingAttendUsersRecord.get();
                                TrainingAttendUsers trainingAttendUsers = trainingAttendUsersRecord.into(TrainingAttendUsers.class);
                                Byte dbOperate = trainingAttendUsersRecord.getOperate();
                                // 空 0:缺席 可变更为其它类型
                                if (Objects.isNull(dbOperate) || dbOperate == 0) {
                                    LocalDateTime now = DateTimeUtil.getNowLocalDateTime();
                                    LocalDateTime startDateTime = DateTimeUtil.defaultParseLocalDateTime(DateTimeUtil.defaultFormatLocalDate(trainingAttend.getAttendDate()) + " " + DateTimeUtil.defaultFormatLocalTime(trainingAttend.getAttendStartTime()));
                                    // 请假可直接更新
                                    if (operate == 1) {
                                        // 检验时间 上课前及上课后10分钟内
                                        if (now.isBefore(startDateTime) || DateTimeUtil.calculationTwoDateDifferMinutes(now, startDateTime) <= 10) {
                                            trainingAttendUsers.setOperate(operate);
                                            trainingAttendUsers.setOperateUser(users.getUsername());
                                            trainingAttendUsers.setRemark(remark);
                                            trainingAttendUsers.setOperateDate(DateTimeUtil.getNowLocalDateTime());

                                            trainingAttendUsersService.update(trainingAttendUsers);

                                            ajaxUtil.success().msg("状态更新成功");
                                        } else {
                                            ajaxUtil.fail().msg("不在时间范围，无法操作");
                                        }

                                    } else if (operate == 2 || operate == 3) {
                                        // 2:迟到,3:正常签到 需要检验微信与地址
                                        if (StringUtils.isNotBlank(location) && StringUtils.isNotBlank(resCode) && StringUtils.isNotBlank(appId)) {
                                            // 检验地址
                                            Optional<SchoolroomBean> optionalSchoolroomBean = schoolroomService.findByIdRelation(trainingAttend.getAttendRoom());
                                            if (optionalSchoolroomBean.isPresent()) {
                                                SchoolroomBean schoolroomBean = optionalSchoolroomBean.get();
                                                String buildingCoordinate = schoolroomBean.getBuildingCoordinate();
                                                if (StringUtils.isNotBlank(buildingCoordinate)) {
                                                    boolean isInCircle = MapUtil.isInCircle(buildingCoordinate, location, "25");
                                                    if (isInCircle) {
                                                        // 检验微信
                                                        AjaxUtil<Object> weiXinInfo = weiXinService.info(resCode, appId, WeiXinAppBook.getAppSecret(appId));
                                                        if (weiXinInfo.getState()) {
                                                            Map<String, Object> mapResult = weiXinInfo.getMapResult();
                                                            String openId = (String) mapResult.get("openid");
                                                            Optional<TrainingAttendUsersRecord> weiXinTrainingAttendUsersRecord =
                                                                    trainingAttendUsersService.findByTrainingAttendIdAndOpenId(trainingAttend.getTrainingAttendId(), openId);
                                                            if (weiXinTrainingAttendUsersRecord.isEmpty()) {
                                                                // 检验时间，上课前10分钟或上课后10分钟内，属于正常签到
                                                                long minutes = DateTimeUtil.calculationTwoDateDifferMinutes(now, startDateTime);
                                                                if (minutes <= 10) {
                                                                    operate = 3;
                                                                    trainingAttendUsers.setOperate(operate);
                                                                    trainingAttendUsers.setOperateUser(users.getUsername());
                                                                    trainingAttendUsers.setOpenId(openId);
                                                                    trainingAttendUsers.setLocation(location);
                                                                    trainingAttendUsers.setRemark(remark);
                                                                    trainingAttendUsers.setOperateDate(DateTimeUtil.getNowLocalDateTime());

                                                                    trainingAttendUsersService.update(trainingAttendUsers);

                                                                    ajaxUtil.success().msg("状态更新成功");
                                                                } else if (now.isAfter(startDateTime) && Math.abs(minutes) <= 30) {
                                                                    // 上课后，超过10分钟，30分钟内属于迟到
                                                                    operate = 2;
                                                                    trainingAttendUsers.setOperate(operate);
                                                                    trainingAttendUsers.setOperateUser(users.getUsername());
                                                                    trainingAttendUsers.setOpenId(openId);
                                                                    trainingAttendUsers.setLocation(location);
                                                                    trainingAttendUsers.setRemark(remark);
                                                                    trainingAttendUsers.setOperateDate(DateTimeUtil.getNowLocalDateTime());

                                                                    trainingAttendUsersService.update(trainingAttendUsers);

                                                                    ajaxUtil.success().msg("状态更新成功");
                                                                } else {
                                                                    ajaxUtil.fail().msg("不在时间范围，无法操作");
                                                                }

                                                            } else {
                                                                ajaxUtil.fail().msg("已签到");
                                                            }
                                                        } else {
                                                            ajaxUtil.fail().msg("获取微信信息失败");
                                                        }
                                                    } else {
                                                        ajaxUtil.fail().msg("不在签到范围");
                                                    }
                                                } else {
                                                    ajaxUtil.fail().msg("未获取到教室位置信息");
                                                }
                                            } else {
                                                ajaxUtil.fail().msg("未获取到教室信息");
                                            }
                                        } else {
                                            ajaxUtil.fail().msg("缺少必要参数");
                                        }
                                    } else {
                                        ajaxUtil.fail().msg("不支持的状态类型");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("当前状态不允许变更");
                                }
                            } else {
                                ajaxUtil.fail().msg("未查询到考勤名单数据");
                            }
                        } else {
                            ajaxUtil.fail().msg("未查询到名单数据");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到考勤数据");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到学生数据");
                }
            } else {
                ajaxUtil.fail().msg("非学生类型，不得使用");
            }
        } else {
            ajaxUtil.fail().msg("未查询到用户类型");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
