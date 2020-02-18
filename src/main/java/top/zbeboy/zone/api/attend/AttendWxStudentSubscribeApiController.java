package top.zbeboy.zone.api.attend;

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
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.AttendWxStudentSubscribe;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.domain.tables.records.AttendWxStudentSubscribeRecord;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.domain.tables.records.WeiXinRecord;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.service.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zone.service.cache.weixin.WeiXinCacheService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.data.WeiXinService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.HttpClientUtil;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.weixin.AttendWxStudentSubscribeAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
public class AttendWxStudentSubscribeApiController {

    private final Logger log = LoggerFactory.getLogger(AttendWxStudentSubscribeApiController.class);

    @Autowired
    private ZoneProperties zoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private WeiXinService weiXinService;

    @Resource
    private AttendWxStudentSubscribeService attendWxStudentSubscribeService;

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
    @PostMapping("/api/attend/weixin/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("resCode") String resCode, @RequestParam("appId") String appId,
                                                    Principal principal) {
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
                String openId = params.getString("openid");
                if (StringUtils.isNotBlank(openId)) {
                    Users users = usersService.getUserFromOauth(principal);
                    if (Objects.nonNull(users)) {
                        Optional<WeiXinRecord> record = weiXinService.findByUsernameAndAppId(users.getUsername(), appId);
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
                            weiXin.setAppId(appId);

                            weiXinService.save(weiXin);
                        }

                        ajaxUtil.success().msg("保存成功");

                    } else {
                        ajaxUtil.fail().msg("查询用户信息失败");
                    }
                } else {
                    ajaxUtil.fail().msg("获取OPEN_ID失败");
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
     * @param attendWxStudentSubscribeAddVo 数据
     * @param bindingResult                 校验
     * @param principal                     当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@Valid AttendWxStudentSubscribeAddVo attendWxStudentSubscribeAddVo, BindingResult bindingResult,
                                                         Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
                if (studentRecord.isPresent()) {
                    Optional<AttendWxStudentSubscribeRecord> record = attendWxStudentSubscribeService.findByAttendReleaseIdAndStudentId(
                            attendWxStudentSubscribeAddVo.getAttendReleaseId(), studentRecord.get().getStudentId());
                    if (!record.isPresent()) {
                        AttendWxStudentSubscribe attendWxStudentSubscribe = new AttendWxStudentSubscribe();
                        attendWxStudentSubscribe.setTemplateId(attendWxStudentSubscribeAddVo.getTemplateId());
                        attendWxStudentSubscribe.setData(attendWxStudentSubscribeAddVo.getData());
                        attendWxStudentSubscribe.setLang(attendWxStudentSubscribeAddVo.getLang());
                        attendWxStudentSubscribe.setMiniProgramState(attendWxStudentSubscribeAddVo.getMiniProgramState());
                        attendWxStudentSubscribe.setPage(attendWxStudentSubscribeAddVo.getPage());
                        attendWxStudentSubscribe.setAttendReleaseId(attendWxStudentSubscribeAddVo.getAttendReleaseId());
                        attendWxStudentSubscribe.setStudentId(studentRecord.get().getStudentId());

                        attendWxStudentSubscribeService.save(attendWxStudentSubscribe);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("已订阅");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到学生信息");
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
     * @param principal  当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/send")
    public ResponseEntity<Map<String, Object>> subscribeSend(@RequestParam("appId") String appId,
                                                             @RequestParam("attendReleaseId") String attendReleaseId,
                                                             Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = usersService.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
                if (studentRecord.isPresent()) {
                    final String accessToken = weiXinCacheService.getAccessToken();
                    Optional<WeiXinRecord> record = weiXinService.findByUsernameAndAppId(users.getUsername(), appId);
                    if (record.isPresent()) {
                        WeiXin weiXin = record.get().into(WeiXin.class);
                        Optional<AttendWxStudentSubscribeRecord> subRecord = attendWxStudentSubscribeService.findByAttendReleaseIdAndStudentId(
                                attendReleaseId, studentRecord.get().getStudentId());
                        if (subRecord.isPresent()) {
                            AttendWxStudentSubscribe attendWxStudentSubscribe = subRecord.get().into(AttendWxStudentSubscribe.class);
                            Map<String, Object> map = new HashMap<>();
                            map.put("touser", weiXin.getOpenId());
                            map.put("template_id", attendWxStudentSubscribe.getTemplateId());
                            map.put("page", attendWxStudentSubscribe.getPage());
                            map.put("miniprogram_state", attendWxStudentSubscribe.getMiniProgramState());
                            map.put("lang", attendWxStudentSubscribe.getLang());
                            Result<Record> attendRecords = attendUsersService.findFutureAttendByStudentId(studentRecord.get().getStudentId());
                            if (attendRecords.isNotEmpty()) {
                                List<AttendReleaseSubBean> beans = attendRecords.into(AttendReleaseSubBean.class);
                                for (AttendReleaseSubBean bean : beans) {
                                    Map<String, Object> data = new HashMap<>();

                                    Map<String, Object> phrase1 = new HashMap<>();
                                    phrase1.put("value", "待签到");
                                    data.put("phrase1", phrase1);

                                    Map<String, Object> name2 = new HashMap<>();
                                    name2.put("value", bean.getRealName());
                                    data.put("name2", name2);

                                    Map<String, Object> date3 = new HashMap<>();
                                    date3.put("value", DateTimeUtil.formatSqlTimestamp(DateTimeUtil.getNowSqlTimestamp(), DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                                    data.put("date3", date3);

                                    Map<String, Object> thing5 = new HashMap<>();
                                    thing5.put("value", bean.getSchoolName() + "-" + bean.getCollegeName());
                                    data.put("thing5", thing5);

                                    map.put("data", data);
                                    String json = JSON.toJSONString(map);

                                    HttpClientUtil.sendJsonPost("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, json);
                                }
                                ajaxUtil.success().msg("发送成功");
                            } else {
                                ajaxUtil.fail().msg("无签到数据");
                            }
                        } else {
                            ajaxUtil.fail().msg("未查询到模板信息");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到微信用户信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到学生信息");
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

    /**
     * 取消订阅
     *
     * @param templateId 模板id
     * @param principal  当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/delete")
    public ResponseEntity<Map<String, Object>> subscribeDelete(@RequestParam("templateId") String templateId,
                                                               @RequestParam("attendReleaseId") String attendReleaseId,
                                                               Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
            if (studentRecord.isPresent()) {
                attendWxStudentSubscribeService.deleteByAttendReleaseIdAndStudentId(attendReleaseId, studentRecord.get().getStudentId());
                ajaxUtil.success().msg("取消订阅成功");
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }

        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
