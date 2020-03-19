package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;

public interface TrainingConfigureService {

    /**
     * 根据实训发布id关联查询
     *
     * @param trainingReleaseId 实训发布id
     * @return 数据
     */
    Result<Record> findByTrainingReleaseIdRelation(String trainingReleaseId);
}
