package top.zbeboy.zone.web.notify;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class UserNotifyRestController {

    @Resource
    private UserNotifyService userNotifyService;

    @Resource
    private UsersService usersService;

    /**
     * 获取用户通知数据
     *
     * @param simplePaginationUtil 分页工具
     * @return 数据
     */
    @GetMapping("/user/data/notify")
    public ResponseEntity<Map<String, Object>> userDataNotify(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<UserNotifyBean> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        simplePaginationUtil.setSearch("acceptUser", users.getUsername());
        List<UserNotifyBean> userNotifies = new ArrayList<>();
        Result<Record> records = userNotifyService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            userNotifies = records.into(UserNotifyBean.class);
            userNotifies.forEach(userNotify -> userNotify.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(userNotify.getCreateDate())));
        }
        simplePaginationUtil.setTotalSize(userNotifyService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(userNotifies).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取通知详情
     *
     * @param userNotifyId 通知id
     * @return 数据
     */
    @GetMapping("/user/notify/detail/{userNotifyId}")
    public ResponseEntity<Map<String, Object>> userNotifyDetail(@PathVariable("userNotifyId") String userNotifyId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        // 防止其它人获取别人的私人通知
        Optional<Record> record = userNotifyService.findByIdAndAcceptUserRelation(userNotifyId, users.getUsername());
        UserNotifyBean userNotify = new UserNotifyBean();
        if (record.isPresent()) {
            userNotify = record.get().into(UserNotifyBean.class);
            userNotify.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(userNotify.getCreateDate()));
        }
        ajaxUtil.success().put("userNotify", userNotify).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新状态为已读
     *
     * @param userNotifyId id
     * @return 是否成功
     */
    @PostMapping("/user/notify/read/{userNotifyId}")
    public ResponseEntity<Map<String, Object>> userNotifyRead(@PathVariable("userNotifyId") String userNotifyId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        // 防止其它人获取别人的私人通知
        Optional<Record> record = userNotifyService.findByIdAndAcceptUserRelation(userNotifyId, users.getUsername());
        if (record.isPresent()) {
            UserNotify userNotify = record.get().into(UserNotify.class);
            Byte isSee = userNotify.getIsSee();
            if(Objects.nonNull(isSee) && !BooleanUtil.toBoolean(isSee)){
                userNotify.setIsSee(BooleanUtil.toByte(true));
                userNotifyService.update(userNotify);
            }
        }
        ajaxUtil.success().msg("更新成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
