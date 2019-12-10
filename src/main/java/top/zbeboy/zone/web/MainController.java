package top.zbeboy.zone.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.annotation.logging.LoggingRecord;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.MenuService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.FilesService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private MenuService menuService;

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    /**
     * 登录页
     *
     * @return 登录页.
     */
    @GetMapping("/login")
    public String login() {
        return !authoritiesService.isAnonymousAuthenticated() ? "redirect:" + Workbook.WEB_BACKSTAGE : "login";
    }

    /**
     * 注册页
     *
     * @param type 注册类型(学生，教职工)
     * @return 注册页
     */
    @GetMapping("/register/{type}")
    public String register(@PathVariable("type") String type) {
        String page = "login";
        if (StringUtils.equals(type, Workbook.REGISTER_STUDENT)) {
            page = "student_register";
        } else if (StringUtils.equals(type, Workbook.REGISTER_STAFF)) {
            page = "staff_register";
        }
        return page;
    }

    /**
     * 忘记密码页
     *
     * @return 忘记密码页.
     */
    @GetMapping("/forget_password")
    public String forgetPassword() {
        return "forget_password";
    }

    /**
     * 后台页
     *
     * @return 后台页.
     */
    @LoggingRecord(module = "Main", methods = "backstage", description = "访问系统主页")
    @GetMapping(Workbook.WEB_BACKSTAGE)
    public String backstage(ModelMap modelMap, HttpServletRequest request) {
        List<String> roles = usersService.getAuthoritiesFromSession();
        // avatar.
        Users users = usersService.getUserFromSession();
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files)) {
                modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
            }
        }

        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("menu", menuService.getMenu(roles, users.getUsername()));

        return "backstage";
    }
}
