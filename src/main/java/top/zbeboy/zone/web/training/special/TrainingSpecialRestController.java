package top.zbeboy.zone.web.training.special;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.TrainingSpecialFileTypeRecord;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.training.*;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.FilesUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.special.TrainingSpecialBean;
import top.zbeboy.zone.web.bean.training.special.TrainingSpecialDocumentBean;
import top.zbeboy.zone.web.bean.training.special.TrainingSpecialFileBean;
import top.zbeboy.zone.web.bean.training.special.TrainingSpecialFileTypeBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.special.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class TrainingSpecialRestController {

    private final Logger log = LoggerFactory.getLogger(TrainingSpecialRestController.class);

    @Resource
    private TrainingSpecialService trainingSpecialService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingSpecialDocumentService trainingSpecialDocumentService;

    @Resource
    private TrainingSpecialDocumentContentService trainingSpecialDocumentContentService;

    @Resource
    private TrainingSpecialFileTypeService trainingSpecialFileTypeService;

    @Resource
    private TrainingSpecialFileService trainingSpecialFileService;

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

    /**
     * 更新
     *
     * @param trainingSpecialEditVo 数据
     * @param bindingResult         检验
     * @return true or false
     */
    @PostMapping("/web/training/special/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingSpecialEditVo trainingSpecialEditVo,
                                                      BindingResult bindingResult, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            if (!bindingResult.hasErrors()) {
                if (trainingConditionCommon.specialCondition()) {
                    Optional<Record> record = trainingSpecialService.findByIdRelation(trainingSpecialEditVo.getTrainingSpecialId());
                    if (record.isPresent()) {
                        TrainingSpecialBean bean = record.get().into(TrainingSpecialBean.class);
                        TrainingSpecial trainingSpecial = record.get().into(TrainingSpecial.class);
                        trainingSpecial.setTitle(trainingSpecialEditVo.getTitle());

                        boolean updateCover = false;
                        if (StringUtils.isNotBlank(trainingSpecialEditVo.getFile())) {
                            if (!StringUtils.equals(bean.getNewName() + "." + bean.getExt(), trainingSpecialEditVo.getFileName())) {
                                Files files = BaseImgUtil.generateImage(trainingSpecialEditVo.getFile(),
                                        trainingSpecialEditVo.getFileName(), request, Workbook.trainingSpecialCoverPath(), request.getRemoteAddr());
                                filesService.save(files);
                                trainingSpecial.setCover(files.getFileId());
                                updateCover = true;
                            }
                        } else {
                            trainingSpecial.setCover(Workbook.SYSTEM_COVER);
                            updateCover = true;
                        }
                        trainingSpecialService.update(trainingSpecial);
                        // 处理旧文件
                        if (updateCover && !StringUtils.equals(bean.getCover(), Workbook.SYSTEM_COVER)) {
                            FilesUtil.deleteFile(RequestUtil.getRealPath(request) + bean.getRelativePath());
                            filesService.deleteById(bean.getCover());
                        }
                        ajaxUtil.success().msg("更新成功");
                    } else {
                        ajaxUtil.fail().msg("未查询到实训专题数据");
                    }
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

    /**
     * 专题删除
     *
     * @param trainingSpecialId 专题id
     * @return true or false
     */
    @PostMapping("/web/training/special/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingSpecialId") String trainingSpecialId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.specialCondition()) {
            trainingSpecialService.deleteById(trainingSpecialId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/training/special/document/data")
    public ResponseEntity<Map<String, Object>> documentListData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TrainingSpecialDocumentBean> ajaxUtil = AjaxUtil.of();
        List<TrainingSpecialDocumentBean> beans = new ArrayList<>();
        Result<Record> records = trainingSpecialDocumentService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TrainingSpecialDocumentBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.specialCondition())));
        }
        simplePaginationUtil.setTotalSize(trainingSpecialDocumentService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章保存
     *
     * @param trainingSpecialDocumentAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/special/document/save")
    public ResponseEntity<Map<String, Object>> documentSave(@Valid TrainingSpecialDocumentAddVo trainingSpecialDocumentAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.specialCondition()) {
                Users users = usersService.getUserFromSession();
                TrainingSpecialDocument trainingSpecialDocument = new TrainingSpecialDocument();
                String trainingSpecialDocumentId = UUIDUtil.getUUID();
                trainingSpecialDocument.setTrainingSpecialDocumentId(trainingSpecialDocumentId);
                trainingSpecialDocument.setTrainingSpecialId(trainingSpecialDocumentAddVo.getTrainingSpecialId());
                trainingSpecialDocument.setTitle(trainingSpecialDocumentAddVo.getTitle());
                trainingSpecialDocument.setUsername(users.getUsername());
                trainingSpecialDocument.setCreator(users.getRealName());
                trainingSpecialDocument.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                trainingSpecialDocument.setReading(0);
                trainingSpecialDocumentService.save(trainingSpecialDocument);

                TrainingSpecialDocumentContent trainingSpecialDocumentContent = new TrainingSpecialDocumentContent();
                trainingSpecialDocumentContent.setTrainingSpecialDocumentId(trainingSpecialDocumentId);
                trainingSpecialDocumentContent.setContent(trainingSpecialDocumentAddVo.getContent());
                trainingSpecialDocumentContentService.save(trainingSpecialDocumentContent);

                ajaxUtil.success().msg("保存成功");

            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章更新
     *
     * @param trainingSpecialDocumentEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/special/document/update")
    public ResponseEntity<Map<String, Object>> documentUpdate(@Valid TrainingSpecialDocumentEditVo trainingSpecialDocumentEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TrainingSpecialDocument trainingSpecialDocument = trainingSpecialDocumentService.findById(trainingSpecialDocumentEditVo.getTrainingSpecialDocumentId());
            if (Objects.nonNull(trainingSpecialDocument)) {
                if (trainingConditionCommon.specialCondition()) {
                    trainingSpecialDocument.setTitle(trainingSpecialDocumentEditVo.getTitle());
                    trainingSpecialDocumentService.update(trainingSpecialDocument);

                    TrainingSpecialDocumentContent trainingSpecialDocumentContent = new TrainingSpecialDocumentContent();
                    trainingSpecialDocumentContent.setTrainingSpecialDocumentId(trainingSpecialDocumentEditVo.getTrainingSpecialDocumentId());
                    trainingSpecialDocumentContent.setContent(trainingSpecialDocumentEditVo.getContent());
                    trainingSpecialDocumentContentService.update(trainingSpecialDocumentContent);
                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("您无权限操作");
                }
            } else {
                ajaxUtil.fail().msg("未查询到实训专题文章数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章删除
     *
     * @param trainingSpecialDocumentId 文章id
     * @return true or false
     */
    @PostMapping("/web/training/special/document/delete")
    public ResponseEntity<Map<String, Object>> documentDelete(@RequestParam("trainingSpecialDocumentId") String trainingSpecialDocumentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.specialCondition()) {
            trainingSpecialDocumentService.deleteById(trainingSpecialDocumentId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件类型数据
     *
     * @param id 专题id
     * @return 数据
     */
    @GetMapping("/web/training/special/file/type/data/{id}")
    public ResponseEntity<Map<String, Object>> fileTypeData(@PathVariable("id") String id) {
        AjaxUtil<TrainingSpecialFileTypeBean> ajaxUtil = AjaxUtil.of();
        List<TrainingSpecialFileTypeBean> trainingSpecialFileTypes = new ArrayList<>();
        Result<TrainingSpecialFileTypeRecord> records =
                trainingSpecialFileTypeService.findByTrainingSpecialId(id);
        if (records.isNotEmpty()) {
            trainingSpecialFileTypes = records.into(TrainingSpecialFileTypeBean.class);
            trainingSpecialFileTypes.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.specialCondition())));
        }
        ajaxUtil.success().list(trainingSpecialFileTypes).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件数据
     *
     * @param id 专题id
     * @return 数据
     */
    @GetMapping("/web/training/special/file/data/{id}")
    public ResponseEntity<Map<String, Object>> fileData(@PathVariable("id") String id) {
        AjaxUtil<TrainingSpecialFileBean> ajaxUtil = AjaxUtil.of();
        List<TrainingSpecialFileBean> trainingSpecialFileBeans = new ArrayList<>();
        Result<Record> records;
        if (trainingConditionCommon.specialCondition()) {
            records = trainingSpecialFileService.findByFileTypeId(id);
        } else {
            records = trainingSpecialFileService.findByFileTypeIdAndMapping(id, BooleanUtil.toByte(true));
        }

        if (records.isNotEmpty()) {
            trainingSpecialFileBeans = records.into(TrainingSpecialFileBean.class);
            trainingSpecialFileBeans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(trainingConditionCommon.specialCondition())));
        }
        ajaxUtil.success().list(trainingSpecialFileBeans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件类型保存
     *
     * @param fileTypeName      文件类型名
     * @param trainingSpecialId 专题id
     * @return true or false
     */
    @PostMapping("/web/training/special/file/type/save")
    public ResponseEntity<Map<String, Object>> fileTypeSave(@RequestParam("fileTypeName") String fileTypeName,
                                                            @RequestParam("trainingSpecialId") String trainingSpecialId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();

        if (trainingConditionCommon.specialCondition()) {
            TrainingSpecialFileType trainingSpecialFileType = new TrainingSpecialFileType();
            trainingSpecialFileType.setFileTypeId(UUIDUtil.getUUID());
            trainingSpecialFileType.setFileTypeName(fileTypeName);
            trainingSpecialFileType.setTrainingSpecialId(trainingSpecialId);
            trainingSpecialFileTypeService.save(trainingSpecialFileType);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件类型保存
     *
     * @param fileTypeName 文件类型名
     * @param fileTypeId   类型id
     * @return true or false
     */
    @PostMapping("/web/training/special/file/type/update")
    public ResponseEntity<Map<String, Object>> fileTypeUpdate(@RequestParam("fileTypeName") String fileTypeName,
                                                              @RequestParam("fileTypeId") String fileTypeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.specialCondition()) {
            TrainingSpecialFileType trainingSpecialFileType = trainingSpecialFileTypeService.findById(fileTypeId);
            trainingSpecialFileType.setFileTypeName(fileTypeName);
            trainingSpecialFileTypeService.update(trainingSpecialFileType);
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件类型删除
     *
     * @param fileTypeId 类型id
     * @return true or false
     */
    @PostMapping("/web/training/special/file/type/delete")
    public ResponseEntity<Map<String, Object>> fileTypeDelete(@RequestParam("fileTypeId") String fileTypeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.specialCondition()) {
            trainingSpecialFileTypeService.deleteById(fileTypeId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件类型保存
     *
     * @param trainingSpecialFileAddVo 数据
     * @param bindingResult            检验
     * @return true or false
     */
    @PostMapping("/web/training/special/file/save")
    public ResponseEntity<Map<String, Object>> fileSave(@Valid TrainingSpecialFileAddVo trainingSpecialFileAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (trainingConditionCommon.specialCondition()) {
                Files files = new Files();
                String fileId = UUIDUtil.getUUID();
                files.setFileId(fileId);
                files.setRelativePath(Workbook.trainingSpecialFilePath());
                files.setContentType(trainingSpecialFileAddVo.getContentType());
                files.setOriginalFileName(trainingSpecialFileAddVo.getOriginalFileName());
                files.setNewName(trainingSpecialFileAddVo.getNewName());
                files.setExt(trainingSpecialFileAddVo.getExt());
                files.setFileSize(trainingSpecialFileAddVo.getFileSize());
                filesService.save(files);

                Users users = usersService.getUserFromSession();
                TrainingSpecialFile trainingSpecialFile = new TrainingSpecialFile();
                trainingSpecialFile.setTrainingSpecialFileId(UUIDUtil.getUUID());
                trainingSpecialFile.setFileTypeId(trainingSpecialFileAddVo.getFileTypeId());
                trainingSpecialFile.setFileId(fileId);
                trainingSpecialFile.setUsername(users.getUsername());
                trainingSpecialFile.setUploader(users.getRealName());
                trainingSpecialFile.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                trainingSpecialFile.setDownloads(0);
                trainingSpecialFile.setMapping(BooleanUtil.toByte(false));

                trainingSpecialFileService.save(trainingSpecialFile);

                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件删除
     *
     * @param trainingSpecialFileId 文件id
     * @return true or false
     */
    @PostMapping("/web/training/special/file/delete")
    public ResponseEntity<Map<String, Object>> fileDelete(@RequestParam("trainingSpecialFileId") String trainingSpecialFileId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (trainingConditionCommon.specialCondition()) {
            trainingSpecialFileService.deleteById(trainingSpecialFileId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
