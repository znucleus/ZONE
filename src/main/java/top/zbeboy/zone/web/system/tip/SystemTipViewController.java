package top.zbeboy.zone.web.system.tip;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemTipViewController {

    /**
     * 注册成功返回信息
     *
     * @param modelMap 页面数据
     * @return 注册成功
     */
    @GetMapping("/tip/register/success")
    public String registerSuccess(ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig(
                "恭喜您注册成功，稍后请前往您的邮箱进行邮箱验证。",
                "邮箱验证通过后，您将可以正常登录，并获得更多功能体验。");
        config.addLoginButton();
        config.addHomeButton();
        config.dataMerging(modelMap);
        return "tip";
    }

    /**
     * 忘记密码验证成功返回信息
     *
     * @param modelMap 页面数据
     * @return 忘记密码验证成功
     */
    @GetMapping("/tip/forget_password/success")
    public String forgetPasswordSuccess(ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig(
                "您的邮箱已验证成功",
                "稍后请前往您的邮箱点击重置密码链接进行密码重置。");
        config.addLoginButton();
        config.addHomeButton();
        config.dataMerging(modelMap);
        return "tip";
    }

    /**
     * 重置密码成功
     *
     * @param modelMap 页面对象
     * @return 成功
     */
    @GetMapping("/tip/reset_password/success")
    public String resetPasswordSuccess(ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig(
                "您的密码已重置成功",
                "");
        config.addLoginButton();
        config.addHomeButton();
        config.dataMerging(modelMap);
        return "tip";
    }

}
