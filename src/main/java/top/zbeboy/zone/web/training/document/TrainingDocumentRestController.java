package top.zbeboy.zone.web.training.document;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.training.TrainingDocumentFileService;
import top.zbeboy.zone.service.training.TrainingDocumentService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentBean;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentFileBean;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TrainingDocumentRestController {

    @Resource
    private TrainingControllerCommon trainingControllerCommon;

    @Resource
    private TrainingDocumentService trainingDocumentService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingDocumentFileService trainingDocumentFileService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/document/training/data")
    public ResponseEntity<Map<String, Object>> trainingData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingReleaseBean> ajaxUtil = trainingControllerCommon.trainingData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/document/list/data")
    public ResponseEntity<Map<String, Object>> documentListData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingDocumentBean> ajaxUtil = AjaxUtil.of();
        List<TrainingDocumentBean> beans = new ArrayList<>();
        Result<Record> records = trainingDocumentService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingDocumentBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.canOperator(bean.getTrainingReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(trainingDocumentService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文档数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/document/file/data")
    public ResponseEntity<Map<String, Object>> documentFileData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingDocumentFileBean> ajaxUtil = AjaxUtil.of();
        List<TrainingDocumentFileBean> beans = new ArrayList<>();
        Result<Record> records = trainingDocumentFileService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingDocumentFileBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.canOperator(bean.getTrainingReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(trainingDocumentFileService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
