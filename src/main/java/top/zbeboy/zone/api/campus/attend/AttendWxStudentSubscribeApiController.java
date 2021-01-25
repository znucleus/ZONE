package top.zbeboy.zone.api.campus.attend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.campus.attend.weixin.AttendWxStudentSubscribeAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendWxStudentSubscribeApiController {

    private final Logger log = LoggerFactory.getLogger(AttendWxStudentSubscribeApiController.class);

    @Resource
    private AttendWxStudentSubscribeService attendWxStudentSubscribeService;



    /**
     * 订阅
     *
     * @param attendWxStudentSubscribeAddVo 数据
     * @param principal                     当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到微信订阅", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/weixin/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(AttendWxStudentSubscribeAddVo attendWxStudentSubscribeAddVo,
                                                         Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendWxStudentSubscribeAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribe(attendWxStudentSubscribeAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 订阅缓存
     *
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到微信订阅缓存保存", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/attend/weixin/subscribe_cache/save")
    public ResponseEntity<Map<String, Object>> subscribeSend(Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribeCache();
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 取消订阅
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到微信订阅删除", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/weixin/subscribe/delete")
    public ResponseEntity<Map<String, Object>> subscribeDelete(@RequestParam("attendReleaseId") String attendReleaseId,
                                                               Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribeDelete(attendReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 订阅查询
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到微信订阅查询", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/weixin/subscribe/query")
    public ResponseEntity<Map<String, Object>> subscribeQuery(@RequestParam("attendReleaseId") String attendReleaseId,
                                                              Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribeQuery(attendReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
