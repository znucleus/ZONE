package top.zbeboy.zone.api.attend.release;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.security.MyUserImpl;
import top.zbeboy.zone.service.attend.AttendReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.release.AttendReleaseAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class AttendReleaseApiController {

    @Resource
    private AttendReleaseService attendReleaseService;

    /**
     * 保存
     *
     * @param attendReleaseAddVo 数据
     * @param bindingResult      校验
     * @param principal          当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AttendReleaseAddVo attendReleaseAddVo, BindingResult bindingResult, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Objects.nonNull(principal) && principal instanceof OAuth2Authentication) {
            Users users = ((MyUserImpl) ((OAuth2Authentication) principal).getUserAuthentication().getPrincipal()).getUsers();
            if (Objects.nonNull(users)) {
                if (!bindingResult.hasErrors()) {
                    AttendRelease attendRelease = new AttendRelease();
                    attendRelease.setAttendReleaseId(UUIDUtil.getUUID());
                    attendRelease.setUsername(users.getUsername());
                    attendRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
                    attendRelease.setTitle(attendReleaseAddVo.getTitle());
                    attendRelease.setOrganizeId(attendReleaseAddVo.getOrganizeId());
                    attendRelease.setIsAuto(attendReleaseAddVo.getIsAuto());
                    attendRelease.setAttendStartTime(DateTimeUtil.parseSqlTimestamp(attendReleaseAddVo.getAttendStartTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    attendRelease.setAttendEndTime(DateTimeUtil.parseSqlTimestamp(attendReleaseAddVo.getAttendEndTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    attendRelease.setExpireDate(DateTimeUtil.parseSqlTimestamp(attendReleaseAddVo.getExpireDate(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    attendReleaseService.save(attendRelease);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
                }
            } else {
                ajaxUtil.fail().msg("获取用户信息失败");
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败，请先登录");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
