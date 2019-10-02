package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;

import java.util.List;

public interface PoliticalLandscapeService {

    /**
     * 查询全部
     *
     * @return 全部
     */
    List<PoliticalLandscape> findAll();
}
