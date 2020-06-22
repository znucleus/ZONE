package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.attend.AttendReleaseService;
import top.zbeboy.zone.web.bean.attend.AttendReleaseBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.attend.release.AttendReleaseAddVo;
import top.zbeboy.zbase.vo.attend.release.AttendReleaseEditVo;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendReleaseApiController {

    @Resource
    private AttendReleaseService attendReleaseService;

    /**
     * 保存
     *
     * @param attendReleaseAddVo 数据
     * @param principal          当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/save")
    public ResponseEntity<Map<String, Object>> save(AttendReleaseAddVo attendReleaseAddVo, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendReleaseAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseService.save(attendReleaseAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @GetMapping("/api/attend/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<AttendReleaseBean> ajaxUtil = attendReleaseService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param attendReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/update")
    public ResponseEntity<Map<String, Object>> update(AttendReleaseEditVo attendReleaseEditVo, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        attendReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseService.update(attendReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
