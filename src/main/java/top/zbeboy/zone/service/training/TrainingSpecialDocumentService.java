package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialDocument;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface TrainingSpecialDocumentService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingSpecialDocument findById(String id);

    /**
     * 通过主键关联查询
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
     * @param trainingSpecialDocument 数据
     */
    void save(TrainingSpecialDocument trainingSpecialDocument);

    /**
     * 更新
     *
     * @param trainingSpecialDocument 数据
     */
    void update(TrainingSpecialDocument trainingSpecialDocument);

    /**
     * 更新阅读量
     *
     * @param id 主键
     */
    void updateReading(String id);

    /**
     * 删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
