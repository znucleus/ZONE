package top.zbeboy.zone.service.training;

import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialDocumentContent;

public interface TrainingSpecialDocumentContentService {

    /**
     * 保存
     *
     * @param trainingSpecialDocumentContent 数据
     */
    void save(TrainingSpecialDocumentContent trainingSpecialDocumentContent);

    /**
     * 更新
     *
     * @param trainingSpecialDocumentContent 数据
     */
    void update(TrainingSpecialDocumentContent trainingSpecialDocumentContent);
}
