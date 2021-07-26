package top.zbeboy.zone.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.feign.system.MapKeyService;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zone.annotation.logging.LoginLoggingRecord;
import top.zbeboy.zone.service.platform.MenuService;
import top.zbeboy.zone.web.platform.common.PlatformControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {

    @Resource
    private MenuService menuService;

    @Resource
    private FilesService filesService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private RoleService roleService;

    @Resource
    private MapKeyService mapKeyService;

    @Resource
    private PlatformControllerCommon platformControllerCommon;

    private final RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 首页
     *
     * @return 首页
     */
    @GetMapping(value = {"/", "/index", "/home"})
    public String index() {
        return "index";
    }

    /**
     * 登录页
     *
     * @return 登录页.
     */
    @GetMapping("/login")
    public String login(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        String page;
        boolean needSkip = false;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (Objects.nonNull(savedRequest)) {
            String requestURI = savedRequest.getRedirectUrl();
            if (requestURI.contains(Workbook.OAUTH_AUTHORIZE)) {
                needSkip = true;
            }
        }

        if (!needSkip) {
            modelMap.put("loginType", "normal");
            page = !SessionUtil.isAnonymousAuthenticated() ? "redirect:" + Workbook.WEB_BACKSTAGE : "login";
        } else {
            modelMap.put("loginType", "oauth");
            page = "login";
        }
        return page;
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
        } else if (StringUtils.equals(type, Workbook.REGISTER_POTENTIAL)) {
            page = "potential_register";
        }
        return page;
    }

    /**
     * 忘记密码页
     *
     * @return 忘记密码页.
     */
    @GetMapping("/forget-password")
    public String forgetPassword() {
        return "forget_password";
    }

    /**
     * 后台页
     *
     * @return 后台页.
     */
    @LoginLoggingRecord(module = "Main", methods = "backstage", description = "访问系统主页")
    @GetMapping(Workbook.WEB_BACKSTAGE)
    public String backstage(ModelMap modelMap, HttpServletRequest request) {

        // avatar.
        Users users = SessionUtil.getUserFromSession();
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Optional<Files> optionalFiles = filesService.findById(users.getAvatar());
            optionalFiles.ifPresent(files -> modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath()));
        }

        boolean isPotential = false;
        if (Objects.nonNull(users.getUsersTypeId()) && users.getUsersTypeId() > 0) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(usersType.getUsersTypeName(), Workbook.POTENTIAL_USERS_TYPE)) {
                    isPotential = true;
                }
            }

        }

        platformControllerCommon.personalQrCode(users.getUsername(),users.getAvatar(),users.getUsersTypeId(), RequestUtil.getRealPath(request));

        modelMap.addAttribute("isPotential", isPotential);
        modelMap.addAttribute("realName", users.getRealName());

        // map key
        Optional<MapKey> optionalMapKey = mapKeyService.mapKey(Workbook.mapFactory.GD.name(), Workbook.mapBusiness.GD_WEB_IP_LOCATION_KEY.name());
        optionalMapKey.ifPresent(mapKey -> modelMap.addAttribute("mapKey", mapKey.getMapKey()));

        Optional<List<Role>> optionalRoles = roleService.findByUsername(users.getUsername());
        if (optionalRoles.isPresent()) {
            List<String> roles = new ArrayList<>();
            optionalRoles.get().forEach(r -> roles.add(r.getRoleEnName()));
            modelMap.addAttribute("menu", menuService.getMenu(roles, users.getUsername()));
        } else {
            modelMap.addAttribute("menu", "");
        }

        return "backstage";
    }

    /**
     * 健康检查
     *
     * @return 健康检查
     */
    @GetMapping(value = "/anyone/health")
    @ResponseBody
    public String health() {
        return "ok";
    }
}
