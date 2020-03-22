package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingAuthorities;

import java.util.Optional;

public interface TrainingAuthoritiesService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingAuthorities findById(String id);

    /**
     * 通过主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

    /**
     * 根据实训发布id关联查询
     *
     * @param trainingReleaseId 实训发布id
     * @return 数据
     */
    Result<Record> findByTrainingReleaseIdRelation(String trainingReleaseId);

    /**
     * 保存
     *
     * @param trainingAuthorities 数据
     */
    void save(TrainingAuthorities trainingAuthorities);

    /**
     * 更新
     *
     * @param trainingAuthorities 数据
     */
    void update(TrainingAuthorities trainingAuthorities);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
