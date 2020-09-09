package top.zbeboy.zone.web.educational.calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.educational.calendar.SchoolCalendarAuthoritiesBean;
import top.zbeboy.zbase.bean.educational.calendar.SchoolCalendarBean;
import top.zbeboy.zbase.domain.tables.pojos.College;
import top.zbeboy.zbase.domain.tables.pojos.SchoolCalendar;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.educational.calendar.SchoolCalendarService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarAddVo;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarAuthoritiesAddVo;
import top.zbeboy.zbase.vo.educational.calendar.SchoolCalendarEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        if (schoolCalendarService.canRelease(users.getUsername())) {
            dataTablesUtil = schoolCalendarService.data(dataTablesUtil);
        } else {
            dataTablesUtil.setData(new ArrayList<>());
        }
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

    /**
     * 批量删除
     *
     * @param calendarIds ids
     * @return true删除成功
     */
    @PostMapping("/web/educational/calendar/delete")
    public ResponseEntity<Map<String, Object>> delete(String calendarIds) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolCalendarService.delete(users.getUsername(), calendarIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限数据
     *
     * @param tableSawUtil 请求
     * @return 数据
     */
    @GetMapping("/web/educational/calendar/authorize/data")
    public ResponseEntity<Map<String, Object>> authorizeData(TableSawUtil tableSawUtil) {
        Users users = SessionUtil.getUserFromSession();
        tableSawUtil.setUsername(users.getUsername());
        AjaxUtil<SchoolCalendarAuthoritiesBean> ajaxUtil = schoolCalendarService.authorizeData(tableSawUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param schoolCalendarAuthoritiesAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/educational/calendar/authorize/save")
    public ResponseEntity<Map<String, Object>> authorizeSave(SchoolCalendarAuthoritiesAddVo schoolCalendarAuthoritiesAddVo) {
        Users users = SessionUtil.getUserFromSession();
        schoolCalendarAuthoritiesAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolCalendarService.authorizeSave(schoolCalendarAuthoritiesAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 权限id
     * @return true or false
     */
    @PostMapping("/web/educational/calendar/authorize/delete")
    public ResponseEntity<Map<String, Object>> authorizeDelete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolCalendarService.authorizeDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询最近数据
     *
     * @param collegeId 院id
     * @return 数据
     */
    @GetMapping("/web/educational/calendars")
    public ResponseEntity<Map<String, Object>> calendars(@RequestParam("collegeId") int collegeId) {
        Select2Data select2Data = Select2Data.of();
        List<SchoolCalendar> schoolCalendars = schoolCalendarService.findByCollegeIdRecently(collegeId);
        schoolCalendars.forEach(schoolCalendar -> select2Data.add(schoolCalendar.getCalendarId(), schoolCalendar.getTitle()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 查看
     *
     * @param calendarId id
     * @return 数据
     */
    @GetMapping("/web/educational/calendar/look")
    public ResponseEntity<Map<String, Object>> look(@RequestParam("calendarId") String calendarId) {
        SchoolCalendarBean bean = schoolCalendarService.findByIdRelation(calendarId);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Objects.nonNull(bean) && StringUtils.isNotBlank(bean.getCalendarId())) {
            bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime()));
            bean.setNowDate(DateTimeUtil.getNowSqlDate());
            bean.setOpenWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getStartDate(), bean.getEndDate()));
            bean.setHolidayWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getHolidayStartDate(), bean.getHolidayEndDate()));
            bean.setWeek(DateTimeUtil.getNowDayOfWeek());
            if (DateTimeUtil.nowRangeSqlDate(bean.getStartDate(), bean.getEndDate())) {
                bean.setWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getStartDate(), bean.getNowDate()));
            } else if (DateTimeUtil.nowRangeSqlDate(bean.getHolidayStartDate(), bean.getHolidayEndDate())) {
                bean.setWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getHolidayStartDate(), bean.getNowDate()));
            }
            ajaxUtil.success().msg("查询数据成功").put("calendar", bean);
        } else {
            ajaxUtil.fail().msg("无数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
