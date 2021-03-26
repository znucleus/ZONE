package top.zbeboy.zone.web.achievement.software;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SoftwareRestController {

    KeyedObjectPool<String, SoftwareHttpClient> objectPool = new GenericKeyedObjectPool<>(new SoftwareHttpClientFactory());

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
        SoftwareHttpClient softwareHttpClient = objectPool.borrowObject(users.getUsername());
        softwareHttpClient.captcha(response);
        // 归还对象，否则下次取会产生新的对象
        objectPool.returnObject(users.getUsername(), softwareHttpClient);
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
        SoftwareHttpClient softwareHttpClient = objectPool.borrowObject(users.getUsername());
        List<String> list = softwareHttpClient.examDate();
        // 归还对象，否则下次取会产生新的对象
        objectPool.returnObject(users.getUsername(), softwareHttpClient);
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
    @PostMapping("/web/achievement/software/query/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("captcha") String captcha,
                                                    @RequestParam("stage") String stage,
                                                    @RequestParam("xm") String xm,
                                                    @RequestParam("zjhm") String zjhm,
                                                    @RequestParam("selectType") String selectType) {
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
            SoftwareHttpClient softwareHttpClient = objectPool.borrowObject(users.getUsername());
            Map<String, Object> result = softwareHttpClient.verifyCaptcha(param);
            softwareHttpClient.getHttpclient().close();
            objectPool.clear(users.getUsername());
            Boolean hasError = (Boolean) result.get("hasError");
            if (!hasError) {
                ajaxUtil.success().msg("查询成绩成功").map(result);
            } else {
                ajaxUtil.fail().msg((String) result.get("reasonPhrase"));
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("查询失败: 异常: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
