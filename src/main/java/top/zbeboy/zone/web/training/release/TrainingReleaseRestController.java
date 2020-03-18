package top.zbeboy.zone.web.training.release;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.release.TrainingReleaseAddVo;
import top.zbeboy.zone.web.vo.training.release.TrainingReleaseEditVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TrainingReleaseRestController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private UsersService usersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/release/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingReleaseBean> ajaxUtil = AjaxUtil.of();
        List<TrainingReleaseBean> beans = new ArrayList<>();
        Result<Record> records = trainingReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.canOperator(bean.getTrainingReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(trainingReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param trainingReleaseAddVo 实训
     * @param bindingResult        检验
     * @return true or false
     */
    @PostMapping("/web/training/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid TrainingReleaseAddVo trainingReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TrainingRelease trainingRelease = new TrainingRelease();
            trainingRelease.setTrainingReleaseId(UUIDUtil.getUUID());
            trainingRelease.setTitle(trainingReleaseAddVo.getTitle());
            trainingRelease.setOrganizeId(trainingReleaseAddVo.getOrganizeId());
            trainingRelease.setCourseId(trainingReleaseAddVo.getCourseId());
            trainingRelease.setStartDate(DateTimeUtil.defaultParseSqlDate(trainingReleaseAddVo.getStartDate()));
            trainingRelease.setEndDate(DateTimeUtil.defaultParseSqlDate(trainingReleaseAddVo.getEndDate()));
            trainingRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
            Users users = usersService.getUserFromSession();
            trainingRelease.setPublisher(users.getRealName());
            trainingRelease.setUsername(users.getUsername());

            trainingReleaseService.save(trainingRelease);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param trainingReleaseEditVo 实训
     * @param bindingResult         检验
     * @return true or false
     */
    @PostMapping("/web/training/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingReleaseEditVo trainingReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TrainingRelease trainingRelease = trainingReleaseService.findById(trainingReleaseEditVo.getTrainingReleaseId());
            trainingRelease.setTitle(trainingReleaseEditVo.getTitle());
            trainingRelease.setCourseId(trainingReleaseEditVo.getCourseId());
            trainingRelease.setStartDate(DateTimeUtil.defaultParseSqlDate(trainingReleaseEditVo.getStartDate()));
            trainingRelease.setEndDate(DateTimeUtil.defaultParseSqlDate(trainingReleaseEditVo.getEndDate()));

            trainingReleaseService.update(trainingRelease);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
