package top.zbeboy.zone.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestNewEduTimetable {

    @Test
    public void testObtainData() throws Exception {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpGet loginSaltGet = new HttpGet("http://cityjw.kust.edu.cn/integration/login-salt");

        HttpResponse response = client.execute(loginSaltGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            //得到实体
            HttpEntity entity = response.getEntity();

            String str = EntityUtils.toString(entity);

            Map<String, Object> params = new HashMap<>();
            params.put("username", "{username}");
            params.put("password", DigestUtils.sha1Hex(str + "-" + "{password}"));
            params.put("captcha", "");

            HttpPost loginPost = new HttpPost("http://cityjw.kust.edu.cn/integration/login");

            StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON);

            loginPost.setEntity(stringEntity);

            HttpResponse loginResponse = client.execute(loginPost);

            if (loginResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = loginResponse.getEntity();
                String loginResult = EntityUtils.toString(responseEntity);
                JSONObject loginJson = JSON.parseObject(loginResult);
                if (loginJson.getBoolean("result")) {
                    HttpGet courseTableGet = new HttpGet("http://cityjw.kust.edu.cn/integration/for-std/course-table");
                    HttpResponse courseTableResponse = client.execute(courseTableGet);
                    if (courseTableResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity courseTableResponseEntity = courseTableResponse.getEntity();
                        String courseTableResult = EntityUtils.toString(courseTableResponseEntity);
                        String semesterId = getSemesters(courseTableResult);

                        HttpGet semesterGet = new HttpGet("http://cityjw.kust.edu.cn/integration/for-std/course-table/semester/" + semesterId + "/print?semesterId=" + semesterId);
                        HttpResponse semesterResponse = client.execute(semesterGet);
                        if (semesterResponse.getStatusLine().getStatusCode() == 200) {
                            HttpEntity semesterResponseEntity = semesterResponse.getEntity();
                            String semesterResult = EntityUtils.toString(semesterResponseEntity);
//                            System.out.println(semesterResult);
                            getTableData(semesterResult);
                        }

                    } else {
                        HttpEntity courseTableResponseEntity = courseTableResponse.getEntity();
                        String courseTableResult = EntityUtils.toString(courseTableResponseEntity);
                        System.out.println(courseTableResult);
                    }
                }
            } else {
                System.out.println(loginResponse.getStatusLine().getStatusCode());
                HttpEntity responseEntity = loginResponse.getEntity();
                String loginResult = EntityUtils.toString(responseEntity);
                System.out.println(loginResult);
            }
        } else {
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    public String getSemesters(String str) throws IOException {
        Document doc = Jsoup.parse(str, CharEncoding.UTF_8);
        // 获取搜索 开课学年 下拉框
        System.out.println("获取学年下拉框:");
        String valueId = "";
        Elements allSemestersSelects = doc.getElementsByAttributeValue("id", "allSemesters");
        for (Element as : allSemestersSelects) {
            Elements options = as.getElementsByTag("option");
            for (Element op : options) {
                String selected = op.attr("selected");
                String value = op.attr("value");
                String text = op.text();
                System.out.println(text + ":" + value + ":" + selected);
                if (StringUtils.equals(selected, "selected")) {
                    valueId = value;
                }
            }
        }
        return valueId;
    }

    public void getTableData(String str) throws IOException {
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

        studentTableVms = studentTableVms.replaceAll("'", "\\\"");
        studentTableVms += "}";
        System.out.println(studentTableVms);
        JSONObject jsonObject = JSON.parseObject(studentTableVms);
        JSONArray jsonArray = jsonObject.getJSONArray("activities");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            System.out.println("周" + j1.getString("weekday"));
            System.out.println(j1.getString("courseName"));
            System.out.println(j1.getString("lessonCode"));
            System.out.println(j1.getString("weeksStr"));
            System.out.println(j1.getString("room"));

            StringBuilder sb = new StringBuilder();
            JSONArray ja1 = j1.getJSONArray("teachers");
            for (int j = 0; j < ja1.size(); j++) {
                sb.append(ja1.getString(j)).append(" ");
            }
            System.out.println(sb.toString());

            System.out.println(j1.getString("startUnit") + "-" + j1.getString("endUnit") + "节");
            System.out.println();
        }
    }
}
