package top.zbeboy.zone.hystrix.platform;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.GoogleOauth;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.platform.UsersService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UsersHystrixClientFallbackFactory implements UsersService {
    @Override
    public Users findByUsername(String username) {
        return new Users();
    }

    @Override
    public Users findByEmail(String email) {
        return new Users();
    }

    @Override
    public Users findByMobile(String mobile) {
        return new Users();
    }

    @Override
    public GoogleOauth findGoogleOauthByUsername(String username) {
        return new GoogleOauth();
    }

    @Override
    public List<Users> findByUsernameNeOwn(String username, String own) {
        return new ArrayList<>();
    }

    @Override
    public List<Users> findByEmailNeOwn(String email, String own) {
        return new ArrayList<>();
    }

    @Override
    public List<Users> findByMobileNeOwn(String mobile, String own) {
        return new ArrayList<>();
    }

    @Override
    public List<Users> findByIdCardNeOwn(String idCard, String own) {
        return new ArrayList<>();
    }

    @Override
    public List<Users> findByJoinDateAndVerifyMailbox(Date joinDate, Byte verifyMailbox) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckUsername(String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckExistUsername(String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckEmail(String email) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckMobile(String mobile) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCheckPassword(String username, String password) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCheckStatusByUsername(String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> usersCheckMobile(String username, String mobile) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> usersCheckEmail(String username, String email) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userOpenGoogleOauth(String username, String password) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCloseGoogleOauth(String username, int mode, String password, String dynamicPassword) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userPasswordUpdate(String username, String oldPassword, String newPassword, String confirmPassword) {
        return AjaxUtil.of();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public List<Role> roleData(String username, String targetUsername) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> roleSave(String username, String targetUsername, String roles) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updateEnabled(String userIds, Byte enabled) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updateLocked(String userIds, Byte locked) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updatePassword(String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String userIds) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> forgetPassword(String username, String dynamicPassword) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkUsername(String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(Users users) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updateUsername(String username, String targetUsername) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(List<Users> users) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> unlockUsers() {
        return AjaxUtil.of();
    }
}
