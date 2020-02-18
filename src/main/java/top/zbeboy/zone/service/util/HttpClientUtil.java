package top.zbeboy.zone.service.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    /**
     * 发送get请求
     * @param uri 网址
     * @param p 参数
     * @return 返回结果
     * @throws IOException
     */
    public static String sendGet(String uri, Map<String, String> p) throws IOException {
        // 1、创建httpClient
        CloseableHttpClient client = HttpClients.createDefault();
        // 2、封装请求参数
        List<BasicNameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> m : p.entrySet()) {
            list.add(new BasicNameValuePair(m.getKey(), m.getValue()));
        }

        // 3、转化参数
        String params = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
        // 4、创建HttpGet请求
        HttpGet httpGet = new HttpGet(uri + "?" + params);
        CloseableHttpResponse response = client.execute(httpGet);

        // 5、获取实体
        HttpEntity entity = response.getEntity();
        // 将实体装成字符串
        String string = EntityUtils.toString(entity);
        response.close();
        return string;
    }

    /**
     * 发送post请求
     * @param uri 网址
     * @param p 参数
     * @return 返回结果
     * @throws IOException
     */
    public static String sendPost(String uri, Map<String, String> p) throws IOException {
        // 1、创建httpClient
        CloseableHttpClient client = HttpClients.createDefault();

        List<BasicNameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> m : p.entrySet()) {
            list.add(new BasicNameValuePair(m.getKey(), m.getValue()));
        }
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(list, Consts.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);

        // 5、获取实体
        HttpEntity entity = response.getEntity();
        // 将实体装成字符串
        String string = EntityUtils.toString(entity);
        response.close();
        return string;
    }
}
