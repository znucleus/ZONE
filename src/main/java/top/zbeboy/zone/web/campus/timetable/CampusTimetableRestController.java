package top.zbeboy.zone.web.campus.timetable;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.timetable.CampusTimetableService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseDataAddVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseDataEditVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseReleaseAddVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseReleaseEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.campus.CampusTimetableEduService;
import top.zbeboy.zone.service.educational.TimetableService;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class CampusTimetableRestController {

    private final Logger log = LoggerFactory.getLogger(CampusTimetableRestController.class);

    @Resource
    private CampusTimetableService campusTimetableService;

    @Resource
    private CampusTimetableEduService campusTimetableEduService;

    @Resource
    private TimetableService timetableService;

    /**
     * 通过主键查询
     *
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/release/{id}")
    public ResponseEntity<Map<String, Object>> release(@PathVariable("id") String id, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(id);
        if (optionalCampusCourseRelease.isPresent()) {
            CampusCourseRelease campusCourseRelease = optionalCampusCourseRelease.get();
            Map<String, Object> map = new HashMap<>();
            map.put("startDate", campusCourseRelease.getStartDate());
            map.put("endDate", campusCourseRelease.getEndDate());
            map.put("totalWeeks", DateTimeUtil.calculationTwoDateDifferWeeks(campusCourseRelease.getStartDate(), campusCourseRelease.getEndDate()));
            map.put("week", DateTimeUtil.getNowDayOfWeek());
            if (DateTimeUtil.nowRangeSqlDate(campusCourseRelease.getStartDate(), campusCourseRelease.getEndDate())) {
                map.put("curWeeks", DateTimeUtil.calculationTwoDateDifferWeeks(campusCourseRelease.getStartDate(), DateTimeUtil.getNowSqlDate()));
            } else {
                map.put("curWeeks", "0");
            }
            map.put("schoolYear", campusCourseRelease.getSchoolYear());
            map.put("semester", campusCourseRelease.getSemester());
            map.put("campusCourseReleaseId", campusCourseRelease.getCampusCourseReleaseId());
            map.put("shareNumber", campusCourseRelease.getShareNumber());
            map.put("qrCodeUrl", campusCourseRelease.getQrCodeUrl());
            map.put("publisher", campusCourseRelease.getPublisher());
            ajaxUtil.success().msg("查询课表成功").map(map);
        } else {
            ajaxUtil.fail().msg("未查询到数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/releases")
    public ResponseEntity<Map<String, Object>> releases() {
        Users users = SessionUtil.getUserFromSession();
        Select2Data select2Data = Select2Data.of();
        Optional<List<CampusCourseRelease>> optionalCampusCourseReleases = campusTimetableService.findByUsername(users.getUsername());
        if (optionalCampusCourseReleases.isPresent()) {
            List<CampusCourseRelease> campusCourseReleases = optionalCampusCourseReleases.get();
            campusCourseReleases.forEach(release -> select2Data.add(release.getCampusCourseReleaseId(), release.getTitle()));
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param campusCourseReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/save")
    public ResponseEntity<Map<String, Object>> save(CampusCourseReleaseAddVo campusCourseReleaseAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            campusCourseReleaseAddVo.setUsername(users.getUsername());
            String id = UUIDUtil.getUUID();
            campusCourseReleaseAddVo.setCampusCourseReleaseId(id);
            String realPath = RequestUtil.getRealPath(request);
            String path = Workbook.campusTimetableQrCodeFilePath() + id + ".jpg";
            String logoPath = Workbook.SYSTEM_LOGO_PATH;
            //生成二维码
            String text = RequestUtil.getBaseUrl(request) + CampusUrlCommon.ANYONE_TIMETABLE_LOOK_URL + id;
            QRCodeUtil.encode(text, logoPath, realPath + path, true);
            campusCourseReleaseAddVo.setQrCodeUrl(path);
            ajaxUtil = campusTimetableService.save(campusCourseReleaseAddVo);
        } catch (Exception e) {
            ajaxUtil.fail().msg("保存失败: 异常: " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param shareId 共享id
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/share/save")
    public ResponseEntity<Map<String, Object>> shareSave(@RequestParam("shareId") String shareId, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(shareId);
            if (optionalCampusCourseRelease.isPresent()) {
                CampusCourseRelease release = optionalCampusCourseRelease.get();
                CampusCourseReleaseAddVo campusCourseReleaseAddVo = new CampusCourseReleaseAddVo();
                Users users = SessionUtil.getUserFromSession();
                campusCourseReleaseAddVo.setUsername(users.getUsername());
                String id = UUIDUtil.getUUID();
                campusCourseReleaseAddVo.setCampusCourseReleaseId(id);
                String realPath = RequestUtil.getRealPath(request);
                String path = Workbook.campusTimetableQrCodeFilePath() + id + ".jpg";
                String logoPath = Workbook.SYSTEM_LOGO_PATH;
                //生成二维码
                String text = RequestUtil.getBaseUrl(request) + CampusUrlCommon.ANYONE_TIMETABLE_LOOK_URL + id;
                QRCodeUtil.encode(text, logoPath, realPath + path, true);
                campusCourseReleaseAddVo.setQrCodeUrl(path);

                campusCourseReleaseAddVo.setTitle(release.getTitle());
                campusCourseReleaseAddVo.setSchoolYear(release.getSchoolYear());
                campusCourseReleaseAddVo.setSemester(release.getSemester());
                campusCourseReleaseAddVo.setStartDate(DateTimeUtil.defaultFormatSqlDate(release.getStartDate()));
                campusCourseReleaseAddVo.setEndDate(DateTimeUtil.defaultFormatSqlDate(release.getEndDate()));
                campusCourseReleaseAddVo.setShareId(shareId);
                ajaxUtil = campusTimetableService.shareSave(campusCourseReleaseAddVo);
            } else {
                ajaxUtil.fail().msg("保存失败，根据共享ID未查询到课表信息");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("保存失败: 异常: " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param campusCourseReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/update")
    public ResponseEntity<Map<String, Object>> update(CampusCourseReleaseEditVo campusCourseReleaseEditVo) {
        Users users = SessionUtil.getUserFromSession();
        campusCourseReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = campusTimetableService.update(campusCourseReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 发布id
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("id") String id, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = campusTimetableService.delete(users.getUsername(), id);
        if (ajaxUtil.getState()) {
            // 删除文件
            String realPath = RequestUtil.getRealPath(request);
            String path = Workbook.campusTimetableQrCodeFilePath() + id + ".jpg";
            FilesUtil.deleteFile(realPath + path);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存课程
     *
     * @param campusCourseDataAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/course/save")
    public ResponseEntity<Map<String, Object>> courseSave(CampusCourseDataAddVo campusCourseDataAddVo) {
        Users users = SessionUtil.getUserFromSession();
        campusCourseDataAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = campusTimetableService.courseSave(campusCourseDataAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量保存课程
     *
     * @param data 数据
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/course/batch-save")
    public ResponseEntity<Map<String, Object>> courseBatchSave(@RequestParam("data") String data) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        List<CampusCourseDataAddVo> list = JSON.parseArray(data, CampusCourseDataAddVo.class);
        if (Objects.nonNull(list) && !list.isEmpty()) {
            list.forEach(campusCourseDataAddVo -> campusCourseDataAddVo.setUsername(users.getUsername()));
            ajaxUtil = campusTimetableService.courseBatchSave(list);
        } else {
            ajaxUtil.fail().msg("请选择课程");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @param campusCourseReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/courses/{campusCourseReleaseId}")
    public ResponseEntity<Map<String, Object>> courses(@PathVariable("campusCourseReleaseId") String campusCourseReleaseId) {
        AjaxUtil<CampusCourseData> ajaxUtil = AjaxUtil.of();
        Optional<List<CampusCourseData>> optionalCampusCourseData = campusTimetableService.findCourseByCampusCourseReleaseId(campusCourseReleaseId);
        if (optionalCampusCourseData.isPresent()) {
            ajaxUtil.success().msg("获取数据成功").list(optionalCampusCourseData.get());
        } else {
            ajaxUtil.fail().msg("获取数据失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 共享课程数据
     *
     * @param campusCourseReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/anyone/campus/timetable/share-courses/{campusCourseReleaseId}")
    public ResponseEntity<Map<String, Object>> shareCourses(@PathVariable("campusCourseReleaseId") String campusCourseReleaseId) {
        AjaxUtil<CampusCourseData> ajaxUtil = AjaxUtil.of();
        Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(campusCourseReleaseId);
        if (optionalCampusCourseRelease.isPresent()) {
            CampusCourseRelease release = optionalCampusCourseRelease.get();
            Optional<List<CampusCourseData>> optionalCampusCourseData = campusTimetableService.findCourseByCampusCourseReleaseId(campusCourseReleaseId);
            if (optionalCampusCourseData.isPresent()) {
                int curWeeks = 0;
                if (DateTimeUtil.nowRangeSqlDate(release.getStartDate(), release.getEndDate())) {
                    curWeeks = DateTimeUtil.calculationTwoDateDifferWeeks(release.getStartDate(), DateTimeUtil.getNowSqlDate());
                }
                ajaxUtil.success().msg("获取数据成功").list(optionalCampusCourseData.get())
                        .put("release", release)
                        .put("totalWeeks", DateTimeUtil.calculationTwoDateDifferWeeks(release.getStartDate(), release.getEndDate()))
                        .put("week", DateTimeUtil.getNowDayOfWeek())
                        .put("curWeeks", curWeeks);
            } else {
                ajaxUtil.fail().msg("未查询到课表数据");
            }
        } else {
            ajaxUtil.fail().msg("未查询到课表发布数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新课程
     *
     * @param campusCourseDataEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/course/update")
    public ResponseEntity<Map<String, Object>> courseUpdate(CampusCourseDataEditVo campusCourseDataEditVo) {
        Users users = SessionUtil.getUserFromSession();
        campusCourseDataEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = campusTimetableService.courseUpdate(campusCourseDataEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 课程id
     * @return true or false
     */
    @PostMapping("/web/campus/timetable/course/delete")
    public ResponseEntity<Map<String, Object>> courseDelete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = campusTimetableService.courseDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 新教务系统学年数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "新教务课表学期", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/campus/timetable/new-edu/semesters")
    public ResponseEntity<Map<String, Object>> semesters(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        try {
            List<Map<String, Object>> semesters = timetableService.semesters(username, password);
            semesters.forEach(s -> select2Data.add((String) s.get("id"), (String) s.get("name"), (Boolean) s.get("selected")));
        } catch (Exception e) {
            log.error("教务课表学期查询错误", e);
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 新教务系统数据
     *
     * @param username 账号
     * @param password 密码
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/course/new-edu/data")
    public ResponseEntity<Map<String, Object>> courseNewEduData(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("schoolYear") int schoolYear) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Map<String, Object> result = timetableService.data(username, password, schoolYear);
            Boolean hasError = (Boolean) result.get("hasError");
            if (!hasError) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
                if (CollectionUtils.isNotEmpty(data)) {
                    for (Map<String, Object> info : data) {
                        List<Map<String, Object>> courseData = (List<Map<String, Object>>) info.get("data");
                        ajaxUtil.success().msg("获取数据成功").list(courseData);
                    }
                } else {
                    ajaxUtil.fail().msg("未获取到数据");
                }
            } else {
                String statusCode = (String) result.get("statusCode");
                String reasonPhrase = (String) result.get("reasonPhrase");
                if (StringUtils.contains(reasonPhrase, "【LOGIN_FAIL】")) {
                    ajaxUtil.fail().msg("用户名或密码错误");
                } else {
                    ajaxUtil.fail().msg("登录失败，请稍后重试或联系管理员，错误：" + statusCode + " error:" + reasonPhrase);
                }
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("登录异常，error: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出数据为日历格式
     *
     * @param request 请求
     */
    @GetMapping("/web/campus/timetable/course/generate-ics")
    public ResponseEntity<Map<String, Object>> generateIcs(@RequestParam("campusCourseReleaseId") String campusCourseReleaseId,
                                                           HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Optional<List<CampusCourseData>> optionalCampusCourseDataList = campusTimetableService.findCourseByCampusCourseReleaseId(campusCourseReleaseId);
            if (optionalCampusCourseDataList.isPresent()) {
                List<CampusCourseData> campusCourseData = optionalCampusCourseDataList.get();
                String filePath = Workbook.campusTimetableIcsFilePath() + campusCourseReleaseId + ".ics";
                String path = RequestUtil.getRealPath(request) + filePath;
                campusTimetableEduService.generateIcs(campusCourseData, path);
                ajaxUtil.success().msg("生成成功").put("path", filePath);
            } else {
                ajaxUtil.fail().msg("无数据可生成");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("生成文件异常，error: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
