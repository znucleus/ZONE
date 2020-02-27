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
import top.zbeboy.zone.service.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zone.service.cache.attend.AttendWxCacheService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.data.WeiXinService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.HttpClientUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.weixin.AttendWxStudentSubscribeAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    private AttendWxCacheService attendWxCacheService;

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
                            weiXin.setOpenId(params.getString("openid"));
                            weiXin.setSessionKey(params.getString("session_key"));
                            weiXin.setUnionId(params.getString("unionid"));
                            weiXin.setResult(result);
                            weiXin.setResCode(resCode);
                            weiXin.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                            weiXinService.update(weiXin);
                        } else {
                            WeiXin weiXin = new WeiXin();
                            weiXin.setUsername(users.getUsername());
                            weiXin.setOpenId(params.getString("openid"));
                            weiXin.setSessionKey(params.getString("session_key"));
                            weiXin.setUnionId(params.getString("unionid"));
                            weiXin.setAppId(appId);
                            weiXin.setResult(result);
                            weiXin.setResCode(resCode);
                            weiXin.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

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
                        attendWxStudentSubscribe.setSubscribeId(UUIDUtil.getUUID());
                        attendWxStudentSubscribe.setTemplateId(attendWxStudentSubscribeAddVo.getTemplateId());
                        attendWxStudentSubscribe.setData(attendWxStudentSubscribeAddVo.getData());
                        attendWxStudentSubscribe.setLang(attendWxStudentSubscribeAddVo.getLang());
                        attendWxStudentSubscribe.setMiniProgramState(attendWxStudentSubscribeAddVo.getMiniProgramState());
                        attendWxStudentSubscribe.setPage(attendWxStudentSubscribeAddVo.getPage());
                        attendWxStudentSubscribe.setAttendReleaseId(attendWxStudentSubscribeAddVo.getAttendReleaseId());
                        attendWxStudentSubscribe.setStudentId(studentRecord.get().getStudentId());
                        attendWxStudentSubscribe.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

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
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/send")
    public ResponseEntity<Map<String, Object>> subscribeSend() {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 1.查询要下发的子表数据
        Result<Record> records = attendWxStudentSubscribeService.findSubscribe();
        // 2.存入缓存
        attendWxCacheService.saveAttendWxSubscribe(records);
        ajaxUtil.success().msg("下发成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 取消订阅
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/delete")
    public ResponseEntity<Map<String, Object>> subscribeDelete(@RequestParam("attendReleaseId") String attendReleaseId,
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

    /**
     * 订阅查询
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/query")
    public ResponseEntity<Map<String, Object>> subscribeQuery(@RequestParam("attendReleaseId") String attendReleaseId,
                                                              Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
            if (studentRecord.isPresent()) {
                Optional<AttendWxStudentSubscribeRecord> subRecord = attendWxStudentSubscribeService.findByAttendReleaseIdAndStudentId(
                        attendReleaseId, studentRecord.get().getStudentId());
                if (subRecord.isPresent()) {
                    ajaxUtil.success().msg("已订阅");
                } else {
                    ajaxUtil.fail().msg("未订阅");
                }
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }

        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
