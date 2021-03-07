package top.zbeboy.zone.service.educational;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

import javax.annotation.Resource;
import java.util.*;

@Service("timetableService")
public class TimetableServiceImpl implements TimetableService {

    @Resource
    private TimetableSemesterService timetableSemesterService;

    @Resource
    private TimetableCourseService timetableCourseService;

    @Override
    public void syncWithStudent(String username, String password) throws Exception {
        Map<String, Object> eduData = eduData(username, password);
        dealSemester(eduData);
        dealCourseWithStudent(eduData);
    }

    public void dealSemester(Map<String, Object> eduData) {
        Integer timetableSemesterId = (Integer) eduData.get("timetableSemesterId");
        if (Objects.nonNull(timetableSemesterId) && timetableSemesterId > 0) {
            // 查询我方是否有，没有则插入
            TimetableSemester timetableSemester = timetableSemesterService.findById(timetableSemesterId);
            if (Objects.nonNull(timetableSemester)) {
                // 有则更新
                boolean isUpdate = false;
                if (Objects.nonNull(eduData.get("schoolYear"))) {
                    timetableSemester.setSchoolYear((String) eduData.get("schoolYear"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("name"))) {
                    timetableSemester.setName((String) eduData.get("name"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("code"))) {
                    timetableSemester.setCode((String) eduData.get("code"));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("startDate"))) {
                    timetableSemester.setStartDate(DateTimeUtil.parseSqlDate((String) eduData.get("startDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    isUpdate = true;
                }

                if (Objects.nonNull(eduData.get("endDate"))) {
                    timetableSemester.setEndDate(DateTimeUtil.parseSqlDate((String) eduData.get("endDate"), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    isUpdate = true;
                }

                if (isUpdate) {
                    timetableSemesterService.update(timetableSemester);
                }
            } else {
                timetableSemester = new TimetableSemester();
                timetableSemester.setTimetableSemesterId(timetableSemesterId);
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
                timetableSemesterService.save(timetableSemester);
            }
        }
    }

    /**
     * 学生以班级同步
     *
     * @param eduData 数据
     */
    public void dealCourseWithStudent(Map<String, Object> eduData) {
        Integer timetableSemesterId = (Integer) eduData.get("timetableSemesterId");
        if (Objects.nonNull(timetableSemesterId) && timetableSemesterId > 0) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) eduData.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> info : data) {
                    String adminclass = (String) info.get("adminclass");
                    List<Map<String, Object>> courseData = (List<Map<String, Object>>) info.get("data");
                    if (CollectionUtils.isNotEmpty(courseData)) {
                        // 删除旧课程
                        timetableCourseService.deleteTimetableCourseByTimetableSemesterIdAndLessonName(timetableSemesterId, adminclass);
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
                            timetableCourse.setTeachers(teachers);
                            timetableCourse.setCredits(credits);
                            timetableCourse.setLessonCode(lessonCode);
                            timetableCourse.setCourseCode(courseCode);
                            timetableCourse.setLessonId(lessonId);
                            timetableCourse.setTimetableSemesterId(timetableSemesterId);
                            insertData.add(timetableCourse);
                        }

                        timetableCourseService.batchSave(insertData);
                    }

                }
            }


        }
    }

    public Map<String, Object> eduData(String username, String password) throws Exception {
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
                        Map<String, Object> semesters = getSemesters(courseTableResult);
                        Integer semesterId = (Integer) semesters.get("id");
                        result.put("timetableSemesterId", semesterId);
                        result.put("schoolYear", semesters.get("schoolYear"));
                        result.put("name", semesters.get("name"));
                        result.put("code", semesters.get("code"));
                        result.put("startDate", semesters.get("startDate"));
                        result.put("endDate", semesters.get("endDate"));

                        if (Objects.nonNull(semesterId) && semesterId > 0) {
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

    private Map<String, Object> getSemesters(String str) {
        Map<String, Object> params = new HashMap<>();
        Document doc = Jsoup.parse(str, CharEncoding.UTF_8);

        String valueId = "";
        boolean isV = false;
        Elements allSemestersSelects = doc.getElementsByAttributeValue("id", "allSemesters");
        for (Element as : allSemestersSelects) {
            Elements options = as.getElementsByTag("option");
            for (Element op : options) {
                String selected = op.attr("selected");
                String value = op.attr("value");
                String text = op.text();
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
            params.put("id", NumberUtils.toInt(valueId));
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
                    if (NumberUtils.toInt(valueId) == j1.getIntValue("id")) {
                        params.put("schoolYear", j1.getString("schoolYear"));
                        params.put("name", j1.getString("name"));
                        params.put("code", j1.getString("code"));
                        params.put("startDate", j1.getString("startDate"));
                        params.put("endDate", j1.getString("endDate"));
                        break;
                    }
                }
            }
        }
        return params;
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
                    JSONObject j2 = arr2.getJSONObject(i);
                    map.put("courseName", j2.getString("courseName"));
                    map.put("lessonName", j2.getString("lessonName"));
                    map.put("building", j2.getString("building"));
                    map.put("room", j2.getString("room"));

                    String weeksStr = j2.getString("weeksStr");
                    if (StringUtils.isNotBlank(weeksStr) && StringUtils.contains(weeksStr, "~")) {
                        String[] ws = weeksStr.split("~");
                        map.put("startWeek", ws[0]);
                        map.put("endWeek", ws[1]);
                    } else {
                        map.put("startWeek", weeksStr);
                    }

                    map.put("weekday", j2.getString("weekday"));
                    map.put("startUnit", j2.getString("startUnit"));
                    map.put("endUnit", j2.getString("endUnit"));

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
                    map.put("timetableSemesterId", id);

                    secondList.add(map);
                }
                info.put("data", secondList);
                firstList.add(info);
            }
        }
        return firstList;
    }
}
