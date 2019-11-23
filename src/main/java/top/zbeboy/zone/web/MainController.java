package top.zbeboy.zone.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.MenuService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.upload.UploadService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private MenuService menuService;

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UploadService uploadService;

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
    @GetMapping(Workbook.WEB_BACKSTAGE)
    public String backstage(ModelMap modelMap) {
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

    /**
     * let's encrypt certificate check.
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/.well-known/acme-challenge/*")
    public void letUsEncryptCertificateCheck(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uri = request.getRequestURI().replace("/", "\\");
            //文件路径自行替换一下就行,就是上图中生成验证文件的路径,因为URI中已经包含了/.well-known/acme-challenge/,所以这里不需要
            File file = new File(ZoneProperties.getCertificate().getPlace() + uri);
            uploadService.download("验证文件", file, response, request);
        } catch (Exception e) {
            log.error("Let's encrypt certificate error : {} ", e);
        }
    }
}
