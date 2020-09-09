package top.zbeboy.zone.web.notify;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.notify.UserNotifyBean;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.notify.UserNotifyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UserNotifyRestController {

    @Resource
    private UserNotifyService userNotifyService;

    /**
     * 获取用户通知数据
     *
     * @param simplePaginationUtil 分页工具
     * @return 数据
     */
    @GetMapping("/users/data/notify")
    public ResponseEntity<Map<String, Object>> userDataNotify(SimplePaginationUtil simplePaginationUtil) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<UserNotifyBean> ajaxUtil = userNotifyService.userDataNotify(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取通知详情
     *
     * @param userNotifyId 通知id
     * @return 数据
     */
    @GetMapping("/users/notify/detail/{userNotifyId}")
    public ResponseEntity<Map<String, Object>> userNotifyDetail(@PathVariable("userNotifyId") String userNotifyId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = userNotifyService.userNotifyDetail(users.getUsername(), userNotifyId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更新状态为已读
     *
     * @param userNotifyIds ids
     * @return 是否成功
     */
    @PostMapping("/users/notify/reads")
    public ResponseEntity<Map<String, Object>> userNotifyReads(@RequestParam("userNotifyId") String userNotifyIds) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = userNotifyService.userNotifyReads(users.getUsername(), userNotifyIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
