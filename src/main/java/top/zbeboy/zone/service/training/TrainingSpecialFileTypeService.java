package top.zbeboy.zone.service.training;

import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialFileType;
import top.zbeboy.zbase.domain.tables.records.TrainingSpecialFileTypeRecord;

public interface TrainingSpecialFileTypeService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingSpecialFileType findById(String id);

    /**
     * 根据专题id查询
     *
     * @param trainingSpecialId 专题id
     * @return 数据
     */
    Result<TrainingSpecialFileTypeRecord> findByTrainingSpecialId(String trainingSpecialId);

    /**
     * 保存
     *
     * @param trainingSpecialFileType 数据
     */
    void save(TrainingSpecialFileType trainingSpecialFileType);

    /**
     * 更新
     *
     * @param trainingSpecialFileType 数据
     */
    void update(TrainingSpecialFileType trainingSpecialFileType);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
