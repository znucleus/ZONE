package top.zbeboy.zone.api.educational.calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.educational.calendar.SchoolCalendarBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SchoolCalendar;
import top.zbeboy.zbase.feign.educational.calendar.SchoolCalendarService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CalendarApiController {

    @Resource
    private SchoolCalendarService schoolCalendarService;

    /**
     * 查询最近数据
     *
     * @param collegeId 院id
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务校历数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/calendars")
    public ResponseEntity<Map<String, Object>> calendars(@RequestParam("collegeId") int collegeId, Principal principal, HttpServletRequest request) {
        AjaxUtil<SchoolCalendar> ajaxUtil = AjaxUtil.of();
        List<SchoolCalendar> schoolCalendars = schoolCalendarService.findByCollegeIdRecently(collegeId);
        ajaxUtil.success().list(schoolCalendars).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查看
     *
     * @param calendarId id
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务校历查看", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/calendar/look")
    public ResponseEntity<Map<String, Object>> look(@RequestParam("calendarId") String calendarId, Principal principal, HttpServletRequest request) {
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
