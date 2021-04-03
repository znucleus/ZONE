package top.zbeboy.zone.web.achievement.software;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.CharEncoding;
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
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SoftwareAchievementHttpClient {

    private final CloseableHttpClient httpclient;

    public SoftwareAchievementHttpClient() {
        this.httpclient = HttpClients.createDefault();
    }

    public void captcha(HttpServletResponse res) throws IOException {
        HttpGet httpget = new HttpGet("https://query.ruankao.org.cn//score/captcha");
        httpget.setHeader("Referer", "https://query.ruankao.org.cn/score/main");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        HttpResponse response = httpclient.execute(httpget);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            res.setContentType(MediaType.IMAGE_JPEG_VALUE);
            FileCopyUtils.copy(EntityUtils.toByteArray(entity), res.getOutputStream());
        }

    }

    public List<String> examDate() throws IOException {
        List<String> list = new ArrayList<>();
        HttpGet httpget = new HttpGet("https://query.ruankao.org.cn/score/main");
        httpget.setHeader("Referer", "https://query.ruankao.org.cn/score/main");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        HttpResponse response = httpclient.execute(httpget);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            list = getExamDate(str);
        }
        return list;
    }

    public Map<String, Object> verifyCaptcha(Map<String, String> param) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpPost post = new HttpPost("https://query.ruankao.org.cn//score/VerifyCaptcha");
        post.setHeader("Referer", "https://query.ruankao.org.cn/score/main");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        List<BasicNameValuePair> parameters = new ArrayList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("captcha", param.get("captcha"));
        parameters.add(param1);

        HttpEntity entity = new UrlEncodedFormEntity(parameters);

        post.setEntity(entity);

        HttpResponse response = httpclient.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getIntValue("flag") == 1) {
                map = query(param);
            } else {
                map.put("hasError", true);
                map.put("statusCode", "500");
                map.put("reasonPhrase", jsonObject.getString("msg"));
            }
        } else {
            map.put("hasError", true);
            map.put("statusCode", response.getStatusLine().getStatusCode());
            map.put("reasonPhrase", response.getStatusLine().getReasonPhrase());
        }
        return map;
    }

    public Map<String, Object> query(Map<String, String> param) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasError", false);
        HttpPost post = new HttpPost("https://query.ruankao.org.cn//score/result");
        post.setHeader("Referer", "https://query.ruankao.org.cn/score/main");
        post.setHeader("Host", "query.ruankao.org.cn");
        post.setHeader("Origin", "https://query.ruankao.org.cn");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        List<BasicNameValuePair> parameters = new ArrayList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("jym", param.get("captcha"));
        parameters.add(param1);

        BasicNameValuePair param2 = new BasicNameValuePair("stage", param.get("stage"));
        parameters.add(param2);

        BasicNameValuePair param3 = new BasicNameValuePair("xm", param.get("xm"));
        parameters.add(param3);

        BasicNameValuePair param4 = new BasicNameValuePair("zjhm", param.get("zjhm"));
        parameters.add(param4);

        BasicNameValuePair param5 = new BasicNameValuePair("select_type", param.get("select_type"));
        parameters.add(param5);

        HttpEntity entity = new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8);

        post.setEntity(entity);

        HttpResponse response = httpclient.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getIntValue("flag") == 1) {
                map.put("data", getData(jsonObject));
            } else {
                map.put("hasError", true);
                map.put("statusCode", "500");
                map.put("reasonPhrase", jsonObject.getString("msg"));
            }
        } else {
            map.put("hasError", true);
            map.put("statusCode", response.getStatusLine().getStatusCode());
            map.put("reasonPhrase", response.getStatusLine().getReasonPhrase());
        }

        return map;
    }

    private List<String> getExamDate(String str){
        List<String> list = new ArrayList<>();
        Document doc = Jsoup.parse(str);
        Elements elements = doc.getElementsByAttribute("data-value");
        if(Objects.nonNull(elements)){
            for (Element element : elements) {
                list.add(StringUtils.trim(element.text()));
            }
        }
        return list;
    }

    private Map<String, Object> getData(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        JSONObject data = jsonObject.getJSONObject("data");
        // 证件号
        map.put("ZJH", StringUtils.defaultString(data.getString("ZJH")));
        // 准考证号
        map.put("ZKZH", StringUtils.defaultString(data.getString("ZKZH")));
        // 姓名
        map.put("XM", StringUtils.defaultString(data.getString("XM")));
        // 考试时间
        map.put("KSSJ", StringUtils.defaultString(data.getString("KSSJ")));
        // 上午成绩
        map.put("SWCJ", StringUtils.defaultString(data.getString("SWCJ")));
        // 下午成绩
        map.put("XWCJ", StringUtils.defaultString(data.getString("XWCJ")));
        // 论文成绩
        map.put("LWCJ", StringUtils.defaultString(data.getString("LWCJ")));
        // 资格名称
        map.put("ZGMC", StringUtils.defaultString(data.getString("ZGMC")));
        return map;
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }
}
