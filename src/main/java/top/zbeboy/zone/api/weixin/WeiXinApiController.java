package top.zbeboy.zone.api.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.domain.tables.pojos.WeiXinSubscribe;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.domain.tables.records.WeiXinRecord;
import top.zbeboy.zone.domain.tables.records.WeiXinSubscribeRecord;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.service.cache.weixin.WeiXinCacheService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.data.WeiXinService;
import top.zbeboy.zone.service.data.WeiXinSubscribeService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.HttpClientUtil;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.data.weixin.WeiXinSubscribeAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
public class WeiXinApiController {

    private final Logger log = LoggerFactory.getLogger(WeiXinApiController.class);

    @Autowired
    private ZoneProperties zoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private WeiXinService weiXinService;

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    @Resource
    private WeiXinCacheService weiXinCacheService;

    @Resource
    private AttendUsersService attendUsersService;

    @Resource
    private StudentService studentService;

    /**
     * 保存
     *
     * @param resCode   code
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/weixin/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("resCode") String resCode, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("appid", zoneProperties.getWeiXin().getAppId());
            map.put("secret", zoneProperties.getWeiXin().getSecret());
            map.put("js_code", resCode);
            map.put("grant_type", "authorization_code");
            String result = HttpClientUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", map);
            if (StringUtils.isNotBlank(result)) {
                JSONObject params = JSON.parseObject(result);
                String errcode = params.getString("errcode");
                if (StringUtils.equals("0", errcode)) {
                    Users users = usersService.getUserFromOauth(principal);
                    if (Objects.nonNull(users)) {
                        Optional<WeiXinRecord> record = weiXinService.findByUsername(users.getUsername());
                        if (record.isPresent()) {
                            WeiXin weiXin = record.get().into(WeiXin.class);
                            weiXin.setUsername(users.getUsername());
                            weiXin.setOpenId(params.getString("openid"));
                            weiXin.setSessionKey(params.getString("session_key"));
                            weiXin.setUnionId(params.getString("unionid"));

                            weiXinService.update(weiXin);
                        } else {
                            WeiXin weiXin = new WeiXin();
                            weiXin.setUsername(users.getUsername());
                            weiXin.setOpenId(params.getString("openid"));
                            weiXin.setSessionKey(params.getString("session_key"));
                            weiXin.setUnionId(params.getString("unionid"));

                            weiXinService.save(weiXin);
                        }

                        ajaxUtil.success().msg("保存成功");

                    } else {
                        ajaxUtil.fail().msg("查询用户信息失败");
                    }
                } else {
                    ajaxUtil.fail().msg("获取失败，code：" + errcode + " msg：" + params.getString("errmsg"));
                }
            } else {
                ajaxUtil.fail().msg("获取信息失败，响应内容为空");
            }
        } catch (IOException e) {
            log.error("获取微信用户信息异常：{}", e);
            ajaxUtil.fail().msg("发送请求异常，" + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 订阅
     *
     * @param weiXinSubscribeAddVo 数据
     * @param bindingResult        校验
     * @param principal            当前用户信息
     * @return true or false
     */
    @PostMapping("/api/weixin/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@Valid WeiXinSubscribeAddVo weiXinSubscribeAddVo, BindingResult bindingResult,
                                                         Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                Optional<WeiXinSubscribeRecord> record = weiXinSubscribeService.findByUsernameAndTemplateId(users.getUsername(), weiXinSubscribeAddVo.getTemplateId());
                if (!record.isPresent()) {
                    WeiXinSubscribe weiXinSubscribe = new WeiXinSubscribe();
                    weiXinSubscribe.setUsername(users.getUsername());
                    weiXinSubscribe.setTemplateId(weiXinSubscribeAddVo.getTemplateId());
                    weiXinSubscribe.setData(weiXinSubscribeAddVo.getData());
                    weiXinSubscribe.setLang(weiXinSubscribeAddVo.getLang());
                    weiXinSubscribe.setMiniProgramState(weiXinSubscribeAddVo.getMiniProgramState());
                    weiXinSubscribe.setPage(weiXinSubscribeAddVo.getPage());
                    weiXinSubscribe.setType(weiXinSubscribeAddVo.getType());

                    weiXinSubscribeService.save(weiXinSubscribe);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("已订阅");
                }
            } else {
                ajaxUtil.fail().msg("查询用户信息失败");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 发送
     *
     * @param templateId 模板id
     * @param principal  当前用户信息
     * @return true or false
     */
    @PostMapping("/api/weixin/subscribe/send")
    public ResponseEntity<Map<String, Object>> subscribeSend(@RequestParam("templateId") String templateId, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = usersService.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                final String accessToken = weiXinCacheService.getAccessToken();
                Optional<WeiXinRecord> record = weiXinService.findByUsername(users.getUsername());
                if (record.isPresent()) {
                    WeiXin weiXin = record.get().into(WeiXin.class);
                    Optional<WeiXinSubscribeRecord> subRecord = weiXinSubscribeService.findByUsernameAndTemplateId(users.getUsername(), templateId);
                    if (subRecord.isPresent()) {
                        WeiXinSubscribe weiXinSubscribe = subRecord.get().into(WeiXinSubscribe.class);
                        Map<String, String> map = new HashMap<>();
                        map.put("access_token", accessToken);
                        map.put("touser", weiXin.getOpenId());
                        map.put("template_id", weiXinSubscribe.getTemplateId());
                        map.put("page", weiXinSubscribe.getPage());
                        map.put("miniprogram_state", weiXinSubscribe.getMiniProgramState());
                        map.put("lang", weiXinSubscribe.getLang());
                        if (StringUtils.isNotBlank(weiXinSubscribe.getData())) {
                            map.put("data", weiXinSubscribe.getData());
                            HttpClientUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/subscribe/send", map);
                            ajaxUtil.success().msg("发送成功");
                        } else {
                            if (StringUtils.equals(weiXinSubscribe.getType(), Workbook.weiXinTemplateType.ATTEND.name())) {
                                Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
                                if (studentRecord.isPresent()) {
                                    Result<Record> attendRecords = attendUsersService.findFutureAttendByStudentId(studentRecord.get().getStudentId());
                                    if (attendRecords.isNotEmpty()) {
                                        String mes = "{\"phrase1\":{\"value\":\"[phrase1]\"},\"name2\":{\"value\":\"[name2]\"},\"date3\":{\"value\":\"[date3]\"},\"thing5\":{\"value\":\"[thing5]\"}}";
                                        List<AttendReleaseSubBean> beans = attendRecords.into(AttendReleaseSubBean.class);
                                        for (AttendReleaseSubBean bean : beans) {
                                            mes = mes.replaceAll("[phrase1]", "待签到");
                                            mes = mes.replaceAll("[name2]", bean.getRealName());
                                            mes = mes.replaceAll("[date3]", DateTimeUtil.formatSqlTimestamp(bean.getAttendStartTime(), DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                                            mes = mes.replaceAll("[thing5]", "校内");

                                            map.put("data", mes);

                                            HttpClientUtil.sendPost("https://api.weixin.qq.com/cgi-bin/message/subscribe/send", map);
                                        }
                                        ajaxUtil.success().msg("发送成功");
                                    } else {
                                        ajaxUtil.fail().msg("无签到数据");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("未查询到学生数据");
                                }
                            } else {
                                ajaxUtil.fail().msg("不支持的模板类型");
                            }
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到模板信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到微信用户信息");
                }
            } else {
                ajaxUtil.fail().msg("获取用户信息失败");
            }

        } catch (Exception e) {
            log.error("发送微信订阅异常：{}", e);
            ajaxUtil.fail().msg("发送微信订阅异常：" + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
