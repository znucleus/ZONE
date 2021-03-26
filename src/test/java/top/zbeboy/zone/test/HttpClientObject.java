package top.zbeboy.zone.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;

public class HttpClientObject {

    private String key;
    private CloseableHttpClient httpclient;

    public HttpClientObject(String key) throws IOException {
        this.key = key;
        this.httpclient = HttpClients.createDefault();
        create();
    }

    private void create() throws IOException {
        HttpGet httpget = new HttpGet("https://query.ruankao.org.cn//score/captcha");
        httpget.setHeader("Referer", "https://query.ruankao.org.cn/");
        System.out.println("Executing request " + httpget.getRequestLine());

        HttpResponse response = httpclient.execute(httpget);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();

            byte[] data = EntityUtils.toByteArray(entity);

            //图片存入磁盘
            FileOutputStream fos = new FileOutputStream("e:/1.jpg");
            fos.write(data);
            fos.close();
        }

    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpClientObject{");
        sb.append("key='").append(key).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
