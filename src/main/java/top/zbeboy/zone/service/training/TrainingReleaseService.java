package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface TrainingReleaseService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingRelease findById(String id);

    /**
     * 通过主键查询
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
     * @param trainingRelease 数据
     */
    void save(TrainingRelease trainingRelease);

    /**
     * 更新
     *
     * @param trainingRelease 数据
     */
    void update(TrainingRelease trainingRelease);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
