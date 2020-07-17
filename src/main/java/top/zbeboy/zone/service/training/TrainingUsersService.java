package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TrainingUsers;
import top.zbeboy.zbase.domain.tables.records.TrainingUsersRecord;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

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
     * 根据实训发布id查询
     *
     * @param trainingReleaseId 实训发布id
     * @return 数据
     */
    List<TrainingUsers> findByTrainingReleaseId(String trainingReleaseId);

    /**
     * 查询不在名单的学生
     *
     * @param trainingReleaseId 实训发布id
     * @return 数据
     */
    Result<Record> findStudentNotExistsUsers(String trainingReleaseId, int organizeId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 数据导出
     *
     * @param dataTablesUtil 工具类
     * @return 数据
     */
    Result<Record> export(DataTablesUtil dataTablesUtil);

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
     * 根据实训发布id统计
     *
     * @param trainingReleaseId 实训发布id
     * @return 数据
     */
    int countByTrainingReleaseId(String trainingReleaseId);

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

    /**
     * 批量删除
     *
     * @param ids id
     */
    void deleteById(List<String> ids);
}
