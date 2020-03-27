package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingUsers;
import top.zbeboy.zone.domain.tables.records.TrainingUsersRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface TrainingUsersService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingUsers findById(String id);

    /**
     * 根据实训发布id与学生id查询
     *
     * @param trainingReleaseId 实训发布id
     * @param studentId         学生id
     * @return 数据
     */
    Optional<TrainingUsersRecord> findByTrainingReleaseIdAndStudentId(String trainingReleaseId, int studentId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 批量保存
     *
     * @param trainingUsers 数据
     */
    void batchSave(List<TrainingUsers> trainingUsers);

    /**
     * 保存
     *
     * @param trainingUsers 数据
     */
    void save(TrainingUsers trainingUsers);

    /**
     * 更新
     *
     * @param trainingUsers 数据
     */
    void update(TrainingUsers trainingUsers);
}
