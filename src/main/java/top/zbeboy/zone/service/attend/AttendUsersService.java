package top.zbeboy.zone.service.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;

import java.util.List;
import java.util.Optional;

public interface AttendUsersService {

    /**
     * 通过发布表id和学生id查询
     *
     * @param attendReleaseId 发布表id
     * @param studentId       学生id
     * @return 数据
     */
    Optional<AttendUsersRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId);

    /**
     * 保存
     *
     * @param attendUsers 数据
     */
    void batchSave(List<AttendUsers> attendUsers);
}
