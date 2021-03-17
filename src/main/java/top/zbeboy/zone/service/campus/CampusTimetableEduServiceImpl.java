package top.zbeboy.zone.service.campus;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.feign.campus.timetable.CampusTimetableService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("campusTimetableEduService")
public class CampusTimetableEduServiceImpl implements CampusTimetableEduService {

    @Resource
    private CampusTimetableService campusTimetableService;

    @Override
    public void generateIcs(List<CampusCourseData> campusCourseDataList, String path) throws IOException {
        // Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Timetable Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);

        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
        VTimeZone tz = timezone.getVTimeZone();

        java.sql.Date calendarStartDate = null;
        if (CollectionUtils.isNotEmpty(campusCourseDataList)) {
            for (CampusCourseData campusCourseData : campusCourseDataList) {
                // Start Date is on: April 1, 2008, 9:00 am
                java.util.Calendar startDate = new GregorianCalendar();
                startDate.setTimeZone(timezone);

                // End Date is on: April 1, 2008, 13:00
                java.util.Calendar endDate = new GregorianCalendar();
                endDate.setTimeZone(timezone);

                String courseName = campusCourseData.getCourseName();
                String buildingName = campusCourseData.getBuildingName();

                java.sql.Time startTime = campusCourseData.getStartTime();
                if (Objects.isNull(startTime)) {
                    startTime = DateTimeUtil.getNowSqlTime();
                }

                java.sql.Time endTime = campusCourseData.getEndTime();
                if (Objects.isNull(endTime)) {
                    endTime = DateTimeUtil.getNowSqlTime();
                }

                Integer startWeek = campusCourseData.getStartWeek();
                if (Objects.isNull(startWeek)) {
                    startWeek = 1;
                }
                Integer endWeek = campusCourseData.getEndWeek();
                if (Objects.isNull(endWeek)) {
                    endWeek = 1;
                }

                Byte weekDay = campusCourseData.getWeekday();
                if (Objects.isNull(weekDay)) {
                    weekDay = 1;
                }

                if (Objects.isNull(calendarStartDate)) {
                    Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(campusCourseData.getCampusCourseReleaseId());
                    if (optionalCampusCourseRelease.isPresent() && Objects.nonNull(optionalCampusCourseRelease.get().getStartDate())) {
                        calendarStartDate = optionalCampusCourseRelease.get().getStartDate();
                    } else {
                        calendarStartDate = DateTimeUtil.getNowSqlDate();
                    }
                }

                // 1.日历偏移开始周数 算开始时间
                java.util.Date deviationCalendarStartDate = calcDeviationDate(calendarStartDate, startWeek, weekDay);
                String finishStartDate = DateTimeUtil.formatUtilDate(deviationCalendarStartDate, DateTimeUtil.YEAR_MONTH_DAY_FORMAT) + " " + DateTimeUtil.defaultFormatSqlTime(startTime);
                startDate.setTime(DateTimeUtil.parseUtilDate(finishStartDate, DateTimeUtil.STANDARD_FORMAT));

                String finishEndDate = DateTimeUtil.formatUtilDate(deviationCalendarStartDate, DateTimeUtil.YEAR_MONTH_DAY_FORMAT) + " " + DateTimeUtil.defaultFormatSqlTime(endTime);
                endDate.setTime(DateTimeUtil.parseUtilDate(finishEndDate, DateTimeUtil.STANDARD_FORMAT));

                // Create the event
                DateTime start = new DateTime(startDate.getTime());
                DateTime end = new DateTime(endDate.getTime());
                VEvent meeting = new VEvent(start, end, courseName);
                meeting.getProperties().add(new Location(buildingName));

                String startWeekContent = "";
                if (Objects.nonNull(campusCourseData.getStartWeek())) {
                    startWeekContent = campusCourseData.getStartWeek() + "";
                }

                String endWeekContent = "";
                if (Objects.nonNull(campusCourseData.getEndWeek())) {
                    endWeekContent = campusCourseData.getEndWeek() + "";
                }

                String startTimeContent = "";
                if (Objects.nonNull(campusCourseData.getStartTime())) {
                    startTimeContent = DateTimeUtil.defaultFormatSqlTime(campusCourseData.getStartTime());
                }

                String endTimeContent = "";
                if (Objects.nonNull(campusCourseData.getEndTime())) {
                    endTimeContent = DateTimeUtil.defaultFormatSqlTime(campusCourseData.getEndTime());
                }

                String description = startWeekContent + "-" + endWeekContent + "周 " + startTimeContent + "-" + endTimeContent;

                meeting.getProperties().add(new Description(description));

                int count = endWeek - startWeek;

                // 重复事件
                if (count > 0) {
                    Recur recur = new Recur(Recur.Frequency.WEEKLY, count + 1);
                    recur.getDayList().add(getWeekday(campusCourseData.getWeekday()));
                    RRule rule = new RRule(recur);
                    meeting.getProperties().add(rule);
                }

                // 提醒,提前15分钟
                VAlarm valarm = new VAlarm(java.time.Duration.ofMinutes(-15));
                valarm.getProperties().add(new Summary(courseName));
                valarm.getProperties().add(Action.DISPLAY);
                valarm.getProperties().add(new Location(buildingName));
                valarm.getProperties().add(new Description(description));
                // 将VAlarm加入VEvent
                meeting.getAlarms().add(valarm);

                // add timezone info..
                meeting.getProperties().add(tz.getTimeZoneId());

                // generate unique identifier..
                UidGenerator ug = new RandomUidGenerator();
                Uid uid = ug.generateUid();
                meeting.getProperties().add(uid);

                // Add the event and print
                icsCalendar.getComponents().add(meeting);
                // 验证
                icsCalendar.validate();
            }
        }


        File saveFile = new File(path);
        if (!saveFile.getParentFile().exists()) {//create file
            saveFile.getParentFile().mkdirs();
        }

        FileOutputStream fout = new FileOutputStream(saveFile);
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(icsCalendar, fout);
    }

    /**
     * 计算时间偏移
     *
     * @param date    开始日期
     * @param week    偏移周
     * @param weekDay 那一周的周几
     * @return 时间
     */
    private java.util.Date calcDeviationDate(java.sql.Date date, int week, int weekDay) {
        int deviationWeek = week - 1;
        org.joda.time.DateTime dt1 = new org.joda.time.DateTime(date);
        if (deviationWeek > 0) {
            dt1 = dt1.plusWeeks(deviationWeek);
        }
        dt1 = dt1.withDayOfWeek(weekDay);
        return dt1.toDate();
    }

    private WeekDay getWeekday(int weekday) {
        WeekDay wv;
        switch (weekday) {
            case 1:
                wv = WeekDay.MO;
                break;
            case 2:
                wv = WeekDay.TU;
                break;
            case 3:
                wv = WeekDay.WE;
                break;
            case 4:
                wv = WeekDay.TH;
                break;
            case 5:
                wv = WeekDay.FR;
                break;
            case 6:
                wv = WeekDay.SA;
                break;
            case 7:
                wv = WeekDay.SU;
                break;
            default:
                wv = WeekDay.MO;
        }
        return wv;
    }
}
