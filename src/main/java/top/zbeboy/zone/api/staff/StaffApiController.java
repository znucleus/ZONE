package top.zbeboy.zone.api.staff;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class StaffApiController {

    @Resource
    private StaffService staffService;

    /**
     * API:获取教职工信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教职工数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/staff")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                Map<String, Object> outPut = new HashMap<>();
                outPut.put("staffId", bean.getStaffId());
                outPut.put("staffNumber", bean.getStaffNumber());
                outPut.put("departmentId", bean.getDepartmentId());
                outPut.put("schoolId", bean.getSchoolId());
                ajaxUtil.success().msg("获取用户信息成功").map(outPut);
            } else {
                ajaxUtil.fail().msg("未查询到教职工信息");
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
