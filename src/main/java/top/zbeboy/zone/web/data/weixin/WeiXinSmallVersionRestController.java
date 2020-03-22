package top.zbeboy.zone.web.data.weixin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.WeiXinSmallVersion;
import top.zbeboy.zone.service.data.WeiXinSmallVersionService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@RestController
public class WeiXinSmallVersionRestController {

    @Resource
    private WeiXinSmallVersionService weiXinSmallVersionService;

    /**
     * 获取版本开关
     *
     * @return 获取版本开关
     */
    @GetMapping("/anyone/data/wxs_switch")
    public ResponseEntity<Map<String, Object>> anyoneData(@RequestParam("version") String version) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        WeiXinSmallVersion weiXinSmallVersion = weiXinSmallVersionService.findById(version);
        if (Objects.nonNull(weiXinSmallVersion)) {
            ajaxUtil.success().put("switch", weiXinSmallVersion.getSwitch()).msg("获取信息成功");
        } else {
            ajaxUtil.fail().msg("获取信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
