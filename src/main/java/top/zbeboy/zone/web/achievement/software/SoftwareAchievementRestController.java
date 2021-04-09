package top.zbeboy.zone.web.achievement.software;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.achievement.software.SoftwareAchievementBean;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SoftwareAchievement;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.achievement.software.SoftwareAchievementService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class SoftwareAchievementRestController {

    @Resource
    private SoftwareAchievementService softwareAchievementService;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, CookieStore> operations;

    /**
     * 获取验证码
     *
     * @param response 响应
     * @throws Exception 异常
     */
    @GetMapping("/web/achievement/software/query/captcha")
    public void captcha(HttpServletResponse response) throws Exception {
        Users users = SessionUtil.getUserFromSession();
        SoftwareAchievementHttpClient softwareAchievementHttpClient = new SoftwareAchievementHttpClient();
        String cacheKey = CacheBook.ACHIEVEMENT_SOFTWARE + users.getUsername();
        if (operations.getOperations().hasKey(cacheKey)) {
            softwareAchievementHttpClient.captcha(operations.get(cacheKey), response);
        }

    }

    /**
     * 获取考试时间
     *
     * @throws Exception 异常
     */
    @GetMapping("/web/achievement/software/query/exam-date")
    public ResponseEntity<Map<String, Object>> examDate() throws Exception {
        AjaxUtil<String> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        SoftwareAchievementHttpClient softwareAchievementHttpClient = new SoftwareAchievementHttpClient();
        List<String> list = softwareAchievementHttpClient.examDate();

        CookieStore cookieStore = softwareAchievementHttpClient.getCookieStore();
        if (Objects.nonNull(cookieStore)) {
            // 存入集合
            operations.set(
                    CacheBook.ACHIEVEMENT_SOFTWARE + users.getUsername(),
                    cookieStore,
                    CacheBook.EXPIRES_MINUTES,
                    TimeUnit.MINUTES
            );
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询成绩
     *
     * @param captcha    验证码
     * @param stage      考试时间
     * @param xm         姓名
     * @param zjhm       证件号码/准考证号
     * @param selectType 查询类型
     * @return 数据
     */
    @ApiLoggingRecord(remark = "软考成绩查询", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/achievement/software/query/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("captcha") String captcha,
                                                    @RequestParam("stage") String stage,
                                                    @RequestParam("xm") String xm,
                                                    @RequestParam("zjhm") String zjhm,
                                                    @RequestParam("selectType") String selectType,
                                                    HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            Map<String, String> param = new HashMap<>();
            param.put("captcha", captcha);
            param.put("stage", stage);
            param.put("xm", xm);
            param.put("zjhm", zjhm);
            param.put("select_type", selectType);
            // 获得对应key的对象
            SoftwareAchievementHttpClient softwareAchievementHttpClient = new SoftwareAchievementHttpClient();
            String cacheKey = CacheBook.ACHIEVEMENT_SOFTWARE + users.getUsername();
            if (operations.getOperations().hasKey(cacheKey)) {
                Map<String, Object> result = softwareAchievementHttpClient.verifyCaptcha(operations.get(cacheKey), param);
                Boolean hasError = (Boolean) result.get("hasError");
                if (!hasError) {
                    Map<String, Object> data = (Map<String, Object>) result.get("data");
                    SoftwareAchievement softwareAchievement = new SoftwareAchievement();
                    softwareAchievement.setExamDate((String) data.get("KSSJ"));
                    softwareAchievement.setQualificationName((String) data.get("ZGMC"));
                    softwareAchievement.setAdmissionNumber((String) data.get("ZKZH"));
                    softwareAchievement.setIdCard((String) data.get("ZJH"));
                    softwareAchievement.setRealName((String) data.get("XM"));
                    softwareAchievement.setMorningResults((String) data.get("SWCJ"));
                    softwareAchievement.setAfternoonResults((String) data.get("XWCJ"));
                    softwareAchievement.setThesisResults((String) data.get("LWCJ"));
                    softwareAchievement.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    softwareAchievementService.save(softwareAchievement);
                    ajaxUtil.success().msg("查询成绩成功").map(result);
                } else {
                    ajaxUtil.fail().msg((String) result.get("reasonPhrase"));
                }
            } else {
                ajaxUtil.fail().msg("获取连接失败");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("查询失败: 异常: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询历史成绩
     *
     * @return 数据
     */
    @GetMapping("/web/achievement/software/query/history")
    public ResponseEntity<Map<String, Object>> history() {
        AjaxUtil<SoftwareAchievementBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        if (StringUtils.isNotBlank(users.getIdCard())) {
            Optional<List<SoftwareAchievementBean>> optionalSoftwareAchievements = softwareAchievementService.findByIdCard(users.getIdCard());
            if (optionalSoftwareAchievements.isPresent()) {
                List<SoftwareAchievementBean> list = optionalSoftwareAchievements.get();
                for (SoftwareAchievementBean bean : list) {
                    bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
                    bean.setIdCard(StringUtils.isNotBlank(bean.getIdCard()) ? StringUtils.overlay(bean.getIdCard(), "****", 3, bean.getIdCard().length() - 4) : "");
                }
                ajaxUtil.success().list(optionalSoftwareAchievements.get());
            } else {
                ajaxUtil.fail().msg("查询失败");
            }
        } else {
            ajaxUtil.fail().msg("未完善身份证信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
