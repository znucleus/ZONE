package top.zbeboy.zone.web.achievement.student;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class StudentAchievementHttpClient {

    private final CloseableHttpClient httpclient;

    public StudentAchievementHttpClient() {
        this.httpclient = HttpClients.createDefault();
    }

    public void captcha(HttpServletResponse res) throws IOException {
        HttpGet httpget = new HttpGet("http://cityxxpt.kmust.edu.cn/GXGLPT/validate.jsp");
        httpget.setHeader("Referer", "http://cityxxpt.kmust.edu.cn/GXGLPT/Login.jsp");
        httpget.setHeader("Host", "cityxxpt.kmust.edu.cn");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        HttpResponse response = httpclient.execute(httpget);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            res.setContentType(MediaType.IMAGE_JPEG_VALUE);
            FileCopyUtils.copy(EntityUtils.toByteArray(entity), res.getOutputStream());
        }

    }

    public Map<String, Object> login(Map<String, String> param) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpPost post = new HttpPost("http://cityxxpt.kmust.edu.cn/GXGLPT/LoginActionLogin.do");
        post.setHeader("Referer", "http://cityxxpt.kmust.edu.cn/GXGLPT/Login.jsp");
        post.setHeader("Host", "cityxxpt.kmust.edu.cn");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        List<BasicNameValuePair> parameters = new ArrayList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("yhdm", param.get("yhdm"));
        parameters.add(param1);

        BasicNameValuePair param2 = new BasicNameValuePair("yhmm", param.get("yhmm"));
        parameters.add(param2);

        BasicNameValuePair param3 = new BasicNameValuePair("yhlx", param.get("yhlx"));
        parameters.add(param3);

        BasicNameValuePair param4 = new BasicNameValuePair("yzm", param.get("yzm"));
        parameters.add(param4);

        HttpEntity entity = new UrlEncodedFormEntity(parameters, "GBK");

        post.setEntity(entity);

        HttpResponse response = httpclient.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            map = query();
        } else {
            map.put("hasError", true);
            map.put("statusCode", response.getStatusLine().getStatusCode());
            map.put("reasonPhrase", response.getStatusLine().getReasonPhrase());
        }

        return map;
    }

    public Map<String, Object> query() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpGet httpget = new HttpGet("http://cityxxpt.kmust.edu.cn/GXGLPT/JXGL/CJGL/CJGL_XS/CjglxscjViewCs.do");
        httpget.setHeader("Referer", "http://cityxxpt.kmust.edu.cn/GXGLPT/JXGL/CJGL/Cjgl_xstop_header.html");
        httpget.setHeader("Host", "cityxxpt.kmust.edu.cn");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");

        HttpResponse response = httpclient.execute(httpget);

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity();
            List<Map<String, Object>> list = getData(EntityUtils.toString(responseEntity));
            map.put("data", list);
        } else {
            map.put("hasError", true);
            map.put("statusCode", response.getStatusLine().getStatusCode());
            map.put("reasonPhrase", response.getStatusLine().getReasonPhrase());
        }

        return map;
    }

    private List<Map<String, Object>> getData(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        Document doc = Jsoup.parse(str);

        Elements elements = doc.getElementsByTag("table");
        if (Objects.nonNull(elements)) {
            String studentNumber = "";
            Element element = elements.get(0);
            Elements tds = element.getElementsByTag("td");
            if (Objects.nonNull(tds)) {
                if (tds.size() > 2) {
                    studentNumber = StringUtils.trim(tds.get(2).text());
                }
            }

            Element tableLastElement = elements.last();
            Elements tableTrElements = tableLastElement.getElementsByTag("tr");
            if (Objects.nonNull(tableTrElements)) {
                for (int i = 2; i < tableTrElements.size() - 1; i++) {
                    Element tableTrElement = tableTrElements.get(i);
                    Elements tableTdElements = tableTrElement.getElementsByTag("td");
                    if (Objects.nonNull(tableTdElements)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentNumber", studentNumber);
                        for (int j = 1; j < tableTdElements.size(); j++) {
                            String text = tableTdElements.get(j).text();
                            switch (j) {
                                case 1:
                                    map.put("schoolYear", text);
                                    break;
                                case 2:
                                    map.put("semester", text);
                                    break;
                                case 3:
                                    map.put("organizeName", text);
                                    break;
                                case 4:
                                    map.put("courseCode", text);
                                    break;
                                case 5:
                                    map.put("courseName", text);
                                    break;
                                case 6:
                                    map.put("courseType", text);
                                    break;
                                case 7:
                                    map.put("totalHours", text);
                                    break;
                                case 8:
                                    map.put("courseNature", text);
                                    break;
                                case 9:
                                    map.put("assessmentMethod", text);
                                    break;
                                case 10:
                                    map.put("registrationMethod", text);
                                    break;
                                case 11:
                                    map.put("creditsDue", text);
                                    break;
                                case 12:
                                    map.put("achievement", text);
                                    break;
                                case 13:
                                    map.put("creditsObtained", text);
                                    break;
                                case 14:
                                    map.put("examType", text);
                                    break;
                                case 15:
                                    map.put("turn", text);
                                    break;
                                case 16:
                                    map.put("examDate", text);
                                    break;
                                case 17:
                                    map.put("remark", text);
                                    break;
                            }
                        }
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }
}
