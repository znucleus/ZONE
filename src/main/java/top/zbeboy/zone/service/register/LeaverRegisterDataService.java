package top.zbeboy.zone.service.register;

import org.jooq.Record;

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
}
