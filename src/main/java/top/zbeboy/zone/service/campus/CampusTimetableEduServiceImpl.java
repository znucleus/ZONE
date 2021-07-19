package top.zbeboy.zone.service.campus;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.feign.campus.timetable.CampusTimetableService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseDataAddVo;
import top.zbeboy.zbase.vo.campus.timetable.CampusCourseReleaseAddVo;
import top.zbeboy.zone.service.educational.TimetableService;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service("campusTimetableEduService")
public class CampusTimetableEduServiceImpl implements CampusTimetableEduService {

    @Resource
    private CampusTimetableService campusTimetableService;

    @Resource
    private TimetableService timetableService;

    @Override
    public void sync(String username, String password, String targetUsername, int semesterId, HttpServletRequest request) throws Exception {
        Map<String, Object> eduData = timetableService.eduData(username, password, false, semesterId);
        dealSemester(eduData, targetUsername, request);
        dealCourse(eduData, targetUsername);
    }

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

        LocalDate calendarStartDate = null;
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

                LocalTime startTime = campusCourseData.getStartTime();
                if (Objects.isNull(startTime)) {
                    startTime = DateTimeUtil.getNowLocalTime();
                }

                LocalTime endTime = campusCourseData.getEndTime();
                if (Objects.isNull(endTime)) {
                    endTime = DateTimeUtil.getNowLocalTime();
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
                        calendarStartDate = DateTimeUtil.getNowLocalDate();
                    }
                }

                // 1.日历偏移开始周数 算开始时间
                java.util.Date deviationCalendarStartDate = timetableService.calcDeviationDate(calendarStartDate, startWeek, weekDay);
                String finishStartDate = DateTimeUtil.formatUtilDate(deviationCalendarStartDate, DateTimeUtil.YEAR_MONTH_DAY_FORMAT) + " " + DateTimeUtil.defaultFormatLocalTime(startTime);
                startDate.setTime(DateTimeUtil.parseUtilDate(finishStartDate, DateTimeUtil.STANDARD_FORMAT));

                String finishEndDate = DateTimeUtil.formatUtilDate(deviationCalendarStartDate, DateTimeUtil.YEAR_MONTH_DAY_FORMAT) + " " + DateTimeUtil.defaultFormatLocalTime(endTime);
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
                    startTimeContent = DateTimeUtil.defaultFormatLocalTime(campusCourseData.getStartTime());
                }

                String endTimeContent = "";
                if (Objects.nonNull(campusCourseData.getEndTime())) {
                    endTimeContent = DateTimeUtil.defaultFormatLocalTime(campusCourseData.getEndTime());
                }

                String description = startWeekContent + "-" + endWeekContent + "周 " + startTimeContent + "-" + endTimeContent;

                meeting.getProperties().add(new Description(description));

                int count = endWeek - startWeek;

                // 重复事件
                if (count > 0) {
                    Recur recur = new Recur(Recur.Frequency.WEEKLY, 1);
                    recur.getDayList().add(timetableService.getWeekday(campusCourseData.getWeekday()));
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

    public void dealSemester(Map<String, Object> eduData, String targetUsername, HttpServletRequest request) throws Exception {
        Boolean hasError = (Boolean) eduData.get("hasError");
        if (!hasError) {
            CampusCourseReleaseAddVo campusCourseReleaseAddVo = new CampusCourseReleaseAddVo();
            campusCourseReleaseAddVo.setUsername(targetUsername);
            String id = UUIDUtil.getUUID();
            campusCourseReleaseAddVo.setCampusCourseReleaseId(id);
            String realPath = RequestUtil.getRealPath(request);
            String path = Workbook.campusTimetableQrCodeFilePath() + id + ".jpg";
            String logoPath = Workbook.SYSTEM_LOGO_PATH;
            //生成二维码
            String text = RequestUtil.getBaseUrl(request) + CampusUrlCommon.ANYONE_TIMETABLE_LOOK_URL + id;
            QRCodeUtil.encode(text, logoPath, realPath + path, true);
            campusCourseReleaseAddVo.setQrCodeUrl(path);

            String name = (String) eduData.get("name");
            String schoolYear = (String) eduData.get("schoolYear");
            campusCourseReleaseAddVo.setTitle(name);
            campusCourseReleaseAddVo.setSchoolYear(schoolYear);

            byte semester = 0;
            if (StringUtils.contains(name, "-")) {
                name = name.replaceAll(schoolYear, "");
                name = name.replaceAll("-", "");
                if (NumberUtils.isDigits(name)) {
                    semester = NumberUtils.toByte(name);
                }
            }

            campusCourseReleaseAddVo.setSemester(semester);
            campusCourseReleaseAddVo.setStartDate((String) eduData.get("startDate"));
            campusCourseReleaseAddVo.setEndDate((String) eduData.get("endDate"));

            eduData.put("campusCourseReleaseId", id);
            campusTimetableService.save(campusCourseReleaseAddVo);
        }
    }

    /**
     * 学生以班级同步
     *
     * @param eduData 数据
     */
    public void dealCourse(Map<String, Object> eduData, String targetUsername) {
        Boolean hasError = (Boolean) eduData.get("hasError");
        if (!hasError) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) eduData.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> info : data) {
                    List<Map<String, Object>> courseData = (List<Map<String, Object>>) info.get("data");
                    if (CollectionUtils.isNotEmpty(courseData)) {
                        String campusCourseReleaseId = (String) eduData.get("campusCourseReleaseId");
                        List<CampusCourseDataAddVo> insertData = new ArrayList<>();
                        for (Map<String, Object> param : courseData) {
                            String courseName = String.valueOf(param.get("courseName"));
                            String lessonName = String.valueOf(param.get("lessonName"));
                            String room = String.valueOf(param.get("room"));
                            int startWeek = NumberUtils.toInt(String.valueOf(param.get("startWeek")));
                            int endWeek = NumberUtils.toInt(String.valueOf(param.get("endWeek")));
                            Byte weekday = NumberUtils.toByte(String.valueOf(param.get("weekday")));
                            String startTime = String.valueOf(param.get("startTime"));
                            String endTime = String.valueOf(param.get("endTime"));
                            String teachers = String.valueOf(param.get("teachers"));

                            CampusCourseDataAddVo campusCourseDataAddVo = new CampusCourseDataAddVo();
                            campusCourseDataAddVo.setCampusCourseReleaseId(campusCourseReleaseId);
                            campusCourseDataAddVo.setOrganizeName(lessonName);
                            campusCourseDataAddVo.setCourseName(courseName);
                            campusCourseDataAddVo.setUsername(targetUsername);
                            campusCourseDataAddVo.setBuildingName(room);
                            campusCourseDataAddVo.setStartWeek(startWeek);
                            campusCourseDataAddVo.setEndWeek(endWeek);
                            campusCourseDataAddVo.setWeekday(weekday);
                            campusCourseDataAddVo.setStartTime(startTime);
                            campusCourseDataAddVo.setEndTime(endTime);
                            campusCourseDataAddVo.setTeacherName(teachers);

                            insertData.add(campusCourseDataAddVo);
                        }

                        campusTimetableService.courseBatchSave(insertData);

                    }

                }
            }


        }
    }
}
