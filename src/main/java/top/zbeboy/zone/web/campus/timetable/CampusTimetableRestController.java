package top.zbeboy.zone.web.campus.timetable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.timetable.CampusCourseReleaseService;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;
import top.zbeboy.zbase.vo.campus.roster.RosterReleaseEditVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseReleaseAddVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseReleaseEditVo;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CampusTimetableRestController {

    @Resource
    private CampusCourseReleaseService campusCourseReleaseService;

    /**
     * 通过主键查询
     *
     * @return 数据
     */
    @GetMapping("/web/campus/timetable/release/{id}")
    public ResponseEntity<Map<String, Object>> release(@PathVariable("id") String id, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        CampusCourseRelease release = campusCourseReleaseService.findById(id);
        if(Objects.nonNull(release) && StringUtils.isNotBlank(release.getCampusCourseReleaseId())){
            ajaxUtil.success().msg("查询课表成功").put("release", release);
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
        List<CampusCourseRelease> campusCourseReleases = campusCourseReleaseService.findByUsername(users.getUsername());
        campusCourseReleases.forEach(release -> select2Data.add(release.getCampusCourseReleaseId(), release.getTitle()));
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
            ajaxUtil = campusCourseReleaseService.save(campusCourseReleaseAddVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = campusCourseReleaseService.update(campusCourseReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
