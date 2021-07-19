package top.zbeboy.zone.api.system.notify;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SystemNotify;
import top.zbeboy.zbase.feign.notify.SystemNotifyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SystemNotifyApiController {

    @Resource
    private SystemNotifyService systemNotifyService;

    /**
     * 获取数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "系统通知", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/system/notify")
    public ResponseEntity<Map<String, Object>> userSystemNotify(Principal principal, HttpServletRequest request) {
        AjaxUtil<SystemNotify> ajaxUtil = AjaxUtil.of();
        List<SystemNotify> list = new ArrayList<>();
        Optional<List<SystemNotify>> optionalSystemNotifies = systemNotifyService.findByEffective();
        if (optionalSystemNotifies.isPresent()) {
            list = optionalSystemNotifies.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
