package top.zbeboy.zone.api.staff;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class StaffApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private StaffService staffService;

    /**
     * API:获取教职工信息
     *
     * @param principal 用户
     * @return 数据
     */
    @GetMapping("/api/staff")
    public ResponseEntity<Map<String, Object>> users(Principal principal) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<StaffRecord> record = staffService.findByUsername(users.getUsername());
            if (record.isPresent()) {
                Map<String, Object> outPut = new HashMap<>();
                Staff staff = record.get().into(Staff.class);
                outPut.put("staffId", staff.getStaffId());
                outPut.put("staffNumber", staff.getStaffNumber());
                outPut.put("departmentId", staff.getDepartmentId());
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
