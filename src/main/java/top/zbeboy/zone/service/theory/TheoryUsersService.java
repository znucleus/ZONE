package top.zbeboy.zone.service.theory;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TheoryUsers;
import top.zbeboy.zbase.domain.tables.records.TheoryUsersRecord;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface TheoryUsersService {
    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TheoryUsers findById(String id);

    /**
     * 根据理论发布id与学生id查询
     *
     * @param theoryReleaseId 理论发布id
     * @param studentId       学生id
     * @return 数据
     */
    Optional<TheoryUsersRecord> findByTheoryReleaseIdAndStudentId(String theoryReleaseId, int studentId);

    /**
     * 根据理论发布id查询
     *
     * @param theoryReleaseId 理论发布id
     * @return 数据
     */
    List<TheoryUsers> findByTheoryReleaseId(String theoryReleaseId);

    /**
     * 查询不在名单的学生
     *
     * @param theoryReleaseId 理论发布id
     * @return 数据
     */
    Result<Record> findStudentNotExistsUsers(String theoryReleaseId, int organizeId);

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
     * 根据理论发布id统计
     *
     * @param theoryReleaseId 理论发布id
     * @return 数据
     */
    int countByTheoryReleaseId(String theoryReleaseId);

    /**
     * 批量保存
     *
     * @param theoryUsers 数据
     */
    void batchSave(List<TheoryUsers> theoryUsers);

    /**
     * 保存
     *
     * @param theoryUsers 数据
     */
    void save(TheoryUsers theoryUsers);

    /**
     * 更新
     *
     * @param theoryUsers 数据
     */
    void update(TheoryUsers theoryUsers);

    /**
     * 批量删除
     *
     * @param ids id
     */
    void deleteById(List<String> ids);
}
