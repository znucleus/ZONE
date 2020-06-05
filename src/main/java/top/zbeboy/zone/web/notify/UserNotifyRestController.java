package top.zbeboy.zone.web.notify;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.*;

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
        AjaxUtil<UserNotifyBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setSearch("acceptUser", users.getUsername());
        List<UserNotifyBean> userNotifies = new ArrayList<>();
        Result<Record> records = userNotifyService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            userNotifies = records.into(UserNotifyBean.class);
            userNotifies.forEach(userNotify -> userNotify.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(userNotify.getCreateDate())));
        }
        simplePaginationUtil.setTotalSize(userNotifyService.countAll(simplePaginationUtil));

        int readNum = 0;
        int unReadNum = 0;
        JSONObject search = simplePaginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String isSee = StringUtils.trim(search.getString("isSee"));
            // 是否需要统计，前台不需要统计
            String needCount = StringUtils.trim(search.getString("needCount"));
            if (StringUtils.equals("1", needCount)) {
                if (StringUtils.equals("1", isSee)) {
                    readNum = simplePaginationUtil.getTotalSize();
                    unReadNum = userNotifyService.countByAcceptUserAndIsSee(users.getUsername(), BooleanUtil.toByte(false));
                } else if (StringUtils.equals("0", isSee)) {
                    unReadNum = simplePaginationUtil.getTotalSize();
                    readNum = userNotifyService.countByAcceptUserAndIsSee(users.getUsername(), BooleanUtil.toByte(true));
                }
            }
        }
        ajaxUtil.success().list(userNotifies).page(simplePaginationUtil).msg("获取数据成功")
                .put("readNum", readNum).put("unReadNum", unReadNum);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
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
     * 批量更新状态为已读
     *
     * @param userNotifyIds ids
     * @return 是否成功
     */
    @PostMapping("/users/notify/reads")
    public ResponseEntity<Map<String, Object>> userNotifyReads(@RequestParam("userNotifyId") String userNotifyIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        String[] ids = userNotifyIds.split(",");
        for (String id : ids) {
            // 防止其它人获取别人的私人通知
            Optional<Record> record = userNotifyService.findByIdAndAcceptUserRelation(id, users.getUsername());
            if (record.isPresent()) {
                UserNotify userNotify = record.get().into(UserNotify.class);
                Byte isSee = userNotify.getIsSee();
                if (Objects.nonNull(isSee) && !BooleanUtil.toBoolean(isSee)) {
                    userNotify.setIsSee(BooleanUtil.toByte(true));
                    userNotifyService.update(userNotify);
                }
            }
        }
        ajaxUtil.success().msg("更新成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
