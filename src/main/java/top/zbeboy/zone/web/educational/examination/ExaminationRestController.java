package top.zbeboy.zone.web.educational.examination;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zbase.bean.educational.calendar.SchoolCalendarAuthoritiesBean;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeAuthoritiesBean;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeDetailBean;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.ExaminationNoticeDetail;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zbase.tools.service.util.*;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarAuthoritiesAddVo;
import top.zbeboy.zbase.vo.educational.examination.*;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.excel.ExaminationNoticeExcel;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class ExaminationRestController {

    private final Logger log = LoggerFactory.getLogger(ExaminationRestController.class);

    @Resource
    private EducationalExaminationService educationalExaminationService;

    @Resource
    private UploadService uploadService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务考试数据", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/examination/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<ExaminationNoticeReleaseBean> ajaxUtil = educationalExaminationService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 上传文件
     *
     * @param request 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/upload/file")
    public ResponseEntity<Map<String, Object>> uploadFile(MultipartHttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            String path = Workbook.tempPath();
            List<FileBean> fileBeens = uploadService.upload(request,
                    RequestUtil.getRealPath(request) + path, RequestUtil.getIpAddress(request));

            Map<String, Object> map = new HashMap<>();
            for (FileBean fileBean : fileBeens) {
                ExaminationNoticeExcel examinationNoticeExcel = new ExaminationNoticeExcel(fileBean.getRelativePath(), 2);
                map = examinationNoticeExcel.readExcelWithTitle();
                FilesUtil.deleteFile(fileBean.getRelativePath());
            }
            if (map.containsKey(ReadExcelUtil.SHEETS_KEY)) {
                List<ExaminationNoticeReleaseBean> beans = new ArrayList<>();
                Integer sheets = (Integer) map.get(ReadExcelUtil.SHEETS_KEY);
                for (int i = 0; i < sheets; i++) {
                    if (map.containsKey(ReadExcelUtil.TITLE_KEY + i)) {
                        Map<String, String> titles = (Map<String, String>) map.get(ReadExcelUtil.TITLE_KEY + i);
                        if (Objects.nonNull(titles)) {
                            String examinationNoticeReleaseId = UUIDUtil.getUUID();
                            ExaminationNoticeReleaseBean bean = new ExaminationNoticeReleaseBean();
                            bean.setExaminationNoticeReleaseId(examinationNoticeReleaseId);
                            bean.setTitle(titles.get("title"));
                            bean.setUsername(users.getUsername());
                            bean.setPublisher(users.getRealName());
                            bean.setReleaseTime(DateTimeUtil.getNowLocalDateTime());

                            if (map.containsKey(ReadExcelUtil.DATA_KEY + i)) {
                                List<ExaminationNoticeDetail> data = (List<ExaminationNoticeDetail>) map.get(ReadExcelUtil.DATA_KEY + i);
                                if (Objects.nonNull(data)) {
                                    for (ExaminationNoticeDetail examinationNoticeDetail : data) {
                                        examinationNoticeDetail.setExaminationNoticeReleaseId(examinationNoticeReleaseId);
                                    }
                                    bean.setExaminationNoticeDetailList(data);
                                }

                                beans.add(bean);
                            }
                        }
                    }
                }

                if (!beans.isEmpty()) {
                    ajaxUtil = educationalExaminationService.batchSave(beans);
                } else {
                    ajaxUtil.fail().msg("无数据");
                }
            } else {
                ajaxUtil.fail().msg("上传失败，无数据");
            }
        } catch (Exception e) {
            log.error("Upload file exception,is {}", e);
            ajaxUtil.fail().msg("上传文件失败： " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 发布保存
     *
     * @param examinationNoticeReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/release/save")
    public ResponseEntity<Map<String, Object>> releaseSave(ExaminationNoticeReleaseAddVo examinationNoticeReleaseAddVo) {
        Users users = SessionUtil.getUserFromSession();
        examinationNoticeReleaseAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.releaseSave(examinationNoticeReleaseAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 发布更新
     *
     * @param examinationNoticeReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/release/update")
    public ResponseEntity<Map<String, Object>> releaseUpdate(ExaminationNoticeReleaseEditVo examinationNoticeReleaseEditVo) {
        Users users = SessionUtil.getUserFromSession();
        examinationNoticeReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.releaseUpdate(examinationNoticeReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 发布id
     * @return true or false
     */
    @PostMapping("/web/educational/examination/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.releaseDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 详情数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/educational/examination/detail/paging")
    public ResponseEntity<Map<String, Object>> authorizeData(SimplePaginationUtil simplePaginationUtil) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<ExaminationNoticeDetailBean> ajaxUtil = educationalExaminationService.detailData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 上传文件
     *
     * @param request 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/detail/upload/file")
    public ResponseEntity<Map<String, Object>> detailUploadFile(MultipartHttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            String examinationNoticeReleaseId = request.getParameter("examinationNoticeReleaseId");
            String path = Workbook.tempPath();
            List<FileBean> fileBeens = uploadService.upload(request,
                    RequestUtil.getRealPath(request) + path, RequestUtil.getIpAddress(request));

            Map<String, Object> map = new HashMap<>();
            for (FileBean fileBean : fileBeens) {
                ExaminationNoticeExcel examinationNoticeExcel = new ExaminationNoticeExcel(fileBean.getRelativePath(), 2);
                map = examinationNoticeExcel.readExcelWithTitle();
                FilesUtil.deleteFile(fileBean.getRelativePath());
            }
            if (map.containsKey(ReadExcelUtil.SHEETS_KEY)) {

                List<ExaminationNoticeDetail> beans = new ArrayList<>();
                Integer sheets = (Integer) map.get(ReadExcelUtil.SHEETS_KEY);
                for (int i = 0; i < sheets; i++) {
                    if (map.containsKey(ReadExcelUtil.DATA_KEY + i)) {
                        List<ExaminationNoticeDetail> data = (List<ExaminationNoticeDetail>) map.get(ReadExcelUtil.DATA_KEY + i);
                        if (Objects.nonNull(data)) {
                            for (ExaminationNoticeDetail examinationNoticeDetail : data) {
                                examinationNoticeDetail.setExaminationNoticeReleaseId(examinationNoticeReleaseId);
                            }

                            beans.addAll(data);
                        }
                    }
                }

                if (!beans.isEmpty()) {
                    ajaxUtil = educationalExaminationService.detailBatchSave(beans, users.getUsername(), examinationNoticeReleaseId);
                } else {
                    ajaxUtil.fail().msg("无数据");
                }
            } else {
                ajaxUtil.fail().msg("上传失败，无数据");
            }
        } catch (Exception e) {
            log.error("Upload file exception,is {}", e);
            ajaxUtil.fail().msg("上传文件失败： " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 详情保存
     *
     * @param examinationNoticeDetailAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/detail/save")
    public ResponseEntity<Map<String, Object>> detailSave(ExaminationNoticeDetailAddVo examinationNoticeDetailAddVo) {
        Users users = SessionUtil.getUserFromSession();
        examinationNoticeDetailAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.detailSave(examinationNoticeDetailAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 详情更新
     *
     * @param examinationNoticeDetailEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/detail/update")
    public ResponseEntity<Map<String, Object>> detailUpdate(ExaminationNoticeDetailEditVo examinationNoticeDetailEditVo) {
        Users users = SessionUtil.getUserFromSession();
        examinationNoticeDetailEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.detailUpdate(examinationNoticeDetailEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 详情id
     * @return true or false
     */
    @PostMapping("/web/educational/examination/detail/delete")
    public ResponseEntity<Map<String, Object>> detailDelete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.detailDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 短信订阅
     *
     * @param id 详情id
     * @return true or false
     */
    @PostMapping("/web/educational/examination/sms-subscribe/save")
    public ResponseEntity<Map<String, Object>> smsSubscribe(@RequestParam("id") String id) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        if (StringUtils.isNotBlank(users.getMobile())) {
            ajaxUtil = educationalExaminationService.examinationSmsSubscribeSave(id, users.getMobile());
        } else {
            ajaxUtil.fail().msg("手机号为空，无法订阅");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限数据
     *
     * @param tableSawUtil 请求
     * @return 数据
     */
    @GetMapping("/web/educational/examination/authorize/paging")
    public ResponseEntity<Map<String, Object>> authorizeData(TableSawUtil tableSawUtil) {
        Users users = SessionUtil.getUserFromSession();
        tableSawUtil.setUsername(users.getUsername());
        AjaxUtil<ExaminationNoticeAuthoritiesBean> ajaxUtil = educationalExaminationService.authorizeData(tableSawUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param examinationNoticeAuthoritiesAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/examination/authorize/save")
    public ResponseEntity<Map<String, Object>> authorizeSave(ExaminationNoticeAuthoritiesAddVo examinationNoticeAuthoritiesAddVo) {
        Users users = SessionUtil.getUserFromSession();
        examinationNoticeAuthoritiesAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.authorizeSave(examinationNoticeAuthoritiesAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 权限id
     * @return true or false
     */
    @PostMapping("/web/educational/examination/authorize/delete")
    public ResponseEntity<Map<String, Object>> authorizeDelete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.authorizeDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
