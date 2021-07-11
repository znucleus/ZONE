package top.zbeboy.zone.web.educational.examination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.ExaminationNoticeDetail;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.*;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
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

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

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
                    ajaxUtil = educationalExaminationService.save(beans);
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
     * 删除
     *
     * @param id 发布id
     * @return true or false
     */
    @PostMapping("/web/educational/examination/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.delete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
