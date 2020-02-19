package top.zbeboy.zone.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信端app记录
 */
public final class WeiXinAppBook {

    // 签到app
    public static final String ATTEND_APP_ID = "a974078a4a344230b4a836c3a9615277";

    // 模板id
    public static List<String> attendTemplateIds() {
        List<String> ids = new ArrayList<>();
        ids.add("_7l7dMHGzjCC00ggF4to7CoIgWZJLqkAhCZBWKZf4nc");
        return ids;
    }
}
