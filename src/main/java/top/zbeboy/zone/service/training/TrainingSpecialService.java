package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecial;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface TrainingSpecialService {

    /**
     * 根据主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

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
     * @param trainingSpecial 数据
     */
    void save(TrainingSpecial trainingSpecial);

    /**
     * 更新
     *
     * @param trainingSpecial 数据
     */
    void update(TrainingSpecial trainingSpecial);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
