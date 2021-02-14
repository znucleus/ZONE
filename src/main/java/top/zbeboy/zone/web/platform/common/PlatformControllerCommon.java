package top.zbeboy.zone.web.platform.common;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.campus.roster.CampusRosterService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.GoogleOauthUtil;
import top.zbeboy.zbase.tools.web.util.PinYinUtil;
import top.zbeboy.zbase.vo.platform.user.UsersProfileVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class PlatformControllerCommon {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private CampusRosterService campusRosterService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户基本信息更新
     *
     * @param usersProfileVo 信息
     * @param own            当前用户
     * @return 是否更新成功
     */
    public AjaxUtil<Map<String, Object>> usersUpdate(UsersProfileVo usersProfileVo, Users own, HttpSession session, HttpServletRequest request, String channel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String name = StringUtils.deleteWhitespace(usersProfileVo.getName());
        String value = StringUtils.deleteWhitespace(usersProfileVo.getValue());

        boolean canUpdate = false;
        if (StringUtils.equals("email", name) ||
                StringUtils.equals("mobile", name)) {
            int mode = usersProfileVo.getMode();
            if (mode == 0) {
                String password = usersProfileVo.getPassword();
                if (StringUtils.isNotBlank(password)) {
                    if (BCryptUtil.bCryptPasswordMatches(password, own.getPassword())) {
                        canUpdate = true;
                    } else {
                        ajaxUtil.fail().msg("登录密码错误");
                    }
                } else {
                    ajaxUtil.fail().msg("请填写登录密码");
                }
            } else if (mode == 1) {
                String dynamicPassword = usersProfileVo.getDynamicPassword();
                if (StringUtils.isNotBlank(dynamicPassword)) {
                    if (NumberUtils.isDigits(dynamicPassword)) {
                        Optional<GoogleOauth> optionalGoogleOauth = usersService.findGoogleOauthByUsername(own.getUsername());
                        if (optionalGoogleOauth.isPresent()) {
                            if (GoogleOauthUtil.validCode(optionalGoogleOauth.get().getGoogleOauthKey(), NumberUtils.toInt(dynamicPassword))) {
                                canUpdate = true;
                            } else {
                                ajaxUtil.fail().msg("动态密码错误");
                            }
                        } else {
                            ajaxUtil.fail().msg("您未开启双因素认证");
                        }
                    } else {
                        ajaxUtil.fail().msg("动态密码错误，非数字");
                    }
                } else {
                    ajaxUtil.fail().msg("请填写动态密码");
                }
            } else {
                ajaxUtil.fail().msg("不支持的验证模式");
            }
        }

        if (StringUtils.equals("username", name)) {
            ajaxUtil = usersService.checkUsername(value);
            if (BooleanUtils.isTrue(ajaxUtil.getState())) {
                if (!StringUtils.equals(own.getUsername(), value)) {
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("own", own.getUsername());
                    paramMap.put("username", value);
                    Optional<List<Users>> optionalUsers = usersService.usersNeOwn(paramMap);
                    if (!optionalUsers.isPresent()) {
                        // 更新
                        usersService.updateUsername(own.getUsername(), value);
                        ajaxUtil.success().msg("账号更新成功");
                    } else {
                        ajaxUtil.fail().msg("账号已被注册");
                    }
                } else {
                    ajaxUtil.fail().msg("账号未改变");
                }
            }
        } else if (StringUtils.equals("realName", name)) {
            if (!StringUtils.equals(own.getRealName(), value)) {
                own.setRealName(value);
                usersService.update(own);
                ajaxUtil.success().msg("姓名更新成功");

                // 学生需要同步花名册
                Optional<UsersType> optionalUsersType = usersTypeService.findById(own.getUsersTypeId());
                if (optionalUsersType.isPresent() && StringUtils.equals(Workbook.STUDENT_USERS_TYPE, optionalUsersType.get().getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsername(own.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        Optional<RosterData> optionalRosterData = campusRosterService.findRosterDataByStudentNumber(optionalStudentBean.get().getStudentNumber());
                        if (optionalRosterData.isPresent()) {
                            RosterData rosterData = optionalRosterData.get();
                            rosterData.setRealName(value);
                            rosterData.setNamePinyin(PinYinUtil.changeToUpper(value));
                            campusRosterService.dataSync(rosterData);
                        }
                    }
                }
            } else {
                ajaxUtil.fail().msg("姓名未改变");
            }
        } else if (StringUtils.equals("email", name)) {
            if (canUpdate) {
                if (Pattern.matches(Workbook.MAIL_REGEX, value)) {
                    if (!StringUtils.equals(own.getEmail(), value)) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("own", own.getEmail());
                        paramMap.put("email", value);
                        Optional<List<Users>> optionalUsers = usersService.usersNeOwn(paramMap);
                        if (!optionalUsers.isPresent()) {
                            // 检查邮件推送是否被关闭
                            Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (optionalSystemConfigure.isPresent()) {
                                SystemConfigure systemConfigure = optionalSystemConfigure.get();
                                if (StringUtils.equals("1", systemConfigure.getDataValue())) {
                                    DateTime dateTime = DateTime.now();
                                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());

                                    own.setEmail(value);
                                    own.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                                    own.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                                    own.setVerifyMailbox(BooleanUtil.toByte(false));
                                    usersService.update(own);
                                    systemMailService.sendValidEmailMail(own, RequestUtil.getBaseUrl(request));
                                    ajaxUtil.success().msg("邮箱更新成功");
                                } else {
                                    ajaxUtil.fail().msg("邮件推送已被管理员关闭");
                                }
                            } else {
                                ajaxUtil.fail().msg("查询系统配置错误");
                            }
                        } else {
                            ajaxUtil.fail().msg("邮箱已被使用");
                        }
                    } else {
                        ajaxUtil.fail().msg("邮箱未改变");
                    }
                } else {
                    ajaxUtil.fail().msg("邮箱格式不正确");
                }
            }
        } else if (StringUtils.equals("mobile", name)) {
            if (canUpdate) {
                if (Pattern.matches(Workbook.MOBILE_REGEX, value)) {
                    if (!StringUtils.equals(own.getMobile(), value)) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("own", own.getMobile());
                        paramMap.put("mobile", value);
                        Optional<List<Users>> optionalUsers = usersService.usersNeOwn(paramMap);
                        if (!optionalUsers.isPresent()) {
                            if (StringUtils.equals(channel, Workbook.channel.WEB.name())) {
                                // step 2.手机号是否已验证
                                if (Objects.nonNull(session.getAttribute(value + SystemMobileConfig.MOBILE_VALID))) {
                                    boolean isValid = (boolean) session.getAttribute(value + SystemMobileConfig.MOBILE_VALID);
                                    if (isValid) {
                                        own.setMobile(value);
                                        usersService.update(own);
                                        ajaxUtil.success().msg("更新手机号成功");
                                    } else {
                                        ajaxUtil.fail().msg("验证手机号失败");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("请重新验证手机号");
                                }
                            } else if (StringUtils.equals(channel, Workbook.channel.API.name())) {
                                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                                if (stringRedisTemplate.hasKey(value + "_" + usersProfileVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID)) {
                                    String validContent = ops.get(value + "_" + usersProfileVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID);
                                    boolean isValid = BooleanUtils.toBoolean(validContent);
                                    if (isValid) {
                                        own.setMobile(value);
                                        usersService.update(own);
                                        ajaxUtil.success().msg("更新手机号成功");
                                    } else {
                                        ajaxUtil.fail().msg("验证手机号失败");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("验证码未验证，请先验证");
                                }
                            } else {
                                ajaxUtil.fail().msg("不支持的渠道");
                            }
                        } else {
                            ajaxUtil.fail().msg("手机号已被使用");
                        }
                    } else {
                        ajaxUtil.fail().msg("手机号未改变");
                    }
                } else {
                    ajaxUtil.fail().msg("手机号不正确");
                }
            }
        } else if (StringUtils.equals("idCard", name)) {
            if (Pattern.matches(Workbook.ID_CARD_REGEX, value)) {
                if (!StringUtils.equals(own.getIdCard(), value)) {
                    // 检查是否已经存在该身份证号
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("own", own.getIdCard());
                    paramMap.put("idCard", value);
                    Optional<List<Users>> optionalUsers = usersService.usersNeOwn(paramMap);
                    if (!optionalUsers.isPresent()) {
                        own.setIdCard(value);
                        usersService.update(own);
                        ajaxUtil.success().msg("身份证号更新成功");

                        // 学生需要同步花名册
                        Optional<UsersType> optionalUsersType = usersTypeService.findById(own.getUsersTypeId());
                        if (optionalUsersType.isPresent() && StringUtils.equals(Workbook.STUDENT_USERS_TYPE, optionalUsersType.get().getUsersTypeName())) {
                            Optional<StudentBean> optionalStudentBean = studentService.findByUsername(own.getUsername());
                            if (optionalStudentBean.isPresent()) {
                                Optional<RosterData> optionalRosterData = campusRosterService.findRosterDataByStudentNumber(optionalStudentBean.get().getStudentNumber());
                                if (optionalRosterData.isPresent()) {
                                    RosterData rosterData = optionalRosterData.get();
                                    rosterData.setIdCard(value);
                                    campusRosterService.dataSync(rosterData);
                                }
                            }
                        }
                    } else {
                        ajaxUtil.fail().msg("身份证号已经存在");
                    }
                } else {
                    ajaxUtil.fail().msg("身份证号未改变");
                }
            } else {
                ajaxUtil.fail().msg("身份证号不正确");
            }
        } else {
            ajaxUtil.fail().msg("未发现更新类型");
        }
        return ajaxUtil;
    }
}
