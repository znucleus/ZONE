package top.zbeboy.zone.web.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.attend.AttendUsersBean;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendReleaseService;
import top.zbeboy.zbase.feign.campus.attend.AttendReleaseSubService;
import top.zbeboy.zbase.feign.campus.attend.AttendUsersService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CampusAttendRestController {

    @Resource
    private AttendReleaseService attendReleaseService;

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    @Resource
    private AttendUsersService attendUsersService;

    /**
     * 获取签到子表数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/campus/attend/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("title");
        headers.add("attendStartTime");
        headers.add("attendEndTime");
        headers.add("isAuto");
        headers.add("validDate");
        headers.add("expireDate");
        headers.add("organizeName");
        headers.add("totalUsers");
        headers.add("totalAttend");
        headers.add("releaseTime");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(attendReleaseSubService.statistics(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 获取详细签到数据
     *
     * @return 详细签到数据
     */
    @GetMapping("/web/campus/attend/details/data")
    public ResponseEntity<Map<String, Object>> detailsData(@RequestParam("attendReleaseId") String attendReleaseId,
                                                           @RequestParam("attendReleaseSubId") int attendReleaseSubId, int type) {
        AjaxUtil<AttendUsersBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        if (attendReleaseService.canReview(users.getUsername(), attendReleaseId)) {
            ajaxUtil = attendUsersService.data(attendReleaseSubId, type);
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
