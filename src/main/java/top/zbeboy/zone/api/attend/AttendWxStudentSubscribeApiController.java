package top.zbeboy.zone.api.attend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.vo.attend.weixin.AttendWxStudentSubscribeAddVo;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendWxStudentSubscribeApiController {

    private final Logger log = LoggerFactory.getLogger(AttendWxStudentSubscribeApiController.class);

    @Resource
    private AttendWxStudentSubscribeService attendWxStudentSubscribeService;

    /**
     * 保存
     *
     * @param resCode   code
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("resCode") String resCode,
                                                    @RequestParam("appId") String appId,
                                                    Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromOauth(principal);
            ajaxUtil = attendWxStudentSubscribeService.save(resCode, appId, users.getUsername());
        } catch (IOException e) {
            log.error("获取微信用户信息异常：{}", e);
            ajaxUtil.fail().msg("保存异常，" + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 订阅
     *
     * @param attendWxStudentSubscribeAddVo 数据
     * @param principal                     当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(AttendWxStudentSubscribeAddVo attendWxStudentSubscribeAddVo,
                                                         Principal principal) {
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
    @GetMapping("/api/attend/weixin/subscribe_cache/save")
    public ResponseEntity<Map<String, Object>> subscribeSend() {
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribeCache();
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 取消订阅
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/delete")
    public ResponseEntity<Map<String, Object>> subscribeDelete(@RequestParam("attendReleaseId") String attendReleaseId,
                                                               Principal principal) {
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
    @PostMapping("/api/attend/weixin/subscribe/query")
    public ResponseEntity<Map<String, Object>> subscribeQuery(@RequestParam("attendReleaseId") String attendReleaseId,
                                                              Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxStudentSubscribeService.subscribeQuery(attendReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
