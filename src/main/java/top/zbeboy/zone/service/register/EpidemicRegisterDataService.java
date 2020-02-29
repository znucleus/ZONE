package top.zbeboy.zone.service.register;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zone.domain.tables.records.EpidemicRegisterDataRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.Optional;

public interface EpidemicRegisterDataService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    EpidemicRegisterData findById(String id);

    /**
     * 根据账号查询今日登记
     *
     * @param username                  账号
     * @param epidemicRegisterReleaseId 发布id
     * @return 数据
     */
    Optional<EpidemicRegisterDataRecord> findTodayByUsernameAndEpidemicRegisterReleaseId(String username, String epidemicRegisterReleaseId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param epidemicRegisterData 数据
     */
    void save(EpidemicRegisterData epidemicRegisterData);

    /**
     * 更新
     *
     * @param epidemicRegisterData 数据
     */
    void update(EpidemicRegisterData epidemicRegisterData);
}
