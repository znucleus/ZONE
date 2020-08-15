package top.zbeboy.zone.web.campus.roster;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.roster.RosterReleaseBean;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CampusRosterRestController {

    @Resource
    private RosterReleaseService rosterReleaseService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/campus/roster/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<RosterReleaseBean> ajaxUtil = rosterReleaseService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
