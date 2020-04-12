package top.zbeboy.zone.service.register;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface LeaverRegisterDataService {

    /**
     * 通过发布id与学生id查询
     *
     * @param leaverRegisterReleaseId 发布id
     * @param studentId               学生id
     * @return 数据
     */
    Optional<Record> findByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId);

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
     * @param leaverRegisterData 数据
     */
    void save(LeaverRegisterData leaverRegisterData);

    /**
     * 通过发布id与学生id删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @param studentId               学生id
     */
    void deleteByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
