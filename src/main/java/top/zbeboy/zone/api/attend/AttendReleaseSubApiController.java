package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.attend.AttendReleaseSubService;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendReleaseSubApiController {

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @param principal            当前用户信息
     * @return true or false
     */
    @GetMapping("/api/attend/sub/data")
    public ResponseEntity<Map<String, Object>> subData(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<AttendReleaseSubBean> ajaxUtil = attendReleaseSubService.subData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 通过id查询子表数据
     *
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    @GetMapping("/api/attend/sub/query/{id}")
    public ResponseEntity<Map<String, Object>> subQuery(@PathVariable("id") int attendReleaseSubId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseSubService.subQuery(attendReleaseSubId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据id删除子表数据
     *
     * @param attendReleaseSubId 子表数据
     * @return 数据
     */
    @PostMapping("/api/attend/sub/delete")
    public ResponseEntity<Map<String, Object>> subDelete(@RequestParam("id") int attendReleaseSubId, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendReleaseSubService.subDelete(attendReleaseSubId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
