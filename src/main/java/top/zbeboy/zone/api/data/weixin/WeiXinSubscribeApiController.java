package top.zbeboy.zone.api.data.weixin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.WeiXin;
import top.zbeboy.zbase.domain.tables.pojos.WeiXinSubscribe;
import top.zbeboy.zbase.feign.data.WeiXinService;
import top.zbeboy.zbase.feign.data.WeiXinSubscribeService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.data.weixin.WeiXinSubscribeAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class WeiXinSubscribeApiController {

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    @Resource
    private WeiXinService weiXinService;

    /**
     * 订阅
     *
     * @param weiXinSubscribeAddVo 订阅数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "微信小程序订阅", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/data/wei-xin-subscribe/save")
    public ResponseEntity<Map<String, Object>> subscribe(WeiXinSubscribeAddVo weiXinSubscribeAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        weiXinSubscribeAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = weiXinSubscribeService.subscribe(weiXinSubscribeAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 订阅查询
     *
     * @param business  业务编码
     * @param appId     appId
     * @param principal 用户
     * @param request   请求
     * @return 是否订阅
     */
    @ApiLoggingRecord(remark = "微信小程序订阅查询", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/data/wei-xin-subscribe/query")
    public ResponseEntity<Map<String, Object>> query(@RequestParam("business") String business,
                                                     @RequestParam("appId") String appId,
                                                     Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        Optional<WeiXin> optionalWeiXin = weiXinService.findByUsernameAndAppId(users.getUsername(), appId);
        if (optionalWeiXin.isPresent()) {
            WeiXin weiXin = optionalWeiXin.get();
            Optional<List<WeiXinSubscribe>> optionalWeiXinSubscribes = weiXinSubscribeService.findSubscribeByTouserAndBusiness(weiXin.getOpenId(), business);
            if (optionalWeiXinSubscribes.isPresent()) {
                ajaxUtil.success().msg("已订阅");
            } else {
                ajaxUtil.fail().msg("未订阅");
            }
        } else {
            ajaxUtil.fail().msg("未查询到微信信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
