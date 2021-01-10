package top.zbeboy.zone.service.theory;

import org.jooq.Record;
import org.jooq.Record11;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TheoryAttendUsers;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface TheoryAttendUsersService {
    /**
     * Query by id
     *
     * @param id primary key
     * @return data
     */
    TheoryAttendUsers findById(String id);

    /**
     * 查询不在名单的学生
     *
     * @param theoryReleaseId 理论发布id
     * @param theoryAttendId  考勤id
     * @return 数据
     */
    Result<Record> findStudentNotExistsUsers(String theoryReleaseId, String theoryAttendId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 数据导出
     *
     * @param dataTablesUtil 工具类
     * @return 数据
     */
    Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> export(DataTablesUtil dataTablesUtil);

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
     * @param theoryAttendUsers 数据
     */
    void batchSave(List<TheoryAttendUsers> theoryAttendUsers);

    /**
     * update
     *
     * @param theoryAttendUsers data
     */
    void update(TheoryAttendUsers theoryAttendUsers);

    /**
     * update operate
     *
     * @param theoryAttendId 考勤id
     * @param operate        状态
     */
    void updateOperateByTheoryAttendId(String theoryAttendId, Byte operate);

    /**
     * delete
     *
     * @param ids ids
     */
    void deleteById(List<String> ids);
}
