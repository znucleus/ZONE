package top.zbeboy.zone.feign.data;

import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.domain.tables.records.WeiXinDeviceRecord;

import java.util.Optional;

public interface WeiXinDeviceService {

    /**
     * 通过账号查询
     *
     * @param username 账号
     * @return 结果
     */
    Optional<WeiXinDeviceRecord> findByUsername(String username);

    /**
     * 保存
     *
     * @param weiXinDevice 数据
     */
    void save(WeiXinDevice weiXinDevice);

    /**
     * 更新
     *
     * @param weiXinDevice 数据
     */
    void update(WeiXinDevice weiXinDevice);
}
