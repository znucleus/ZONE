package top.zbeboy.zone.service.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendMapKey;

import java.util.List;

public interface AttendMapKeyService {

    /**
     * 查询全部key
     *
     * @return 数据
     */
    List<AttendMapKey> findAll();
}
