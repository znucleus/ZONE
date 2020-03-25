package top.zbeboy.zone.web.training.users;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.training.TrainingUsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.bean.training.users.TrainingUsersBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TrainingUsersRestController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/users/training/data")
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
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/training/users/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("studentNumber");
        headers.add("username");
        headers.add("mobile");
        headers.add("email");
        headers.add("sex");
        headers.add("remark");
        headers.add("createDateStr");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = trainingUsersService.findAllByPage(dataTablesUtil);
        List<TrainingUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(TrainingUsersBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
            beans.forEach(bean -> {
                if (!trainingConditionCommon.canOperator(bean.getTrainingReleaseId())) {
                    bean.setEmail(StringUtils.overlay(bean.getEmail(), "****", 1, bean.getEmail().lastIndexOf("@")));
                    bean.setMobile(StringUtils.overlay(bean.getMobile(), "****", 3, 6));
                }
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(trainingUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(trainingUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
