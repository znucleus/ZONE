package top.zbeboy.zone.web.training.special;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecial;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.training.TrainingSpecialService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.special.TrainingSpecialBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.special.TrainingSpecialAddVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TrainingSpecialRestController {

    private final Logger log = LoggerFactory.getLogger(TrainingSpecialRestController.class);

    @Resource
    private TrainingSpecialService trainingSpecialService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private FilesService filesService;

    @Resource
    private UploadService uploadService;

    @Resource
    private UsersService usersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/special/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingSpecialBean> ajaxUtil = AjaxUtil.of();
        List<TrainingSpecialBean> beans = new ArrayList<>();
        Result<Record> records = trainingSpecialService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingSpecialBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.specialCondition())));
            beans.forEach(bean -> bean.setRealCover(Workbook.DIRECTORY_SPLIT + bean.getRelativePath()));
        }
        simplePaginationUtil.setTotalSize(trainingSpecialService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param trainingSpecialAddVo 数据
     * @param bindingResult        检验
     * @return true or false
     */
    @PostMapping("/web/training/special/save")
    public ResponseEntity<Map<String, Object>> save(@Valid TrainingSpecialAddVo trainingSpecialAddVo,
                                                    BindingResult bindingResult, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            if (!bindingResult.hasErrors()) {
                if (trainingConditionCommon.specialCondition()) {
                    Users users = usersService.getUserFromSession();
                    TrainingSpecial trainingSpecial = new TrainingSpecial();
                    if (StringUtils.isNotBlank(trainingSpecialAddVo.getFile())) {
                        Files files = BaseImgUtil.generateImage(trainingSpecialAddVo.getFile(),
                                trainingSpecialAddVo.getFileName(), request, Workbook.trainingSpecialCoverPath(), request.getRemoteAddr());
                        filesService.save(files);
                        trainingSpecial.setCover(files.getFileId());
                    } else {
                        trainingSpecial.setCover(Workbook.SYSTEM_COVER);
                    }
                    trainingSpecial.setTitle(trainingSpecialAddVo.getTitle());
                    trainingSpecial.setTrainingSpecialId(UUIDUtil.getUUID());
                    trainingSpecial.setUsername(users.getUsername());
                    trainingSpecial.setPublisher(users.getRealName());
                    trainingSpecial.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
                    trainingSpecialService.save(trainingSpecial);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("您无权限操作");
                }
            } else {
                ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        } catch (Exception e) {
            log.error("User upload cover error.", e);
            ajaxUtil.fail().msg(String.format("上传专题封面异常:%s", e.getMessage()));
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
