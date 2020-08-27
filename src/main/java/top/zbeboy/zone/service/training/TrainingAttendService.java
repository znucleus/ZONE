package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttend;
import top.zbeboy.zbase.domain.tables.records.TrainingAttendRecord;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface TrainingAttendService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingAttend findById(String id);

    /**
     * 查询最近一条考勤
     *
     * @param trainingReleaseId 发布id
     * @return 数据
     */
    TrainingAttendRecord findByTrainingReleaseIdWithRecentlyAttendDate(String trainingReleaseId);

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
     * @param trainingAttend 数据
     */
    void save(TrainingAttend trainingAttend);

    /**
     * 更新
     *
     * @param trainingAttend 数据
     */
    void update(TrainingAttend trainingAttend);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
