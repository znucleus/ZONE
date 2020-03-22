package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.WeiXinSmallVersion;

public interface WeiXinSmallVersionService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    WeiXinSmallVersion findById(String id);
}
