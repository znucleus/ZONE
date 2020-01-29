package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Nation;
import top.zbeboy.zone.domain.tables.records.NationRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface NationService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Nation findById(int id);

    /**
     * 查询全部
     *
     * @return 全部
     */
    List<Nation> findAll();

    /**
     * 通过民族查询
     *
     * @param nationName 民族
     * @return 民族
     */
    List<Nation> findByNationName(String nationName);

    /**
     * 通过民族查询 注：不等于民族id
     *
     * @param nationName 民族
     * @param nationId   民族id
     * @return 民族
     */
    Result<NationRecord> findByNationNameNeNationId(String nationName, int nationId);

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
    int countAll();

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param nation 数据
     */
    void save(Nation nation);

    /**
     * 更新
     *
     * @param nation 数据
     */
    void update(Nation nation);
}
