package top.zbeboy.zone.api.attend;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;
import top.zbeboy.zone.service.attend.AttendReleaseSubService;
import top.zbeboy.zone.service.attend.AttendUsersService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AttendUsersApiController {

    @Resource
    private AttendUsersService attendUsersService;

    @Resource
    private AttendReleaseSubService attendReleaseSubService;

    /**
     * 获取签到名单数据
     *
     * @param attendReleaseSubId 签到子表ID
     * @param type               数据类型 1:已签到 2:未签到 0:全部
     * @return 数据
     */
    @GetMapping("/api/attend/users/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("attendReleaseSubId") int attendReleaseSubId, int type) {
        AjaxUtil<AttendUsers> ajaxUtil = AjaxUtil.of();
        // 根据子表id查询主表id
        AttendReleaseSub attendReleaseSub = attendReleaseSubService.findById(attendReleaseSubId);
        if (Objects.nonNull(attendReleaseSub)) {
            List<AttendUsers> attendUsers = new ArrayList<>();
            // 已签到数据
            if (type == 1) {
                Result<AttendUsersRecord> records = attendUsersService.findHasAttendedStudent(attendReleaseSub.getAttendReleaseId(), attendReleaseSubId);
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsers.class);
                }
            } else if (type == 2) {
                // 未签到数据
                Result<AttendUsersRecord> records = attendUsersService.findNotAttendedStudent(attendReleaseSub.getAttendReleaseId(), attendReleaseSubId);
                if (records.isNotEmpty()) {
                    attendUsers = records.into(AttendUsers.class);
                }
            } else {
                attendUsers = attendUsersService.findByAttendReleaseId(attendReleaseSub.getAttendReleaseId());
            }

            ajaxUtil.success().list(attendUsers).msg("获取数据成功");
        } else {
            ajaxUtil.fail().msg("根据ID未查询到签到发布子表数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
