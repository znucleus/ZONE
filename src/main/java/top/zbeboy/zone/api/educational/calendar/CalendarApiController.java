package top.zbeboy.zone.api.educational.calendar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.educational.calendar.SchoolCalendarBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SchoolCalendar;
import top.zbeboy.zbase.feign.educational.calendar.EducationalCalendarService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CalendarApiController {

    @Resource
    private EducationalCalendarService educationalCalendarService;

    /**
     * 查询最近数据
     *
     * @param collegeId 院id
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务校历数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/calendar/query-with-college-id/{collegeId}")
    public ResponseEntity<Map<String, Object>> calendars(@PathVariable("collegeId") int collegeId, Principal principal, HttpServletRequest request) {
        AjaxUtil<SchoolCalendar> ajaxUtil = AjaxUtil.of();
        Optional<List<SchoolCalendar>> optionalSchoolCalendars = educationalCalendarService.findRecentlyByCollegeId(collegeId);
        if(optionalSchoolCalendars.isPresent()){
            ajaxUtil.success().list(optionalSchoolCalendars.get()).msg("获取数据成功");
        } else {
            ajaxUtil.fail().msg("未查询到校历数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查看
     *
     * @param calendarId id
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务校历查询", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/calendar/query/{calendarId}")
    public ResponseEntity<Map<String, Object>> query(@PathVariable("calendarId") String calendarId, Principal principal, HttpServletRequest request) {
        Optional<SchoolCalendarBean> optionalSchoolCalendarBean = educationalCalendarService.findByIdRelation(calendarId);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (optionalSchoolCalendarBean.isPresent()) {
            SchoolCalendarBean bean = optionalSchoolCalendarBean.get();
            bean.setReleaseTimeStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getReleaseTime()));
            bean.setNowDate(DateTimeUtil.getNowLocalDate());
            bean.setOpenWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getStartDate(), bean.getEndDate()));
            bean.setHolidayWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getHolidayStartDate(), bean.getHolidayEndDate()));
            bean.setWeek(DateTimeUtil.getNowDayOfWeek());
            if (DateTimeUtil.nowRangeLocalDate(bean.getStartDate(), bean.getEndDate())) {
                bean.setWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getStartDate(), bean.getNowDate()));
            } else if (DateTimeUtil.nowRangeLocalDate(bean.getHolidayStartDate(), bean.getHolidayEndDate())) {
                bean.setWeeks(DateTimeUtil.calculationTwoDateDifferWeeks(bean.getHolidayStartDate(), bean.getNowDate()));
            }
            ajaxUtil.success().msg("查询数据成功").put("calendar", bean);
        } else {
            ajaxUtil.fail().msg("无数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
