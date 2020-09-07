package top.zbeboy.zone.web.educational.calendar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.educational.calendar.SchoolCalendarService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarAddVo;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CalendarRestController {

    @Resource
    private SchoolCalendarService schoolCalendarService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/educational/calendar/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("title");
        headers.add("collegeName");
        headers.add("academicYear");
        headers.add("term");
        headers.add("startDate");
        headers.add("endDate");
        headers.add("holidayStartDate");
        headers.add("holidayEndDate");
        headers.add("publisher");
        headers.add("releaseTimeStr");
        headers.add("remark");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(schoolCalendarService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param schoolCalendarAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/calendar/save")
    public ResponseEntity<Map<String, Object>> save(SchoolCalendarAddVo schoolCalendarAddVo, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        schoolCalendarAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolCalendarService.save(schoolCalendarAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param schoolCalendarEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/calendar/update")
    public ResponseEntity<Map<String, Object>> update(SchoolCalendarEditVo schoolCalendarEditVo) {
        Users users = SessionUtil.getUserFromSession();
        schoolCalendarEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolCalendarService.update(schoolCalendarEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

}
