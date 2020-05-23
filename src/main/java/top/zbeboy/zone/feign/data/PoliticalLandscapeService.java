package top.zbeboy.zone.feign.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.domain.tables.records.PoliticalLandscapeRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface PoliticalLandscapeService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    PoliticalLandscape findById(int id);

    /**
     * 查询全部
     *
     * @return 全部
     */
    List<PoliticalLandscape> findAll();

    /**
     * 通过政治面貌查询
     *
     * @param politicalLandscapeName 政治面貌
     * @return 政治面貌
     */
    List<PoliticalLandscape> findByPoliticalLandscapeName(String politicalLandscapeName);

    /**
     * 通过政治面貌查询 注：不等于政治面貌id
     *
     * @param politicalLandscapeName 政治面貌
     * @param politicalLandscapeId   政治面貌id
     * @return 政治面貌
     */
    Result<PoliticalLandscapeRecord> findByPoliticalLandscapeNameNePoliticalLandscapeId(String politicalLandscapeName, int politicalLandscapeId);

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
     * @param politicalLandscape 数据
     */
    void save(PoliticalLandscape politicalLandscape);

    /**
     * 更新
     *
     * @param politicalLandscape 数据
     */
    void update(PoliticalLandscape politicalLandscape);
}
