package top.zbeboy.zone.web.campus.timetable;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
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
import top.zbeboy.zone.service.campus.CampusTimetableEduService;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class CampusTimetableRestController {

    @Resource
    private CampusTimetableService campusTimetableService;

    @Resource
    private CampusTimetableEduService campusTimetableEduService;

    /**
     * 通过主键查询
     *
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/release/{id}")
    public ResponseEntity<Map<String, Object>> release(@PathVariable("id") String id, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(id);
        if (optionalCampusCourseRelease.isPresent()) {
            ajaxUtil.success().msg("查询课表成功").put("release", optionalCampusCourseRelease.get());
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
        if(optionalCampusCourseReleases.isPresent()){
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
                campusCourseReleaseAddVo.setStartYear(release.getStartYear());
                campusCourseReleaseAddVo.setEndYear(release.getEndYear());
                campusCourseReleaseAddVo.setTerm(release.getTerm());
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
        if(optionalCampusCourseData.isPresent()){
            ajaxUtil.success().msg("获取数据成功").list(optionalCampusCourseData.get()).put("weekDay", DateTimeUtil.getNowDayOfWeek());
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
            if(optionalCampusCourseData.isPresent()){
                ajaxUtil.success().msg("获取数据成功").list(optionalCampusCourseData.get())
                        .put("release", release)
                        .put("weekDay", DateTimeUtil.getNowDayOfWeek());
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
     * 新教务系统数据
     *
     * @param username 账号
     * @param password 密码
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/course/new-edu/data")
    public ResponseEntity<Map<String, Object>> courseNewEduData(@RequestParam("username") String username, @RequestParam("password") String password) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try{
            Map<String, Object> result = campusTimetableEduService.data(username, password);
            Boolean hasError = (Boolean) result.get("hasError");
            if(!hasError){
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("data");
                ajaxUtil.success().msg("获取数据成功").list(list);
            } else {
                String statusCode = (String) result.get("statusCode");
                String reasonPhrase = (String) result.get("reasonPhrase");
                if(StringUtils.contains(reasonPhrase, "【LOGIN_FAIL】")){
                    ajaxUtil.fail().msg("用户名或密码错误");
                } else {
                    ajaxUtil.fail().msg("登录失败，请稍后重试或联系管理员，错误：" + statusCode + " error:" + reasonPhrase);
                }
            }
        } catch (Exception e){
            ajaxUtil.fail().msg("登录异常，error: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
