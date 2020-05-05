package top.zbeboy.zone.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestWeiXin {

    @Test
    public void testCode() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("appid", "wxd41fbb701eef2c73");
        map.put("secret", "b9ae620acdb80ff0ec6ac06222bf3768");
        map.put("js_code", "043y7Afi1raDgs0amtdi1n3Jfi1y7Af-");
        map.put("grant_type", "authorization_code");
        String result = HttpClientUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", map);
        System.out.println(result);
        JSONObject params = JSON.parseObject(result);
        //{"session_key":"ompKx486DtFyRwg2t0IXLA==","expires_in":7200,"openid":"oSOTs0Nzjzm0YBQrmVS2pz2i8iQw"}
    }

    @Test
    public void testAccessToken() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", "wxd41fbb701eef2c73");
        map.put("secret", "b9ae620acdb80ff0ec6ac06222bf3768");
        String result = HttpClientUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", map);
        System.out.println(result);
        JSONObject params = JSON.parseObject(result);
        System.out.println(params);
        //{"access_token":"30_Yy9oac_04VMBIdj4BBKpisP3DzNKXbAd02eKVMRb7Z2Q5rdOtwtGUqYdGOXgAFJlokvBaqwVUlfaIrMqV7GEcOjdT5PbRnW8ndxpTLu8m-ejf1eGqLAsBFzFgRXsY2-Qi91pxWPdLZKO-ox9EAKeACALHR","expires_in":7200}
    }

    @Test
    public void testSubscribe() throws IOException {
        String accessToken = "30_Yy9oac_04VMBIdj4BBKpisP3DzNKXbAd02eKVMRb7Z2Q5rdOtwtGUqYdGOXgAFJlokvBaqwVUlfaIrMqV7GEcOjdT5PbRnW8ndxpTLu8m-ejf1eGqLAsBFzFgRXsY2-Qi91pxWPdLZKO-ox9EAKeACALHR";
        Map<String, Object> map = new HashMap<>();
        map.put("touser", "oSOTs0Nzjzm0YBQrmVS2pz2i8iQw");
        map.put("template_id", "_7l7dMHGzjCC00ggF4to7CoIgWZJLqkAhCZBWKZf4nc");
        map.put("page", "pages/index/index");
        map.put("miniprogram_state", "");
        map.put("lang", "");

        Map<String, Object> data = new HashMap<>();

        Map<String, Object> phrase1 = new HashMap<>();
        phrase1.put("value", "待签到");
        data.put("phrase1", phrase1);

        Map<String, Object> name2 = new HashMap<>();
        name2.put("value", "");
        data.put("name2", name2);

        Map<String, Object> date3 = new HashMap<>();
        date3.put("value", DateTimeUtil.formatSqlTimestamp(DateTimeUtil.getNowSqlTimestamp(), DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
        data.put("date3", date3);

        Map<String, Object> thing5 = new HashMap<>();
        thing5.put("value", "校内");
        data.put("thing5", thing5);

        map.put("data", data);
        String json = JSON.toJSONString(map);

        String result = HttpClientUtil.sendJsonPost("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, json);

        System.out.println(result);
    }
}
