package top.zbeboy.zone.service.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.tools.service.util.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("systemNowApiService")
public class SystemNowApiServiceImpl implements SystemNowApiService {

    private final Logger log = LoggerFactory.getLogger(SystemNowApiService.class);

    @Autowired
    private ZoneProperties zoneProperties;

    @Override
    public Map<String, String> findZipCodeByName(String name) throws IOException {
        Map<String, String> last = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        param.put("app", "life.postcode");
        param.put("areaname", name);
        param.put("appkey", zoneProperties.getNowApi().getZipCodeAppKey());
        param.put("sign", zoneProperties.getNowApi().getZipCodeSign());
        param.put("format", "json");
        String json = queryNowApi(param);
        JSONObject jsonObject = JSON.parseObject(json);
        if (StringUtils.equals("1", jsonObject.getString("success"))) {
            JSONObject result = jsonObject.getJSONObject("result");
            int count = result.getInteger("count");
            if (count > 0) {
                JSONArray jsonArray = result.getJSONArray("lists");
                JSONObject first = jsonArray.getJSONObject(0);
                last.put("postcode", first.getString("postcode"));
                last.put("simcall", first.getString("simcall"));
            }
        } else {
            log.debug("查询邮政编码失败，NAME：{}，MESSAGE：{} ", name, jsonObject.getString("msg"));
        }
        return last;
    }


    /**
     * 发送数据查询
     *
     * @param p 参数
     * @return 返回值
     * @throws IOException 连接异常
     */
    private String queryNowApi(Map<String, String> p) throws IOException {
        return HttpClientUtil.sendGet(zoneProperties.getNowApi().getUrl(), p);
    }
}
