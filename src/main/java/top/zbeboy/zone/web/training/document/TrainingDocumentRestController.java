package top.zbeboy.zone.web.training.document;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.TrainingDocument;
import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentContent;
import top.zbeboy.zone.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.training.TrainingDocumentContentService;
import top.zbeboy.zone.service.training.TrainingDocumentFileService;
import top.zbeboy.zone.service.training.TrainingDocumentService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentBean;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentFileBean;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.document.TrainingDocumentAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Resource
    private TrainingDocumentContentService trainingDocumentContentService;

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private UsersService usersService;

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

    /**
     * 文章保存
     *
     * @param trainingDocumentAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/document/save")
    public ResponseEntity<Map<String, Object>> save(@Valid TrainingDocumentAddVo trainingDocumentAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.canOperator(trainingDocumentAddVo.getTrainingReleaseId())) {
                TrainingRelease trainingRelease = trainingReleaseService.findById(trainingDocumentAddVo.getTrainingReleaseId());
                if (Objects.nonNull(trainingRelease)) {
                    Users users = usersService.getUserFromSession();
                    TrainingDocument trainingDocument = new TrainingDocument();
                    String trainingDocumentId = UUIDUtil.getUUID();
                    trainingDocument.setTrainingDocumentId(trainingDocumentId);
                    trainingDocument.setTrainingReleaseId(trainingDocumentAddVo.getTrainingReleaseId());
                    trainingDocument.setDocumentTitle(trainingDocumentAddVo.getDocumentTitle());
                    trainingDocument.setUsername(users.getUsername());
                    trainingDocument.setCourseId(trainingRelease.getCourseId());
                    trainingDocument.setCreator(users.getRealName());
                    trainingDocument.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    trainingDocument.setReading(0);
                    trainingDocument.setIsOriginal(trainingDocumentAddVo.getIsOriginal());
                    trainingDocument.setOrigin(trainingDocumentAddVo.getOrigin());

                    trainingDocumentService.save(trainingDocument);

                    TrainingDocumentContent trainingDocumentContent = new TrainingDocumentContent();
                    trainingDocumentContent.setTrainingDocumentId(trainingDocumentId);
                    trainingDocumentContent.setTrainingDocumentContent(trainingDocumentAddVo.getTrainingDocumentContent());

                    trainingDocumentContentService.save(trainingDocumentContent);

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实训发布数据");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
