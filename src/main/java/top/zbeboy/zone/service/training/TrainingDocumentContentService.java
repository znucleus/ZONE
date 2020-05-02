package top.zbeboy.zone.service.training;

import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentContent;

public interface TrainingDocumentContentService {

    /**
     * 保存
     *
     * @param trainingDocumentContent 数据
     */
    void save(TrainingDocumentContent trainingDocumentContent);

    /**
     * 更新
     *
     * @param trainingDocumentContent 数据
     */
    void update(TrainingDocumentContent trainingDocumentContent);
}
