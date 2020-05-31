package top.zbeboy.zone.feign.platform;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.hystrix.platform.UsersHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = UsersHystrixClientFallbackFactory.class)
public interface UsersService {

    /**
     * 获取用户
     *
     * @param username 账号
     * @return 数据
     */
    @GetMapping("/base/users/username/{username}")
    Users findByUsername(@PathVariable("username") String username);

    /**
     * 获取用户
     *
     * @param email 邮箱
     * @return 数据
     */
    @GetMapping("/base/users/email/{email}")
    Users findByEmail(@PathVariable("email") String email);

    /**
     * 获取用户
     *
     * @param mobile 手机号
     * @return 数据
     */
    @GetMapping("/base/users/mobile/{mobile}")
    Users findByMobile(@PathVariable("mobile") String mobile);

    /**
     * 获取用户
     *
     * @param username 账号
     * @param own      当前账号
     * @return 数据
     */
    @GetMapping("/base/users/username/ne/own/{username}/{own}")
    List<Users> findByUsernameNeOwn(@PathVariable("username") String username, @PathVariable("own") String own);

    /**
     * 获取用户
     *
     * @param email 邮箱
     * @param own   当前账号
     * @return 数据
     */
    @GetMapping("/base/users/email/ne/own/{email}/{own}")
    List<Users> findByEmailNeOwn(@PathVariable("email") String email, @PathVariable("own") String own);

    /**
     * 获取用户
     *
     * @param mobile 电话
     * @param own    当前账号
     * @return 数据
     */
    @GetMapping("/base/users/mobile/ne/own/{mobile}/{own}")
    List<Users> findByMobileNeOwn(@PathVariable("mobile") String mobile, @PathVariable("own") String own);

    /**
     * 获取用户
     *
     * @param idCard 身份证号
     * @param own    当前账号
     * @return 数据
     */
    @GetMapping("/base/users/id_card/ne/own/{idCard}/{own}")
    List<Users> findByIdCardNeOwn(@PathVariable("idCard") String idCard, @PathVariable("own") String own);

    /**
     * 获取用户
     *
     * @param joinDate      加入时间
     * @param verifyMailbox 是否验证邮箱
     * @return 数据
     */
    @PostMapping("/base/users/join_date/verify_mailbox")
    List<Users> findByJoinDateAndVerifyMailbox(@RequestBody java.sql.Date joinDate, @RequestParam("verifyMailbox") Byte verifyMailbox);

    /**
     * 检验账号是否被注册
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/base/anyone/check/username")
    AjaxUtil<Map<String, Object>> anyoneCheckUsername(@RequestParam("username") String username);

    /**
     * 检验账号是否存在
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/base/anyone/check/exist/username")
    AjaxUtil<Map<String, Object>> anyoneCheckExistUsername(@RequestParam("username") String username);

    /**
     * 检验邮箱是否被注册
     *
     * @param email 邮箱
     * @return 是否被注册
     */
    @PostMapping("/base/anyone/check/email")
    AjaxUtil<Map<String, Object>> anyoneCheckEmail(@RequestParam("email") String email);

    /**
     * 检验手机号是否被注册
     *
     * @param mobile 手机号
     * @return 是否被注册
     */
    @PostMapping("/base/anyone/check/mobile")
    AjaxUtil<Map<String, Object>> anyoneCheckMobile(@RequestParam("mobile") String mobile);

    /**
     * 检验密码是否正确
     *
     * @param password 密码
     * @return 是否正确
     */
    @PostMapping("/base/users/check/password")
    AjaxUtil<Map<String, Object>> userCheckPassword(@RequestParam("username") String username, @RequestParam("password") String password);

    /**
     * 根据账号检验是否存在以及该用户状态是否正常
     *
     * @param username 账号
     * @return 是否存在以及该用户状态是否正常
     */
    @PostMapping("/base/users/check/username/status")
    AjaxUtil<Map<String, Object>> userCheckStatusByUsername(@RequestParam("username") String username);

    /**
     * 检验手机号是否被注册
     *
     * @param mobile 手机号
     * @return 是否被注册
     */
    @PostMapping("/base/users/check/mobile")
    AjaxUtil<Map<String, Object>> usersCheckMobile(@RequestParam("username") String username, @RequestParam("mobile") String mobile);

    /**
     * 检验邮箱是否被注册
     *
     * @param email 邮箱
     * @return 是否被注册
     */
    @PostMapping("/base/users/check/email")
    AjaxUtil<Map<String, Object>> usersCheckEmail(@RequestParam("username") String username, @RequestParam("email") String email);

    /**
     * 双因素认证开启
     *
     * @param password 当前密码
     * @return true or false
     */
    @PostMapping("/base/users/open/google_oauth")
    AjaxUtil<Map<String, Object>> userOpenGoogleOauth(@RequestParam("username") String username, @RequestParam("password") String password);

    /**
     * 双因素认证关闭
     *
     * @param mode            验证模式
     * @param password        密码
     * @param dynamicPassword 动态密码
     * @return true or false
     */
    @PostMapping("/base/users/close/google_oauth")
    AjaxUtil<Map<String, Object>> userCloseGoogleOauth(@RequestParam("username") String username, @RequestParam("mode") int mode, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "dynamicPassword", required = false) String dynamicPassword);

    /**
     * 更新密码
     *
     * @param oldPassword     旧密码
     * @param newPassword     新密码
     * @param confirmPassword 确认密码
     * @return 是否成功
     */
    @PostMapping("/base/users/password/update")
    AjaxUtil<Map<String, Object>> userPasswordUpdate(@RequestParam("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword);

    /**
     * upload users avatar.
     *
     * @param file     base64 file.
     * @param fileName name.
     * @return success or fail.
     */
    @PostMapping("/base/users/avatar/upload")
    AjaxUtil<Map<String, Object>> userAvatarUpload(@RequestParam("username") String username, @RequestParam("file") String file, @RequestParam("fileName") String fileName);

    /**
     * delete avatar.
     *
     * @return success or false
     */
    @GetMapping("/base/users/avatar/delete/{username}")
    AjaxUtil<Map<String, Object>> userAvatarDelete(@PathVariable("username") String username);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/platform/users/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 用户角色数据
     *
     * @param username       用户账号
     * @param targetUsername 目标账号
     * @return 数据
     */
    @PostMapping("/base/platform/users/role/data")
    List<Role> roleData(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @return success or false
     */
    @PostMapping("/base/platform/users/role/save")
    AjaxUtil<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername, @RequestParam("roles") String roles);

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/base/platform/users/update/enabled")
    AjaxUtil<Map<String, Object>> updateEnabled(@RequestParam(value = "userIds", required = false) String userIds, @RequestParam("enabled") Byte enabled);

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/base/platform/users/update/locked")
    AjaxUtil<Map<String, Object>> updateLocked(@RequestParam(value = "userIds", required = false) String userIds, @RequestParam("locked") Byte locked);

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/base/platform/users/update/password")
    AjaxUtil<Map<String, Object>> updatePassword(@RequestParam("username") String username);

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/base/platform/users/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam(value = "userIds", required = false) String userIds);

    /**
     * 忘记密码动态密码验证
     *
     * @param username        账号
     * @param dynamicPassword 动态密码
     * @return true or false
     */
    @PostMapping("/base/forget_password/dynamic_password")
    AjaxUtil<Map<String, Object>> forgetPassword(@RequestParam("username") String username, @RequestParam("dynamicPassword") String dynamicPassword);

    /**
     * 更新
     *
     * @param users 用户
     */
    @PostMapping("/base/users/update")
    void update(@RequestBody Users users);

    /**
     * 更新
     *
     * @param username       用户
     * @param targetUsername 目标用户
     */
    @PostMapping("/base/users/update/username")
    void updateUsername(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);

    /**
     * 删除
     *
     * @param users 用户
     */
    @PostMapping("/base/users/delete")
    void delete(@RequestBody List<Users> users);

    /**
     * 解锁
     */
    @GetMapping("/base/users/unlock")
    void unlockUsers();
}
