package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendDataService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.campus.attend.data.AttendDataAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendDataApiController {

    @Resource
    private AttendDataService attendDataService;

    /**
     * 保存
     *
     * @param attendDataAddVo 数据
     * @param principal       当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到数据保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/data/save")
    public ResponseEntity<Map<String, Object>> save(AttendDataAddVo attendDataAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendDataAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendDataService.save(attendDataAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除签到记录
     *
     * @param attendReleaseSubId 子表ID
     * @param attendUsersId      名单ID
     * @param principal          当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到数据删除", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/data/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("attendReleaseSubId") int attendReleaseSubId,
                                                      @RequestParam("attendUsersId") String attendUsersId, Principal principal,
                                                      HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendDataService.delete(attendReleaseSubId, attendUsersId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
