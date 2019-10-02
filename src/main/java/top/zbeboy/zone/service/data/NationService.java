package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.Nation;

import java.util.List;

public interface NationService {

    /**
     * 查询全部
     *
     * @return 全部
     */
    List<Nation> findAll();
}
