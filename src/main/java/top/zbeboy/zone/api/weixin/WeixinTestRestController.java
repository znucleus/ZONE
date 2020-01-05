package top.zbeboy.zone.api.weixin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.web.util.AjaxUtil;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WeixinTestRestController {
    @GetMapping("/api/weixin/info")
    public ResponseEntity<Map<String, Object>> user() {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Map<String, Object> outPut = new HashMap<>();
        outPut.put("realName", "test");
        ajaxUtil.success().msg("获取信息成功").map(outPut);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
