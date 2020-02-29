package top.zbeboy.zone.service.register;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterRelease;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

public interface EpidemicRegisterReleaseService {

    /**
     * 分页查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAllByPage(SimplePaginationUtil paginationUtil);

    /**
     * 总数
     *
     * @return 总数
     */
    int countAll(SimplePaginationUtil paginationUtil);

    /**
     * 保存
     *
     * @param epidemicRegisterRelease 数据
     */
    void save(EpidemicRegisterRelease epidemicRegisterRelease);
}
