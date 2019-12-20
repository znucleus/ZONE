package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.AuthorizeType;

import java.util.List;

public interface AuthorizeTypeService {

    /**
     * 查询全部
     *
     * @return 数据
     */
    List<AuthorizeType> findAll();
}
