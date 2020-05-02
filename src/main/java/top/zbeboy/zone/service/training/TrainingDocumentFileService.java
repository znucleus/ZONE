package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentFile;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

public interface TrainingDocumentFileService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingDocumentFile findById(String id);

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
     * @param trainingDocumentFile 数据
     */
    void save(TrainingDocumentFile trainingDocumentFile);

    /**
     * 更新下载量
     *
     * @param id 主键
     */
    void updateDownloads(String id);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
