package top.zbeboy.zone.api.system;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.SystemNotify;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;
import top.zbeboy.zone.service.notify.SystemNotifyService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
        List<SystemNotify> systemNotifies = new ArrayList<>();
        Result<SystemNotifyRecord> records = systemNotifyService.findByEffective();
        if (records.isNotEmpty()) {
            systemNotifies = records.into(SystemNotify.class);
        }
        ajaxUtil.success().list(systemNotifies).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
