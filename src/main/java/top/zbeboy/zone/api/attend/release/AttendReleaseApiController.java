package top.zbeboy.zone.api.attend.release;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.attend.AttendReleaseService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.vo.attend.release.AttendReleaseAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class AttendReleaseApiController {

    @Resource
    private UsersService usersService;

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
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            if (!bindingResult.hasErrors()) {
                AttendRelease attendRelease = new AttendRelease();
                attendRelease.setAttendReleaseId(UUIDUtil.getUUID());
                attendRelease.setUsername(users.getUsername());
                attendRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
                attendRelease.setTitle(attendReleaseAddVo.getTitle());
                attendRelease.setOrganizeId(attendReleaseAddVo.getOrganizeId());
                attendRelease.setIsAuto(ByteUtil.toByte(1).equals(attendReleaseAddVo.getIsAuto()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                attendRelease.setAttendStartTime(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getAttendStartTime()));
                attendRelease.setAttendEndTime(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getAttendEndTime()));

                if (StringUtils.isBlank(attendReleaseAddVo.getExpireDate())) {
                    attendRelease.setExpireDate(DateTimeUtil.getNowSqlTimestamp());
                } else {
                    attendRelease.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(attendReleaseAddVo.getExpireDate()));
                }

                attendReleaseService.save(attendRelease);
                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
