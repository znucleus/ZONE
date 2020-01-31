package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;

import java.util.List;
import java.util.Optional;

public interface AttendUsersService {

    /**
     * 根据发布表id查询名单数据
     *
     * @return 数据
     */
    List<AttendUsers> findByAttendReleaseId(String attendReleaseId);

    /**
     * 通过发布表id和学生id查询
     *
     * @param attendReleaseId 发布表id
     * @param studentId       学生id
     * @return 数据
     */
    Optional<AttendUsersRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId);

    /**
     * 查询已签到学生数据
     *
     * @param attendReleaseId    主表id
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    Result<AttendUsersRecord> findHasAttendedStudent(String attendReleaseId, int attendReleaseSubId);

    /**
     * 查询未签到学生数据
     *
     * @param attendReleaseId    主表id
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    Result<AttendUsersRecord> findNotAttendedStudent(String attendReleaseId, int attendReleaseSubId);

    /**
     * 保存
     *
     * @param attendUsers 数据
     */
    void batchSave(List<AttendUsers> attendUsers);
}
