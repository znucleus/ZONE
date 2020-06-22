package top.zbeboy.zone.api.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.SystemNotify;
import top.zbeboy.zbase.feign.notify.SystemNotifyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class SystemNotifyApiController {

    @Resource
    private SystemNotifyService systemNotifyService;

    /**
     * 获取数据
     *
     * @return 数据
     */
    @GetMapping("/api/system/notify")
    public ResponseEntity<Map<String, Object>> userSystemNotify() {
        AjaxUtil<SystemNotify> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(systemNotifyService.findByEffective()).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
