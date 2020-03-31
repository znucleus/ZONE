package top.zbeboy.zone.web.training.attend;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.training.*;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.attend.TrainingAttendBean;
import top.zbeboy.zone.web.bean.training.release.TrainingConfigureBean;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.attend.TrainingAttendAddVo;
import top.zbeboy.zone.web.vo.training.attend.TrainingAttendEditVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TrainingAttendRestController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingAttendService trainingAttendService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingConfigureService trainingConfigureService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingAttendUsersService trainingAttendUsersService;

    @Resource
    private UsersService usersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/attend/training/data")
    public ResponseEntity<Map<String, Object>> trainingData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingReleaseBean> ajaxUtil = AjaxUtil.of();
        List<TrainingReleaseBean> beans = new ArrayList<>();
        Result<Record> records = trainingReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
        }
        simplePaginationUtil.setTotalSize(trainingReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/attend/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingAttendBean> ajaxUtil = AjaxUtil.of();
        List<TrainingAttendBean> beans = new ArrayList<>();
        Result<Record> records = trainingAttendService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingAttendBean.class);
            beans.forEach(bean -> bean.setPublishDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getPublishDate())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.usersCondition(bean.getTrainingReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(trainingAttendService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置数据
     *
     * @return 数据
     */
    @GetMapping("/web/training/attend/configure/data/{id}")
    public ResponseEntity<Map<String, Object>> configureData(@PathVariable("id") String id) {
        AjaxUtil<TrainingConfigureBean> ajaxUtil = AjaxUtil.of();
        List<TrainingConfigureBean> beans = new ArrayList<>();
        if (trainingConditionCommon.usersCondition(id)) {
            Result<Record> records = trainingConfigureService.findByTrainingReleaseIdRelation(id);
            if (records.isNotEmpty()) {
                beans = records.into(TrainingConfigureBean.class);
            }
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 配置发布
     *
     * @param trainingConfigureId 配置id
     * @return true or false
     */
    @PostMapping("/web/training/attend/configure/release")
    public ResponseEntity<Map<String, Object>> configureRelease(@RequestParam("trainingConfigureId") String trainingConfigureId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingConfigure trainingConfigure = trainingConfigureService.findById(trainingConfigureId);
        if (Objects.nonNull(trainingConfigure)) {
            if (trainingConditionCommon.usersCondition(trainingConfigure.getTrainingReleaseId())) {
                TrainingAttend trainingAttend = new TrainingAttend();
                String trainingAttendId = UUIDUtil.getUUID();
                trainingAttend.setTrainingAttendId(trainingAttendId);
                trainingAttend.setTrainingReleaseId(trainingConfigure.getTrainingReleaseId());
                trainingAttend.setAttendDate(DateTimeUtil.getNowSqlDate());
                trainingAttend.setAttendStartTime(trainingConfigure.getStartTime());
                trainingAttend.setAttendEndTime(trainingConfigure.getEndTime());
                trainingAttend.setAttendRoom(trainingConfigure.getSchoolroomId());
                trainingAttend.setPublishDate(DateTimeUtil.getNowSqlTimestamp());
                trainingAttendService.save(trainingAttend);

                Users user = usersService.getUserFromSession();
                List<TrainingUsers> trainingUsers = trainingUsersService.findByTrainingReleaseId(trainingConfigure.getTrainingReleaseId());
                if(Objects.nonNull(trainingUsers)){
                    List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                    for(TrainingUsers users : trainingUsers){
                        TrainingAttendUsers trainingAttendUser = new TrainingAttendUsers();
                        trainingAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        trainingAttendUser.setTrainingAttendId(trainingAttendId);
                        trainingAttendUser.setTrainingUsersId(users.getTrainingUsersId());
                        trainingAttendUser.setOperateUser(user.getUsername());
                        trainingAttendUser.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                        trainingAttendUser.setOperate(ByteUtil.toByte(0));
                        trainingAttendUser.setRemark(users.getRemark());

                        trainingAttendUsers.add(trainingAttendUser);
                    }

                    trainingAttendUsersService.batchSave(trainingAttendUsers);
                }
                ajaxUtil.success().msg("发布成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训配置数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 自定义发布
     *
     * @param trainingAttendAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/attend/release/save")
    public ResponseEntity<Map<String, Object>> releaseSave(@Valid TrainingAttendAddVo trainingAttendAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.usersCondition(trainingAttendAddVo.getTrainingReleaseId())) {
                TrainingAttend trainingAttend = new TrainingAttend();
                String trainingAttendId = UUIDUtil.getUUID();
                trainingAttend.setTrainingAttendId(trainingAttendId);
                trainingAttend.setTrainingReleaseId(trainingAttendAddVo.getTrainingReleaseId());
                trainingAttend.setAttendDate(DateTimeUtil.defaultParseSqlDate(trainingAttendAddVo.getAttendDate()));
                trainingAttend.setAttendStartTime(DateTimeUtil.defaultParseSqlTime(trainingAttendAddVo.getAttendStartTime()));
                trainingAttend.setAttendEndTime(DateTimeUtil.defaultParseSqlTime(trainingAttendAddVo.getAttendEndTime()));
                trainingAttend.setAttendRoom(trainingAttendAddVo.getAttendRoom());
                trainingAttend.setPublishDate(DateTimeUtil.getNowSqlTimestamp());
                trainingAttend.setRemark(trainingAttendAddVo.getRemark());
                trainingAttendService.save(trainingAttend);

                Users user = usersService.getUserFromSession();
                List<TrainingUsers> trainingUsers = trainingUsersService.findByTrainingReleaseId(trainingAttendAddVo.getTrainingReleaseId());
                if(Objects.nonNull(trainingUsers)){
                    List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                    for(TrainingUsers users : trainingUsers){
                        TrainingAttendUsers trainingAttendUser = new TrainingAttendUsers();
                        trainingAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                        trainingAttendUser.setTrainingAttendId(trainingAttendId);
                        trainingAttendUser.setTrainingUsersId(users.getTrainingUsersId());
                        trainingAttendUser.setOperateUser(user.getUsername());
                        trainingAttendUser.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                        trainingAttendUser.setOperate(ByteUtil.toByte(0));
                        trainingAttendUser.setRemark(users.getRemark());

                        trainingAttendUsers.add(trainingAttendUser);
                    }

                    trainingAttendUsersService.batchSave(trainingAttendUsers);
                }
                ajaxUtil.success().msg("发布成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 编辑
     *
     * @param trainingAttendEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/attend/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingAttendEditVo trainingAttendEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.usersCondition(trainingAttendEditVo.getTrainingReleaseId())) {
                TrainingAttend trainingAttend = trainingAttendService.findById(trainingAttendEditVo.getTrainingAttendId());
                trainingAttend.setAttendDate(DateTimeUtil.defaultParseSqlDate(trainingAttendEditVo.getAttendDate()));
                trainingAttend.setAttendStartTime(DateTimeUtil.defaultParseSqlTime(trainingAttendEditVo.getAttendStartTime()));
                trainingAttend.setAttendEndTime(DateTimeUtil.defaultParseSqlTime(trainingAttendEditVo.getAttendEndTime()));
                trainingAttend.setAttendRoom(trainingAttendEditVo.getAttendRoom());
                trainingAttend.setRemark(trainingAttendEditVo.getRemark());
                trainingAttendService.update(trainingAttend);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

}
