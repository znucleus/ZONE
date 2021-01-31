package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.attend.AttendReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendReleaseService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.campus.attend.release.AttendReleaseAddVo;
import top.zbeboy.zbase.vo.campus.attend.release.AttendReleaseEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendReleaseApiController {

    @Resource
    private AttendReleaseService attendReleaseService;

    /**
     * 检查是否可分配权限
     *
     * @param id 发布id
     * @return true or false
     */
    @GetMapping("/api/campus/attend/authorize-condition/{id}")
    public ResponseEntity<Map<String, Object>> canAuthorize(@PathVariable("id") String id, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (attendReleaseService.canAuthorize(users.getUsername(), id)) {
            ajaxUtil.success().msg("可操作");
        } else {
            ajaxUtil.fail().msg("无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检查是否可操作分配权限
     *
     * @param targetUsername 被授权人
     * @return true or false
     */
    @GetMapping("/api/campus/attend/operator-authorize-condition/{targetUsername}")
    public ResponseEntity<Map<String, Object>> canOperatorAuthorize(@PathVariable("targetUsername") String targetUsername, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (attendReleaseService.canOperatorAuthorize(users.getUsername(), targetUsername)) {
            ajaxUtil.success().msg("可操作");
        } else {
            ajaxUtil.fail().msg("无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检查是否可统计
     *
     * @param id 发布id
     * @return true or false
     */
    @GetMapping("/api/campus/attend/review-condition/review/{id}")
    public ResponseEntity<Map<String, Object>> canReview(@PathVariable("id") String id, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (attendReleaseService.canReview(users.getUsername(), id)) {
            ajaxUtil.success().msg("可操作");
        } else {
            ajaxUtil.fail().msg("无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param attendReleaseAddVo 数据
     * @param principal          当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到发布保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/campus/attend/release/save")
    public ResponseEntity<Map<String, Object>> save(AttendReleaseAddVo attendReleaseAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendReleaseAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseService.save(attendReleaseAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到发布数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/campus/attend/release/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<AttendReleaseBean> ajaxUtil = attendReleaseService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param attendReleaseEditVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到发布更新", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/campus/attend/release/update")
    public ResponseEntity<Map<String, Object>> update(AttendReleaseEditVo attendReleaseEditVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseService.update(attendReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
