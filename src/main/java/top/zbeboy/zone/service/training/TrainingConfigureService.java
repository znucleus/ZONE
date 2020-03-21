package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingConfigure;

public interface TrainingConfigureService {

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
     * @param trainingConfigure 数据
     */
    void save(TrainingConfigure trainingConfigure);

    /**
     * 更新
     *
     * @param trainingConfigure 数据
     */
    void update(TrainingConfigure trainingConfigure);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
