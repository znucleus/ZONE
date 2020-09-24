package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zbase.bean.campus.attend.AttendAuthoritiesBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendAuthoritiesService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.campus.attend.authorities.AttendAuthoritiesAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class AttendAuthoritiesApiController {

    @Resource
    private AttendAuthoritiesService attendAuthoritiesService;

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到权限数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/attend/authorize/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<AttendAuthoritiesBean> ajaxUtil = attendAuthoritiesService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param attendAuthoritiesAddVo 数据
     * @param principal              当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到权限保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/authorize/save")
    public ResponseEntity<Map<String, Object>> save(AttendAuthoritiesAddVo attendAuthoritiesAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendAuthoritiesAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendAuthoritiesService.save(attendAuthoritiesAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据id删除数据
     *
     * @param authoritiesId 主键
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园签到权限删除", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/authorize/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("id") String authoritiesId, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendAuthoritiesService.delete(users.getUsername(), authoritiesId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
