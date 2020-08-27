package top.zbeboy.zone.service.system;

import java.io.IOException;
import java.util.Map;

public interface SystemNowApiService {

    /**
     * 查询邮编
     *
     * @param name 地市
     * @return 邮编
     */
    Map<String, String> findZipCodeByName(String name) throws IOException;
}
