package top.zbeboy.zone.service.educational;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.feign.educational.timetable.EducationalTimetableService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service("timetableService")
public class TimetableServiceImpl implements TimetableService {

    @Resource
    private EducationalTimetableService educationalTimetableService;

    @Override
    public List<Map<String, Object>> semesters(String username, String password) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> eduData = eduData(username, password, true, 0);
        Boolean hasError = (Boolean) eduData.get("hasError");
        if (!hasError) {
            list = (List<Map<String, Object>>) eduData.get("data");
        }
        return list;
    }

    @Override
    public Map<String, Object> data(String username, String password, int semesterId) throws Exception {
        return eduData(username, password, false, semesterId);
    }

    @Override
    public void syncWithStudent(String username, String password, int collegeId, int semesterId) throws Exception {
        Map<String, Object> eduData = eduData(username, password, false, semesterId);
        dealSemester(eduData, collegeId);
        dealCourseWithStudent(eduData);
    }

    @Override
    public void generateIcs(List<TimetableCourse> timetableCourses, String path) throws IOException {
        // Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Timetable Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        net.fortuna.ical4j.model.TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
        VTimeZone tz = timezone.getVTimeZone();

        java.sql.Date calendarStartDate = null;
        for (TimetableCourse timetableCourse : timetableCourses) {
            // Start Date is on: April 1, 2008, 9:00 am
            java.util.Calendar startDate = new GregorianCalendar();
            startDate.setTimeZone(timezone);

            // End Date is on: April 1, 2008, 13:00
            java.util.Calendar endDate = new GregorianCalendar();
            endDate.setTimeZone(timezone);

            String courseName = timetableCourse.getCourseName();
            String room = timetableCourse.getRoom();

            java.sql.Time startTime = timetableCourse.getStartTime();
            if (Objects.isNull(startTime)) {
                startTime = DateTimeUtil.getNowSqlTime();
            }

            java.sql.Time endTime = timetableCourse.getEndTime();
            if (Objects.isNull(endTime)) {
                endTime = DateTimeUtil.getNowSqlTime();
            }

            Byte startWeek = timetableCourse.getStartWeek();
            if (Objects.isNull(startWeek)) {
                startWeek = 1;
            }
            Byte endWeek = timetableCourse.getEndWeek();
            if (Objects.isNull(endWeek)) {
                endWeek = 1;
            }

            Byte weekDay = timetableCourse.getWeekday();
            if (Objects.isNull(weekDay)) {
                weekDay = 1;
            }

            if (Objects.isNull(calendarStartDate)) {
                Optional<TimetableSemester> optionalTimetableSemester = educationalTimetableService.findSemesterById(timetableCourse.getTimetableSemesterId());
                if (optionalTimetableSemester.isPresent() && Objects.nonNull(optionalTimetableSemester.get().getStartDate())) {
                    calendarStartDate = optionalTimetableSemester.get().getStartDate();
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
            meeting.getProperties().add(new Location(room));

            String startWeekContent = "";
            if (Objects.nonNull(timetableCourse.getStartWeek())) {
                startWeekContent = timetableCourse.getStartWeek() + "";
            }

            String endWeekContent = "";
            if (Objects.nonNull(timetableCourse.getEndWeek())) {
                endWeekContent = timetableCourse.getEndWeek() + "";
            }

            String startTimeContent = DateTimeUtil.defaultFormatSqlTime(startTime);

            String endTimeContent = DateTimeUtil.defaultFormatSqlTime(endTime);

            String description = startWeekContent + "-" + endWeekContent + "周 " + startTimeContent + "-" + endTimeContent;

            meeting.getProperties().add(new Description(description));

            int count = endWeek - startWeek;

            // 重复事件
            if (count > 0) {
                Recur recur = new Recur(Recur.Frequency.WEEKLY, count + 1);
                recur.getDayList().add(getWeekday(timetableCourse.getWeekday()));
                RRule rule = new RRule(recur);
                meeting.getProperties().add(rule);
            }

            // 提醒,提前15分钟
            VAlarm valarm = new VAlarm(java.time.Duration.ofMinutes(-15));
            valarm.getProperties().add(new Summary(courseName));
            valarm.getProperties().add(Action.DISPLAY);
            valarm.getProperties().add(new Location(room));
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

        File saveFile = new File(path);
        if (!saveFile.getParentFile().exists()) {//create file
            saveFile.getParentFile().mkdirs();
        }

        FileOutputStream fout = new FileOutputStream(saveFile);
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(icsCalendar, fout);
    }

    public void dealSemester(Map<String, Object> eduData, int collegeId) {
        Boolean hasError = (Boolean) eduData.get("hasError");
        if (!hasError) {
            Integer id = (Integer) eduData.get("id");
            // 查询我方是否有，没有则插入
            Optional<TimetableSemester> optionalTimetableSemester = educationalTimetableService.findSemesterByIdAndCollegeId(id, collegeId);
            if (optionalTimetableSemester.isPresent()) {
                TimetableSemester timetableSemester = optionalTimetableSemester.get();
                eduData.put("timetableSemesterId", timetableSemester.getTimetableSemesterId());
                // 有则更新
                boolean isUpdate = false;
                if (Objects.nonNull(eduData.get("schoolYear")) && !StringUtils.equals(timetableSemester.getSchoolYear(), (String) eduData.get("schoolYear"))) {
                    timetableSemester.setSchoolYear((String) eduData.get("schoolYear"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("name")) && !StringUtils.equals(timetableSemester.getName(), (String) eduData.get("name"))) {
                    timetableSemester.setName((String) eduData.get("name"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("code")) && !StringUtils.equals(timetableSemester.getCode(), (String) eduData.get("code"))) {
                    timetableSemester.setCode((String) eduData.get("code"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("startDate"))) {
                    java.sql.Date startDate = DateTimeUtil.parseSqlDate((String) eduData.get("startDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT);
                    if (timetableSemester.getStartDate().compareTo(startDate) != 0) {
                        timetableSemester.setStartDate(startDate);
                        isUpdate = true;
                    }

                }

                if (Objects.nonNull(eduData.get("endDate"))) {
                    java.sql.Date endDate = DateTimeUtil.parseSqlDate((String) eduData.get("endDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT);
                    if (timetableSemester.getEndDate().compareTo(endDate) != 0) {
                        timetableSemester.setEndDate(endDate);
                        isUpdate = true;
                    }
                }

                if (isUpdate) {
                    educationalTimetableService.semesterUpdate(timetableSemester);
                }
            } else {
                TimetableSemester timetableSemester = new TimetableSemester();
                String timetableSemesterId = UUIDUtil.getUUID();
                eduData.put("timetableSemesterId", timetableSemesterId);
                timetableSemester.setTimetableSemesterId(timetableSemesterId);
                timetableSemester.setId(id);
                timetableSemester.setCollegeId(collegeId);
                if (Objects.nonNull(eduData.get("schoolYear"))) {
                    timetableSemester.setSchoolYear((String) eduData.get("schoolYear"));
                }

                if (Objects.nonNull(eduData.get("name"))) {
                    timetableSemester.setName((String) eduData.get("name"));
                }

                if (Objects.nonNull(eduData.get("code"))) {
                    timetableSemester.setCode((String) eduData.get("code"));
                }

                if (Objects.nonNull(eduData.get("startDate"))) {
                    timetableSemester.setStartDate(DateTimeUtil.parseSqlDate((String) eduData.get("startDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                }

                if (Objects.nonNull(eduData.get("endDate"))) {
                    timetableSemester.setEndDate(DateTimeUtil.parseSqlDate((String) eduData.get("endDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                }
                educationalTimetableService.semesterSave(timetableSemester);
            }
        }
    }

    /**
     * 学生以班级同步
     *
     * @param eduData 数据
     */
    public void dealCourseWithStudent(Map<String, Object> eduData) {
        Boolean hasError = (Boolean) eduData.get("hasError");
        if (!hasError) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) eduData.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> info : data) {
                    String adminclass = (String) info.get("adminclass");
                    List<Map<String, Object>> courseData = (List<Map<String, Object>>) info.get("data");
                    if (CollectionUtils.isNotEmpty(courseData)) {
                        // 删除旧课程
                        String timetableSemesterId = (String) eduData.get("timetableSemesterId");
                        AjaxUtil<Map<String, Object>> ajaxUtil = educationalTimetableService.courseDeleteByTimetableSemesterIdAndLessonName(timetableSemesterId, adminclass);
                        if (ajaxUtil.getState()) {
                            List<TimetableCourse> insertData = new ArrayList<>();
                            for (Map<String, Object> param : courseData) {
                                String courseName = String.valueOf(param.get("courseName"));
                                String lessonName = String.valueOf(param.get("lessonName"));
                                String building = String.valueOf(param.get("building"));
                                String room = String.valueOf(param.get("room"));
                                Byte startWeek = NumberUtils.toByte(String.valueOf(param.get("startWeek")));
                                Byte endWeek = NumberUtils.toByte(String.valueOf(param.get("endWeek")));
                                Byte weekday = NumberUtils.toByte(String.valueOf(param.get("weekday")));
                                Byte startUnit = NumberUtils.toByte(String.valueOf(param.get("startUnit")));
                                Byte endUnit = NumberUtils.toByte(String.valueOf(param.get("endUnit")));
                                String startTime = String.valueOf(param.get("startTime"));
                                String endTime = String.valueOf(param.get("endTime"));
                                String teachers = String.valueOf(param.get("teachers"));
                                String credits = String.valueOf(param.get("credits"));
                                String lessonCode = String.valueOf(param.get("lessonCode"));
                                String courseCode = String.valueOf(param.get("courseCode"));
                                String lessonId = String.valueOf(param.get("lessonId"));

                                TimetableCourse timetableCourse = new TimetableCourse();
                                timetableCourse.setTimetableCourseId(UUIDUtil.getUUID());
                                timetableCourse.setCourseName(courseName);
                                timetableCourse.setLessonName(lessonName);
                                timetableCourse.setBuilding(building);
                                timetableCourse.setRoom(room);
                                timetableCourse.setStartWeek(startWeek);
                                timetableCourse.setEndWeek(endWeek);
                                timetableCourse.setWeekday(weekday);
                                timetableCourse.setStartUnit(startUnit);
                                timetableCourse.setEndUnit(endUnit);
                                if (StringUtils.isNotBlank(startTime)) {
                                    timetableCourse.setStartTime(DateTimeUtil.defaultParseSqlTime(startTime));
                                }

                                if (StringUtils.isNotBlank(endTime)) {
                                    timetableCourse.setEndTime(DateTimeUtil.defaultParseSqlTime(endTime));
                                }
                                timetableCourse.setTeachers(teachers);
                                timetableCourse.setCredits(credits);
                                timetableCourse.setLessonCode(lessonCode);
                                timetableCourse.setCourseCode(courseCode);
                                timetableCourse.setLessonId(lessonId);
                                timetableCourse.setTimetableSemesterId(timetableSemesterId);
                                insertData.add(timetableCourse);
                            }

                            educationalTimetableService.courseBatchSave(insertData);
                        }

                    }

                }
            }


        }
    }

    @Override
    public Map<String, Object> eduData(String username, String password, boolean getAllSemesters, int semesterId) throws Exception {
        final String loginSaltUri = "http://cityjw.kust.edu.cn/integration/login-salt";
        final String loginUri = "http://cityjw.kust.edu.cn/integration/login";
        final String courseTableUri = "http://cityjw.kust.edu.cn/integration/for-std/course-table";
        final String semesterUri = "http://cityjw.kust.edu.cn/integration/for-std/course-table/semester/%s/print?semesterId=%s";

        Map<String, Object> result = new HashMap<>();
        result.put("hasError", false);
        CloseableHttpClient client = HttpClients.custom().build();

        HttpGet loginSaltGet = new HttpGet(loginSaltUri);

        HttpResponse response = client.execute(loginSaltGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            //得到实体
            HttpEntity entity = response.getEntity();

            String str = EntityUtils.toString(entity);

            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            params.put("password", DigestUtils.sha1Hex(str + "-" + password));
            params.put("captcha", "");

            HttpPost loginPost = new HttpPost(loginUri);

            StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON);

            loginPost.setEntity(stringEntity);

            HttpResponse loginResponse = client.execute(loginPost);

            if (loginResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = loginResponse.getEntity();
                String loginResult = EntityUtils.toString(responseEntity);
                JSONObject loginJson = JSON.parseObject(loginResult);
                if (loginJson.getBoolean("result")) {
                    HttpGet courseTableGet = new HttpGet(courseTableUri);
                    HttpResponse courseTableResponse = client.execute(courseTableGet);
                    if (courseTableResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity courseTableResponseEntity = courseTableResponse.getEntity();
                        String courseTableResult = EntityUtils.toString(courseTableResponseEntity);
                        if (!getAllSemesters) {
                            Map<String, Object> semesters = getSemesters(courseTableResult, semesterId);
                            result.put("id", semesterId);
                            result.put("schoolYear", semesters.get("schoolYear"));
                            result.put("name", semesters.get("name"));
                            result.put("code", semesters.get("code"));
                            result.put("startDate", semesters.get("startDate"));
                            result.put("endDate", semesters.get("endDate"));

                            if (semesterId > 0) {
                                HttpGet semesterGet = new HttpGet(String.format(semesterUri, semesterId, semesterId));
                                HttpResponse semesterResponse = client.execute(semesterGet);
                                if (semesterResponse.getStatusLine().getStatusCode() == 200) {
                                    HttpEntity semesterResponseEntity = semesterResponse.getEntity();
                                    String semesterResult = EntityUtils.toString(semesterResponseEntity);
                                    List<Map<String, Object>> list = getTableData(semesterId, semesterResult);
                                    result.put("data", list);
                                } else {
                                    result.put("hasError", true);
                                    result.put("statusCode", semesterResponse.getStatusLine().getStatusCode());
                                    result.put("reasonPhrase", semesterResponse.getStatusLine().getReasonPhrase() + "【SEMESTER】");
                                }
                            } else {
                                result.put("hasError", true);
                                result.put("statusCode", "500");
                                result.put("reasonPhrase", "获取学期id空" + "【SEMESTER_ID】");
                            }
                        } else {
                            List<Map<String, Object>> list = getAllSemesters(courseTableResult);
                            result.put("data", list);
                        }

                    } else {
                        result.put("hasError", true);
                        result.put("statusCode", courseTableResponse.getStatusLine().getStatusCode());
                        result.put("reasonPhrase", courseTableResponse.getStatusLine().getReasonPhrase() + "【COURSE_TABLE】");
                    }
                } else {
                    result.put("hasError", true);
                    result.put("statusCode", "500");
                    result.put("reasonPhrase", "登录失败" + "【LOGIN_FAIL】");
                }
            } else {
                result.put("hasError", true);
                result.put("statusCode", loginResponse.getStatusLine().getStatusCode());
                result.put("reasonPhrase", loginResponse.getStatusLine().getReasonPhrase() + "【LOGIN】");
            }
        } else {
            result.put("hasError", true);
            result.put("statusCode", response.getStatusLine().getStatusCode());
            result.put("reasonPhrase", response.getStatusLine().getReasonPhrase() + "【LOGIN_SALT】");
        }
        return result;
    }

    private Map<String, Object> getSemesters(String str, int semesterId) {
        Map<String, Object> params = new HashMap<>();
        Document doc = Jsoup.parse(str, CharEncoding.UTF_8);

        params.put("id", semesterId);
        Elements elements = doc.getElementsByTag("script");

        String semesters = "";
        for (Element element : elements) {

            /*取得JS变量数组*/
            String[] data = StringUtils.deleteWhitespace(element.data().toString()).split("var");
            /*取得单个JS变量*/
            for (String variable : data) {
                if (variable.contains("semesters")) {
                    semesters = variable.substring(variable.indexOf("=") + 1, variable.lastIndexOf(";")).trim();
                    semesters = semesters.substring(semesters.indexOf("'") + 1, semesters.lastIndexOf("'"));
                    semesters = semesters.replaceAll("\\\\\"", "\\\"");
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(semesters)) {
            JSONArray arr1 = JSON.parseArray(semesters);
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject j1 = arr1.getJSONObject(i);
                if (semesterId == j1.getIntValue("id")) {
                    params.put("schoolYear", j1.getString("schoolYear"));
                    params.put("name", j1.getString("name"));
                    params.put("code", j1.getString("code"));
                    params.put("startDate", j1.getString("startDate"));
                    params.put("endDate", j1.getString("endDate"));
                    break;
                }
            }
        }
        return params;
    }

    private List<Map<String, Object>> getAllSemesters(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        Document doc = Jsoup.parse(str, CharEncoding.UTF_8);

        String valueId = "";
        boolean isV = false;
        Elements allSemestersSelects = doc.getElementsByAttributeValue("id", "allSemesters");
        for (Element as : allSemestersSelects) {
            Elements options = as.getElementsByTag("option");
            for (Element op : options) {
                String selected = op.attr("selected");
                String value = op.attr("value");
                if (!isV) {
                    isV = true;
                    valueId = value;
                }
                if (StringUtils.equals(selected, "selected")) {
                    valueId = value;
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(valueId)) {
            Elements elements = doc.getElementsByTag("script");

            String semesters = "";
            for (Element element : elements) {

                /*取得JS变量数组*/
                String[] data = StringUtils.deleteWhitespace(element.data().toString()).split("var");
                /*取得单个JS变量*/
                for (String variable : data) {
                    if (variable.contains("semesters")) {
                        semesters = variable.substring(variable.indexOf("=") + 1, variable.lastIndexOf(";")).trim();
                        semesters = semesters.substring(semesters.indexOf("'") + 1, semesters.lastIndexOf("'"));
                        semesters = semesters.replaceAll("\\\\\"", "\\\"");
                        break;
                    }
                }
            }

            if (StringUtils.isNotBlank(semesters)) {
                JSONArray arr1 = JSON.parseArray(semesters);
                for (int i = 0; i < arr1.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONObject j1 = arr1.getJSONObject(i);

                    map.put("id", j1.getString("id"));
                    map.put("schoolYear", j1.getString("schoolYear"));
                    map.put("name", j1.getString("name"));
                    map.put("code", j1.getString("code"));
                    map.put("startDate", j1.getString("startDate"));
                    map.put("endDate", j1.getString("endDate"));

                    if (NumberUtils.toInt(valueId) == j1.getIntValue("id")) {
                        map.put("selected", true);
                    } else {
                        map.put("selected", false);
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }

    private List<Map<String, Object>> getTableData(Integer id, String str) {
        List<Map<String, Object>> firstList = new ArrayList<>();
        Document doc = Jsoup.parse(str, CharEncoding.UTF_8);
        Elements elements = doc.getElementsByTag("script");

        String studentTableVms = "";
        for (Element element : elements) {

            /*取得JS变量数组*/
            String[] data = element.data().toString().split("var");
            /*取得单个JS变量*/
            for (String variable : data) {
                if (variable.contains("studentTableVms")) {
                    studentTableVms = variable.substring(variable.indexOf("=") + 1, variable.lastIndexOf(";")).trim();
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(studentTableVms)) {
            studentTableVms = studentTableVms.replaceAll("\"", "@");
            studentTableVms = studentTableVms.replaceAll("'", "\\\"");

            JSONArray arr1 = JSON.parseArray(studentTableVms);
            for (int i = 0; i < arr1.size(); i++) {
                Map<String, Object> info = new HashMap<>();
                JSONObject j1 = arr1.getJSONObject(i);
                JSONArray arr2 = j1.getJSONArray("activities");
                info.put("adminclass", j1.getString("adminclass"));
                List<Map<String, Object>> secondList = new ArrayList<>();
                for (int j = 0; j < arr2.size(); j++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONObject j2 = arr2.getJSONObject(j);
                    map.put("courseName", StringUtils.defaultString(j2.getString("courseName")));
                    map.put("lessonName", StringUtils.defaultString(j2.getString("lessonName")));
                    map.put("building", StringUtils.defaultString(j2.getString("building")));
                    map.put("room", StringUtils.defaultString(j2.getString("room")));

                    String weeksStr = j2.getString("weeksStr");
                    if (StringUtils.isNotBlank(weeksStr) && StringUtils.contains(weeksStr, "~")) {
                        String[] ws = weeksStr.split("~");
                        map.put("startWeek", ws[0]);
                        map.put("endWeek", ws[1]);
                    } else {
                        map.put("startWeek", weeksStr);
                    }

                    map.put("weekday", StringUtils.defaultString(j2.getString("weekday")));
                    map.put("startUnit", j2.getString("startUnit"));
                    map.put("endUnit", j2.getString("endUnit"));
                    map.put("startTime", getStartTime(j2.getString("startUnit")));
                    map.put("endTime", getEndTime(j2.getString("endUnit")));

                    StringBuilder sb = new StringBuilder();
                    JSONArray arr3 = j2.getJSONArray("teachers");
                    for (int k = 0; k < arr3.size(); k++) {
                        sb.append(arr3.getString(k)).append(" ");
                    }
                    map.put("teachers", sb.toString());
                    map.put("credits", j2.getString("credits"));
                    map.put("lessonCode", j2.getString("lessonCode"));
                    map.put("courseCode", j2.getString("courseCode"));
                    map.put("lessonId", j2.getString("lessonId"));
                    map.put("id", id);

                    secondList.add(map);
                }
                info.put("data", secondList);
                firstList.add(info);
            }
        }
        return firstList;
    }

    /**
     * 计算时间偏移
     *
     * @param date    开始日期
     * @param week    偏移周
     * @param weekDay 那一周的周几
     * @return 时间
     */
    @Override
    public java.util.Date calcDeviationDate(java.sql.Date date, int week, int weekDay) {
        int deviationWeek = week - 1;
        org.joda.time.DateTime dt1 = new org.joda.time.DateTime(date);
        if (deviationWeek > 0) {
            dt1 = dt1.plusWeeks(deviationWeek);
        }
        dt1 = dt1.withDayOfWeek(weekDay);
        return dt1.toDate();
    }

    @Override
    public WeekDay getWeekday(int weekday) {
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

    @Override
    public String getStartTime(String startUnit) {
        String su = "";
        if (StringUtils.isNotBlank(startUnit)) {
            switch (NumberUtils.toInt(startUnit)) {
                case 1:
                    su = "08:00:00";
                    break;
                case 2:
                    su = "08:50:00";
                    break;
                case 3:
                    su = "09:50:00";
                    break;
                case 4:
                    su = "10:40:00";
                    break;
                case 5:
                    su = "11:30:00";
                    break;
                case 6:
                    su = "13:30:00";
                    break;
                case 7:
                    su = "14:20:00";
                    break;
                case 8:
                    su = "15:20:00";
                    break;
                case 9:
                    su = "16:10:00";
                    break;
                case 10:
                    su = "17:00:00";
                    break;
                case 11:
                    su = "19:00:00";
                    break;
                case 12:
                    su = "19:50:00";
                    break;
            }
        }
        return su;

    }

    public String getEndTime(String endUnit) {
        String eu = "";
        if (StringUtils.isNotBlank(endUnit)) {
            switch (NumberUtils.toInt(endUnit)) {
                case 1:
                    eu = "08:45:00";
                    break;
                case 2:
                    eu = "09:35:00";
                    break;
                case 3:
                    eu = "10:35:00";
                    break;
                case 4:
                    eu = "11:25:00";
                    break;
                case 5:
                    eu = "12:15:00";
                    break;
                case 6:
                    eu = "14:15:00";
                    break;
                case 7:
                    eu = "15:05:00";
                    break;
                case 8:
                    eu = "16:05:00";
                    break;
                case 9:
                    eu = "16:55:00";
                    break;
                case 10:
                    eu = "17:45:00";
                    break;
                case 11:
                    eu = "19:45:00";
                    break;
                case 12:
                    eu = "20:35:00";
                    break;
            }
        }
        return eu;

    }
}
