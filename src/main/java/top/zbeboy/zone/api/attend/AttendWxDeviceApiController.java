package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.domain.tables.records.WeiXinDeviceRecord;
import top.zbeboy.zone.service.data.WeiXinDeviceService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AttendWxDeviceApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private WeiXinDeviceService weiXinDeviceService;

    /**
     * 保存
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/device/save")
    public ResponseEntity<Map<String, Object>> save(String model, String version, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<WeiXinDeviceRecord> record = weiXinDeviceService.findByUsername(users.getUsername());
            if (record.isPresent()) {
                WeiXinDevice weiXinDevice = record.get().into(WeiXinDevice.class);
                weiXinDevice.setModel(model);
                weiXinDevice.setVersion(version);
                weiXinDevice.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                weiXinDeviceService.update(weiXinDevice);
            } else {
                WeiXinDevice weiXinDevice = new WeiXinDevice();
                weiXinDevice.setDeviceId(UUIDUtil.getUUID());
                weiXinDevice.setUsername(users.getUsername());
                weiXinDevice.setModel(model);
                weiXinDevice.setVersion(version);
                weiXinDevice.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                weiXinDeviceService.save(weiXinDevice);
            }
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg("查询用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/device/query")
    public ResponseEntity<Map<String, Object>> query(Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<WeiXinDeviceRecord> record = weiXinDeviceService.findByUsername(users.getUsername());
            if (record.isPresent()) {
                WeiXinDevice weiXinDevice = record.get().into(WeiXinDevice.class);
                ajaxUtil.success().msg("查询信息成功").put("device", weiXinDevice);
            } else {
                ajaxUtil.fail().msg("未查询到设备信息");
            }
        } else {
            ajaxUtil.fail().msg("查询用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
