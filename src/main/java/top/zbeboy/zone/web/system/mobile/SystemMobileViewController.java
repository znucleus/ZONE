package top.zbeboy.zone.web.system.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class SystemMobileViewController {

    @Resource
    private UsersService usersService;

    /**
     * 重置密码
     *
     * @param modelMap 页面对象
     * @return 消息
     */
    @GetMapping("/anyone/reset_password/mobile")
    public String resetPasswordMobile(HttpSession session, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        if (Objects.nonNull(session.getAttribute(SystemMobileConfig.MOBILE))) {
            String mobile = (String) session.getAttribute(SystemMobileConfig.MOBILE);
            Users users = usersService.findByMobile(mobile);
            if (Objects.nonNull(users)) {
                if (Objects.nonNull(session.getAttribute(mobile + SystemMobileConfig.MOBILE_VALID))) {
                    boolean isValid = (boolean) session.getAttribute(mobile + SystemMobileConfig.MOBILE_VALID);
                    if (isValid) {
                        modelMap.addAttribute("username", users.getUsername());
                        modelMap.addAttribute("verificationMode", 1);
                        return "reset_password";
                    } else {
                        config.buildDangerTip(
                                "重置密码失败。",
                                "手机验证码无效。");
                        config.addLoginButton();
                        config.addHomeButton();
                        config.dataMerging(modelMap);
                    }
                } else {
                    config.buildDangerTip(
                            "重置密码失败。",
                            "手机号未验证。");
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                }
            } else {
                config.buildDangerTip(
                        "重置密码失败。",
                        "未发现您的账号注册信息！");
                config.addLoginButton();
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "重置密码失败。",
                    "获取手机号失败！");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }

        return "tip";
    }
}
