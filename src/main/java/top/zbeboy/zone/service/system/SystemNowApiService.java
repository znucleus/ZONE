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

    /**
     * 查询身份证信息
     *
     * @param idCard 身份证号
     * @return 信息
     */
    Map<String, String> findInfoByIdCard(String idCard) throws IOException;
}
