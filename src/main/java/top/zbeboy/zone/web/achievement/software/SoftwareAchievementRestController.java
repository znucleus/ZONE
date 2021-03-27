package top.zbeboy.zone.web.achievement.software;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.achievement.software.SoftwareAchievementBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SoftwareAchievementRestController {

    KeyedObjectPool<String, SoftwareAchievementHttpClient> objectPool = new GenericKeyedObjectPool<>(new SoftwareAchievementHttpClientFactory());

    @Resource
    private SoftwareAchievementService softwareAchievementService;

    /**
     * 获取验证码
     *
     * @param response 响应
     * @throws Exception 异常
     */
    @GetMapping("/web/achievement/software/query/captcha")
    public void captcha(HttpServletResponse response) throws Exception {
        Users users = SessionUtil.getUserFromSession();
        // 从池中借走到一个对象。借走不等于删除。对象一直都属于池子，只是状态的变化。
        SoftwareAchievementHttpClient softwareAchievementHttpClient = objectPool.borrowObject(users.getUsername());
        softwareAchievementHttpClient.captcha(response);
        // 归还对象，否则下次取会产生新的对象
        objectPool.returnObject(users.getUsername(), softwareAchievementHttpClient);
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
        // 从池中借走到一个对象。借走不等于删除。对象一直都属于池子，只是状态的变化。
        SoftwareAchievementHttpClient softwareAchievementHttpClient = objectPool.borrowObject(users.getUsername());
        List<String> list = softwareAchievementHttpClient.examDate();
        // 归还对象，否则下次取会产生新的对象
        objectPool.returnObject(users.getUsername(), softwareAchievementHttpClient);
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
            SoftwareAchievementHttpClient softwareAchievementHttpClient = objectPool.borrowObject(users.getUsername());
            Map<String, Object> result = softwareAchievementHttpClient.verifyCaptcha(param);
            softwareAchievementHttpClient.getHttpclient().close();
            objectPool.clear(users.getUsername());
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
                for(SoftwareAchievementBean bean : list){
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