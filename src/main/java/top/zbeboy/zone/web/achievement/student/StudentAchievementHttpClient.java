package top.zbeboy.zone.web.achievement.student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class StudentAchievementHttpClient {

    public CookieStore captcha(HttpServletResponse res) throws IOException {
        CookieStore cookieStore = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext httpClientContext = HttpClientContext.create();
        HttpGet httpget = new HttpGet("http://10.1.1.9:7002/GXGLPT/validate.jsp");
        httpget.setHeader("Referer", "http://10.1.1.9:7002/GXGLPT/Login.jsp");
        httpget.setHeader("Host", "10.1.1.9:7002");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        HttpResponse response = httpclient.execute(httpget, httpClientContext);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            res.setContentType(MediaType.IMAGE_JPEG_VALUE);
            FileCopyUtils.copy(EntityUtils.toByteArray(entity), res.getOutputStream());
            cookieStore = httpClientContext.getCookieStore();
        }
        httpclient.close();
        return cookieStore;
    }

    public Map<String, Object> login(CookieStore cookieStore, Map<String, String> param) throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpPost post = new HttpPost("http://10.1.1.9:7002/GXGLPT/LoginActionLogin.do");
        post.setHeader("Referer", "http://10.1.1.9:7002/GXGLPT/Login.jsp");
        post.setHeader("Host", "10.1.1.9:7002");
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

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            map = query(httpclient);
        } else {
            map.put("hasError", true);
            map.put("statusCode", response.getStatusLine().getStatusCode());
            map.put("reasonPhrase", response.getStatusLine().getReasonPhrase());
        }
        httpclient.close();
        return map;
    }

    public Map<String, Object> query(CloseableHttpClient httpclient) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpGet httpget = new HttpGet("http://10.1.1.9:7002/GXGLPT/JXGL/CJGL/CJGL_XS/CjglxscjViewCs.do");
        httpget.setHeader("Referer", "http://10.1.1.9:7002/GXGLPT/JXGL/CJGL/Cjgl_xstop_header.html");
        httpget.setHeader("Host", "10.1.1.9:7002");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");

        HttpResponse response = httpclient.execute(httpget);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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

    public Map<String, Object> eduData(String username, String password) throws Exception {
        final String loginSaltUri = "http://cityjw.kust.edu.cn/integration/login-salt";
        final String loginUri = "http://cityjw.kust.edu.cn/integration/login";
        final String gradeUri = "http://cityjw.kust.edu.cn/integration/for-std/best/grade/sheet";
        final String gradePrintUri = "http://cityjw.kust.edu.cn/integration/for-std/best/grade/sheet/print/%s?semesterId=";

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

            if (loginResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity responseEntity = loginResponse.getEntity();
                String loginResult = EntityUtils.toString(responseEntity);
                JSONObject loginJson = JSON.parseObject(loginResult);
                if (loginJson.getBoolean("result")) {
                    HttpContext httpContext = new BasicHttpContext();
                    HttpGet gradeGet = new HttpGet(gradeUri);
                    HttpResponse gradeResponse = client.execute(gradeGet, httpContext);

                    if (gradeResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                        HttpHost currentHost = clientContext.getTargetHost();
                        HttpUriRequest req = clientContext.getAttribute(HttpCoreContext.HTTP_REQUEST, HttpUriRequest.class);
                        String uri = (req.getURI().isAbsolute()) ? req.getURI().toString() : (currentHost.toURI() + req.getURI());
                        if (StringUtils.isNotBlank(uri)) {
                            String code = uri.substring(uri.lastIndexOf("/") + 1);
                            HttpGet gradePrintGet = new HttpGet(String.format(gradePrintUri, code));
                            HttpResponse gradePrintResponse = client.execute(gradePrintGet);
                            if (gradePrintResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                HttpEntity gradePrintResponseEntity = gradePrintResponse.getEntity();
                                List<Map<String, Object>> list = getDataNew(EntityUtils.toString(gradePrintResponseEntity));
                                result.put("data", list);
                            } else {
                                result.put("hasError", true);
                                result.put("statusCode", gradeResponse.getStatusLine().getStatusCode());
                                result.put("reasonPhrase", gradeResponse.getStatusLine().getReasonPhrase() + "【GRADE_PRINT】");
                            }
                        } else {
                            result.put("hasError", true);
                            result.put("statusCode", "500");
                            result.put("reasonPhrase", "获取CODE失败【GRADE_CODE】");
                        }
                    } else {
                        result.put("hasError", true);
                        result.put("statusCode", gradeResponse.getStatusLine().getStatusCode());
                        result.put("reasonPhrase", gradeResponse.getStatusLine().getReasonPhrase() + "【GRADE】");
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

    private List<Map<String, Object>> getDataNew(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        Document doc = Jsoup.parse(str);
        Elements pagingElements = doc.getElementsByClass("paging");
        if (Objects.nonNull(pagingElements)) {
            for (Element pagingElement : pagingElements) {
                Map<String, Object> param = new HashMap<>();
                String semesterId = UUIDUtil.getUUID();
                param.put("semesterId", semesterId);
                // 解析学期
                Elements semesters = pagingElement.getElementsByTag("h4");
                if (Objects.nonNull(semesters)) {
                    String semester = semesters.get(0).text();
                    param.put("semester", semester);
                }

                // 解析个人信息及成绩
                Elements tables = pagingElement.getElementsByTag("table");
                if (Objects.nonNull(tables)) {
                    List<Map<String, Object>> data = new ArrayList<>();
                    for (int i = 0; i < tables.size(); i++) {
                        // 个人信息
                        if (i == 0) {
                            Elements tds = tables.get(i).getElementsByTag("td");
                            if (Objects.nonNull(tds)) {
                                for (int j = 0; j < tds.size(); j++) {
                                    switch (j) {
                                        case 1:
                                            param.put("departmentName", tds.get(j).text());
                                            break;
                                        case 3:
                                            param.put("organizeName", tds.get(j).text());
                                            break;
                                        case 5:
                                            param.put("studentNumber", tds.get(j).text());
                                            break;
                                        case 7:
                                            param.put("realName", tds.get(j).text());
                                            break;
                                    }
                                }
                            }
                        } else {
                            Elements tbodies = tables.get(i).getElementsByTag("tbody");
                            if (Objects.nonNull(tbodies)) {
                                Element tbody = tbodies.first();
                                Elements tableTrElements = tbody.getElementsByTag("tr");
                                if (Objects.nonNull(tableTrElements)) {
                                    int serialNumber = 1;
                                    for (Element tableTrElement : tableTrElements) {
                                        Elements tableTdElements = tableTrElement.getElementsByTag("td");
                                        if (Objects.nonNull(tableTdElements)) {
                                            Map<String, Object> tableData = new HashMap<>();
                                            for (int j = 0; j < tableTdElements.size(); j++) {
                                                switch (j) {
                                                    case 0:
                                                        tableData.put("courseName", tableTdElements.get(j).text());
                                                        break;
                                                    case 1:
                                                        tableData.put("courseCode", tableTdElements.get(j).text());
                                                        break;
                                                    case 2:
                                                        tableData.put("courseTypeOne", tableTdElements.get(j).text());
                                                        break;
                                                    case 3:
                                                        tableData.put("courseTypeTwo", tableTdElements.get(j).text());
                                                        break;
                                                    case 4:
                                                        tableData.put("teachingClassCode", tableTdElements.get(j).text());
                                                        break;
                                                    case 5:
                                                        tableData.put("achievement", tableTdElements.get(j).text());
                                                        break;
                                                    case 6:
                                                        tableData.put("credits", tableTdElements.get(j).text());
                                                        break;
                                                    case 7:
                                                        tableData.put("point", tableTdElements.get(j).text());
                                                        break;
                                                    case 8:
                                                        tableData.put("gradePoint", tableTdElements.get(j).text());
                                                        break;
                                                    case 9:
                                                        tableData.put("nature", tableTdElements.get(j).text());
                                                        break;
                                                    case 10:
                                                        tableData.put("remark", tableTdElements.get(j).text());
                                                        break;
                                                }
                                            }
                                            tableData.put("semesterId", semesterId);
                                            tableData.put("serialNumber", serialNumber);
                                            serialNumber++;
                                            data.add(tableData);

                                        }
                                    }

                                }
                            }


                        }
                    }
                    param.put("data", data);
                }
                list.add(param);
            }
        }
        return list;
    }
}
