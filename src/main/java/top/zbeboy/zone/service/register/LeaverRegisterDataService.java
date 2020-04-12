package top.zbeboy.zone.service.register;

import org.jooq.Record;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData;

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
}
