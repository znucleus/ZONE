package top.zbeboy.zone.web.data.potential;

import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.PotentialService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.vo.data.potential.PotentialAddVo;
import top.zbeboy.zbase.vo.data.potential.PotentialEditVo;
import top.zbeboy.zbase.vo.data.staff.StaffEditVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class PotentialRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private PotentialService potentialService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    /**
     * 临时用户注册
     *
     * @param potentialAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/anyone/data/register/potential")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterPotential(PotentialAddVo potentialAddVo, HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 手机号是否已验证
        if (!ObjectUtils.isEmpty(session.getAttribute(potentialAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
            boolean isValid = (boolean) session.getAttribute(potentialAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
            if (isValid) {
                potentialAddVo.setEnabled(BooleanUtil.toByte(true));
                potentialAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                potentialAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                potentialAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                potentialAddVo.setUsersTypeId(usersTypeService.findByUsersTypeName(Workbook.POTENTIAL_USERS_TYPE).getUsersTypeId());
                potentialAddVo.setAvatar(Workbook.USERS_AVATAR);
                DateTime dateTime = DateTime.now();
                dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                potentialAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                potentialAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                potentialAddVo.setJoinDate(DateTimeUtil.getNowSqlDate());
                potentialAddVo.setLangKey(request.getLocale().toLanguageTag());
                potentialAddVo.setBaseUrl(RequestUtil.getBaseUrl(request));
                ajaxUtil = potentialService.save(potentialAddVo);

                if (ajaxUtil.getState()) {
                    Users users = new Users();
                    users.setUsername(potentialAddVo.getUsername());
                    users.setLangKey(potentialAddVo.getLangKey());
                    users.setMailboxVerifyCode(potentialAddVo.getMailboxVerifyCode());
                    users.setMailboxVerifyValid(potentialAddVo.getMailboxVerifyValid());
                    users.setEmail(potentialAddVo.getEmail());
                    users.setRealName(potentialAddVo.getRealName());
                    systemMailService.sendValidEmailMail(users, potentialAddVo.getBaseUrl());
                }
            } else {
                ajaxUtil.fail().msg("验证手机号失败");
            }
        } else {
            ajaxUtil.fail().msg("请重新验证手机号");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 临时用户信息更新
     *
     * @param potentialEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/users/potential/update/school")
    public ResponseEntity<Map<String, Object>> userPotentialUpdateSchool(PotentialEditVo potentialEditVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.userPotentialUpdateSchool(potentialEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param potentialEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/users/potential/update/info")
    public ResponseEntity<Map<String, Object>> userPotentialUpdateInfo(PotentialEditVo potentialEditVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.userPotentialUpdateInfo(potentialEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
