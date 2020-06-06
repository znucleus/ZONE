package top.zbeboy.zone.web.training.document;

import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.feign.system.FilesService;
import top.zbeboy.zone.service.training.TrainingDocumentContentService;
import top.zbeboy.zone.service.training.TrainingDocumentFileService;
import top.zbeboy.zone.service.training.TrainingDocumentService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.FilesUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentBean;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentFileBean;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.training.common.TrainingControllerCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.training.document.TrainingDocumentAddVo;
import top.zbeboy.zone.web.vo.training.document.TrainingDocumentEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TrainingDocumentRestController {

    private final Logger log = LoggerFactory.getLogger(TrainingDocumentRestController.class);

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
    private UploadService uploadService;

    @Resource
    private FilesService filesService;

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
                    Users users = SessionUtil.getUserFromSession();
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

    /**
     * 文章更新
     *
     * @param trainingDocumentEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/training/document/update")
    public ResponseEntity<Map<String, Object>> update(@Valid TrainingDocumentEditVo trainingDocumentEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            TrainingDocument trainingDocument = trainingDocumentService.findById(trainingDocumentEditVo.getTrainingDocumentId());
            if (Objects.nonNull(trainingDocument)) {
                if (trainingConditionCommon.canOperator(trainingDocument.getTrainingReleaseId())) {
                    trainingDocument.setDocumentTitle(trainingDocumentEditVo.getDocumentTitle());
                    trainingDocument.setIsOriginal(trainingDocumentEditVo.getIsOriginal());
                    trainingDocument.setOrigin(trainingDocumentEditVo.getOrigin());

                    trainingDocumentService.update(trainingDocument);

                    TrainingDocumentContent trainingDocumentContent = new TrainingDocumentContent();
                    trainingDocumentContent.setTrainingDocumentId(trainingDocument.getTrainingDocumentId());
                    trainingDocumentContent.setTrainingDocumentContent(trainingDocumentEditVo.getTrainingDocumentContent());

                    trainingDocumentContentService.update(trainingDocumentContent);
                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("您无权限操作");
                }
            } else {
                ajaxUtil.fail().msg("未查询到实训文章数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文章删除
     *
     * @param trainingDocumentId 文章id
     * @return true or false
     */
    @PostMapping("/web/training/document/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("trainingDocumentId") String trainingDocumentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingDocument trainingDocument = trainingDocumentService.findById(trainingDocumentId);
        if (Objects.nonNull(trainingDocument)) {
            if (trainingConditionCommon.canOperator(trainingDocument.getTrainingReleaseId())) {
                trainingDocumentService.deleteById(trainingDocumentId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训文章数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 实训文档
     *
     * @param trainingReleaseId 实训发布id
     * @param request           请求
     * @return true or false
     */
    @PostMapping("/web/training/document/upload/file")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("trainingReleaseId") String trainingReleaseId,
                                                          MultipartHttpServletRequest request) {
        AjaxUtil<FileBean> ajaxUtil = AjaxUtil.of();
        try {
            if (trainingConditionCommon.canOperator(trainingReleaseId)) {
                TrainingRelease trainingRelease = trainingReleaseService.findById(trainingReleaseId);
                if (Objects.nonNull(trainingRelease)) {
                    Users users = SessionUtil.getUserFromSession();
                    String path = Workbook.trainingDocumentFilePath(trainingReleaseId);
                    List<FileBean> fileBeens = uploadService.upload(request,
                            RequestUtil.getRealPath(request) + path, request.getRemoteAddr());
                    for (FileBean fileBean : fileBeens) {
                        String fileId = UUIDUtil.getUUID();
                        Files files = new Files();
                        files.setFileId(fileId);
                        files.setExt(fileBean.getExt());
                        files.setNewName(fileBean.getNewName());
                        files.setOriginalFileName(fileBean.getOriginalFileName());
                        files.setFileSize(fileBean.getFileSize());
                        files.setRelativePath(path + fileBean.getNewName());
                        filesService.save(files);

                        TrainingDocumentFile trainingDocumentFile = new TrainingDocumentFile();
                        trainingDocumentFile.setTrainingDocumentFileId(UUIDUtil.getUUID());
                        trainingDocumentFile.setTrainingReleaseId(trainingReleaseId);
                        trainingDocumentFile.setCourseId(trainingRelease.getCourseId());
                        trainingDocumentFile.setFileId(fileId);
                        trainingDocumentFile.setUsername(users.getUsername());
                        trainingDocumentFile.setUploader(users.getRealName());
                        trainingDocumentFile.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                        trainingDocumentFile.setDownloads(0);

                        trainingDocumentFileService.save(trainingDocumentFile);
                    }

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到实训发布数据");
                }

            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } catch (Exception e) {
            log.error("Upload file error, error is {}", e);
            ajaxUtil.fail().msg("上传文件失败： " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文档删除
     *
     * @param trainingDocumentFileId 文档id
     * @return true or false
     */
    @PostMapping("/web/training/document/delete/file")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam("trainingDocumentFileId") String trainingDocumentFileId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        TrainingDocumentFile trainingDocumentFile = trainingDocumentFileService.findById(trainingDocumentFileId);
        if (Objects.nonNull(trainingDocumentFile)) {
            if (trainingConditionCommon.canOperator(trainingDocumentFile.getTrainingReleaseId())) {
                trainingDocumentFileService.deleteById(trainingDocumentFileId);
                Files files = filesService.findById(trainingDocumentFile.getFileId());
                if (Objects.nonNull(files)) {
                    FilesUtil.deleteFile(RequestUtil.getRealPath(request) + files.getRelativePath());
                    filesService.delete(files);
                }
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg("未查询到实训文档数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param id       文件id
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/web/training/document/download/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        TrainingDocumentFile trainingDocumentFile = trainingDocumentFileService.findById(id);
        if (Objects.nonNull(trainingDocumentFile)) {
            trainingDocumentFileService.updateDownloads(id);
            Files files = filesService.findById(trainingDocumentFile.getFileId());
            if (Objects.nonNull(files)) {
                uploadService.download(files.getOriginalFileName(), files.getRelativePath(), response, request);
            }
        }

    }
}
