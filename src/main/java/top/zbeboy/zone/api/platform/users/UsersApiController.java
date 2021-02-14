package top.zbeboy.zone.api.platform.users;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.platform.user.ResetPasswordApiVo;
import top.zbeboy.zbase.vo.platform.user.UsersProfileVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.upload.FileBean;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.platform.common.PlatformControllerCommon;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
public class UsersApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RoleService roleService;

    @Resource
    private UploadService uploadService;

    @Resource
    private PlatformControllerCommon platformControllerCommon;

    /**
     * API:获取用户信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "用户数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/platform/users")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Map<String, Object> outPut = new HashMap<>();
            outPut.put("realName", users.getRealName());
            outPut.put("username", users.getUsername());
            outPut.put("usersTypeId", users.getUsersTypeId());
            outPut.put("verifyMailbox", users.getVerifyMailbox());
            outPut.put("enabled", users.getEnabled());
            outPut.put("accountNonLocked", users.getAccountNonLocked());
            if (StringUtils.isNotBlank(users.getAvatar())) {
                Optional<Files> optionalFiles = filesService.findById(users.getAvatar());
                optionalFiles.ifPresent(files -> outPut.put("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath()));
            }

            String clientId = ((OAuth2Authentication) principal).getOAuth2Request().getClientId();
            if (StringUtils.isNotBlank(clientId) && Workbook.advancedApp().contains(clientId)) {
                outPut.put("email", users.getEmail());
                outPut.put("mobile", users.getMobile());
                outPut.put("idCard", users.getIdCard());
                outPut.put("joinDate", DateTimeUtil.defaultFormatSqlDate(users.getJoinDate()));

                outPut.put("authorities", ((OAuth2Authentication) principal).getUserAuthentication().getAuthorities());
                // roles.
                Optional<List<Role>> optionalRoles = roleService.findByUsername(users.getUsername());
                List<String> rList = new ArrayList<>();
                optionalRoles.ifPresent(roles -> roles.forEach(r -> rList.add(r.getRoleName())));

                outPut.put("roles", String.join(",", rList));
            }
            ajaxUtil.success().msg("获取用户信息成功").map(outPut);
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 重置密码
     *
     * @param resetPasswordApiVo 数据
     * @param bindingResult      检验
     * @return 重置
     */
    @PostMapping("/overt/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid ResetPasswordApiVo resetPasswordApiVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (resetPasswordApiVo.getPassword().equals(resetPasswordApiVo.getOkPassword())) {
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                if (stringRedisTemplate.hasKey(resetPasswordApiVo.getMobile() + "_" + resetPasswordApiVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID)) {
                    String validContent = ops.get(resetPasswordApiVo.getMobile() + "_" + resetPasswordApiVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID);
                    boolean isValid = BooleanUtils.toBoolean(validContent);
                    if (isValid) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("mobile", resetPasswordApiVo.getMobile());
                        Optional<Users> result = usersService.findByCondition(paramMap);
                        if (result.isPresent()) {
                            Users users = result.get();
                            users.setPassword(BCryptUtil.bCryptPassword(resetPasswordApiVo.getPassword()));
                            usersService.update(users);
                            ajaxUtil.success().msg("重置密码成功");
                        } else {
                            ajaxUtil.fail().msg("查询用户注册信息失败");
                        }
                    } else {
                        ajaxUtil.fail().msg("验证码未验证通过");
                    }
                } else {
                    ajaxUtil.fail().msg("验证码未验证，请先验证");
                }
            } else {
                ajaxUtil.fail().msg("密码不一致");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 上传头像
     *
     * @param request 数据
     * @return true or false
     */
    @PostMapping("/api/platform/users/avatar/upload")
    public ResponseEntity<Map<String, Object>> userAvatarUpload(Principal principal, MultipartHttpServletRequest request) {
        AjaxUtil<FileBean> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                String path = Workbook.avatarPath(users.getUsername());
                List<FileBean> fileBeens = uploadService.upload(request,
                        RequestUtil.getRealPath(request) + path, RequestUtil.getIpAddress(request));
                if (!fileBeens.isEmpty()) {
                    FileBean fileBean = fileBeens.get(0);
                    fileBean.setRelativePath(path + fileBean.getNewName());

                    Files files = new Files();
                    files.setFileId(UUIDUtil.getUUID());
                    files.setFileSize(fileBean.getFileSize());
                    files.setContentType(fileBean.getContentType());
                    files.setOriginalFileName(fileBean.getOriginalFileName());
                    files.setNewName(fileBean.getNewName());
                    files.setRelativePath(fileBean.getRelativePath());
                    files.setExt(fileBean.getExt());
                    BaseImgUtil.optimizeImage(files, request, path, 500, 500, 0.5f);
                    filesService.save(files);

                    String avatar = users.getAvatar();
                    users.setAvatar(files.getFileId());
                    usersService.update(users);
                    if (!StringUtils.equals(avatar, Workbook.USERS_AVATAR)) {
                        Optional<Files> optionalFiles = filesService.findById(avatar);
                        if (optionalFiles.isPresent()) {
                            Files oldFiles = optionalFiles.get();
                            // delete file.
                            FilesUtil.deleteFile(RequestUtil.getRealPath(request) + oldFiles.getRelativePath());
                            filesService.delete(oldFiles);
                        }
                    }

                    ajaxUtil.success().msg("上传头像成功").put("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
                } else {
                    ajaxUtil.fail().msg("上传失败，未获取到文件");
                }
            } else {
                ajaxUtil.fail().msg("获取用户信息失败");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("上传头像失败： " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 用户基本信息更新
     *
     * @param usersProfileVo 信息
     * @param bindingResult  检验
     * @return 是否更新成功
     */
    @ApiLoggingRecord(remark = "用户基本信息更新", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/platform/users/update")
    public ResponseEntity<Map<String, Object>> usersUpdate(@Valid UsersProfileVo usersProfileVo, BindingResult bindingResult, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users own = SessionUtil.getUserFromOauth(principal);
            ajaxUtil = platformControllerCommon.usersUpdate(usersProfileVo, own, null, request, Workbook.channel.API.name());
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
