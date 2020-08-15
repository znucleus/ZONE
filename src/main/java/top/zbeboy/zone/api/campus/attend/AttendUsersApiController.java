package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.attend.AttendUsersBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendUsersService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.vo.campus.attend.users.AttendUsersAddVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendUsersApiController {

    @Resource
    private AttendUsersService attendUsersService;

    /**
     * 获取签到名单数据
     *
     * @param attendReleaseSubId 签到子表ID
     * @param type               数据类型 1:已签到 2:未签到 0:全部
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园签到名单数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/attend/users/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("attendReleaseSubId") int attendReleaseSubId,
                                                    int type, Principal principal, HttpServletRequest request) {
        AjaxUtil<AttendUsersBean> ajaxUtil = attendUsersService.data(attendReleaseSubId, type);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param attendUsersAddVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到名单保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/users/save")
    public ResponseEntity<Map<String, Object>> save(AttendUsersAddVo attendUsersAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendUsersAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendUsersService.save(attendUsersAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除人员
     *
     * @param attendUsersId 名单ID
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到名单删除", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/users/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("attendUsersId") String attendUsersId, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendUsersService.delete(attendUsersId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 名单重置
     *
     * @param attendReleaseId 发布表ID
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到名单重置", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/users/reset")
    public ResponseEntity<Map<String, Object>> reset(@RequestParam("attendReleaseId") String attendReleaseId, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendUsersService.reset(attendReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param attendUsersId 名单ID
     * @param remark        备注
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到名单备注", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/users/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("attendUsersId") String attendUsersId, String remark, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendUsersService.remark(attendUsersId, remark, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
