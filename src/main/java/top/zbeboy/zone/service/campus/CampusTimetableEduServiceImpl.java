package top.zbeboy.zone.service.campus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.DigestUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("campusTimetableEduService")
public class CampusTimetableEduServiceImpl implements CampusTimetableEduService {

    @Override
    public Map<String, Object> data(String username, String password) throws Exception {
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
                        String semesterId = getSemesters(courseTableResult);

                        if (StringUtils.isNotBlank(semesterId)) {
                            HttpGet semesterGet = new HttpGet(String.format(semesterUri, semesterId, semesterId));
                            HttpResponse semesterResponse = client.execute(semesterGet);
                            if (semesterResponse.getStatusLine().getStatusCode() == 200) {
                                HttpEntity semesterResponseEntity = semesterResponse.getEntity();
                                String semesterResult = EntityUtils.toString(semesterResponseEntity);
                                List<Map<String, Object>> list = getTableData(semesterResult);
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

    private String getSemesters(String str) {
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
                    valueId = value;
                    isV = true;
                }
                if (StringUtils.equals(selected, "selected")) {
                    valueId = value;
                    break;
                }
            }
        }
        return valueId;
    }

    private List<Map<String, Object>> getTableData(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
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
        studentTableVms = studentTableVms.substring(studentTableVms.indexOf("[") + 1, studentTableVms.indexOf("adminclass"));
        studentTableVms = studentTableVms.substring(0, studentTableVms.lastIndexOf(","));
        studentTableVms = studentTableVms.replaceAll("\"", "@");
        studentTableVms = studentTableVms.replaceAll("'", "\\\"");
        studentTableVms += "}";

        JSONObject jsonObject = JSON.parseObject(studentTableVms);
        JSONArray jsonArray = jsonObject.getJSONArray("activities");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            map.put("attendClass", j1.getString("lessonName"));
            map.put("courseName", j1.getString("courseName"));
            map.put("classroom", j1.getString("room"));

            String weeksStr = j1.getString("weeksStr");
            if (StringUtils.isNotBlank(weeksStr) && StringUtils.contains(weeksStr, "~")) {
                String[] ws = weeksStr.split("~");
                map.put("openDate", ws[0]);
                map.put("closeDate", ws[1]);
            } else {
                map.put("openDate", weeksStr);
            }

            map.put("week", getWeekday(j1.getString("weekday")));

            map.put("classTime", getStartUnit(j1.getString("startUnit")));
            map.put("overTime", getEndUnit(j1.getString("endUnit")));

            StringBuilder sb = new StringBuilder();
            JSONArray ja1 = j1.getJSONArray("teachers");
            for (int j = 0; j < ja1.size(); j++) {
                sb.append(ja1.getString(j)).append(" ");
            }
            map.put("teacherName", sb.toString());

            list.add(map);
        }
        return list;
    }

    private String getWeekday(String weekday) {
        String wk = "";
        if (StringUtils.isNotBlank(weekday)) {
            switch (NumberUtils.toInt(weekday)) {
                case 1:
                    wk = "星期一";
                    break;
                case 2:
                    wk = "星期二";
                    break;
                case 3:
                    wk = "星期三";
                    break;
                case 4:
                    wk = "星期四";
                    break;
                case 5:
                    wk = "星期五";
                    break;
                case 6:
                    wk = "星期六";
                    break;
                case 7:
                    wk = "星期天";
                    break;
            }
        }
        return wk;

    }

    private String getStartUnit(String startUnit) {
        String su = "";
        if (StringUtils.isNotBlank(startUnit)) {
            switch (NumberUtils.toInt(startUnit)) {
                case 1:
                    su = "8:00";
                    break;
                case 2:
                    su = "8:50";
                    break;
                case 3:
                    su = "9:50";
                    break;
                case 4:
                    su = "10:40";
                    break;
                case 5:
                    su = "11:30";
                    break;
                case 6:
                    su = "13:30";
                    break;
                case 7:
                    su = "14:20";
                    break;
                case 8:
                    su = "15:20";
                    break;
                case 9:
                    su = "16:10";
                    break;
                case 10:
                    su = "17:00";
                    break;
                case 11:
                    su = "19:00";
                    break;
                case 12:
                    su = "19:50";
                    break;
            }
        }
        return su;

    }

    private String getEndUnit(String endUnit) {
        String eu = "";
        if (StringUtils.isNotBlank(endUnit)) {
            switch (NumberUtils.toInt(endUnit)) {
                case 1:
                    eu = "8:45";
                    break;
                case 2:
                    eu = "9:35";
                    break;
                case 3:
                    eu = "10:35";
                    break;
                case 4:
                    eu = "11:25";
                    break;
                case 5:
                    eu = "12:15";
                    break;
                case 6:
                    eu = "14:15";
                    break;
                case 7:
                    eu = "15:05";
                    break;
                case 8:
                    eu = "16:05";
                    break;
                case 9:
                    eu = "16:55";
                    break;
                case 10:
                    eu = "17:45";
                    break;
                case 11:
                    eu = "19:45";
                    break;
                case 12:
                    eu = "20:35";
                    break;
            }
        }
        return eu;

    }
}
