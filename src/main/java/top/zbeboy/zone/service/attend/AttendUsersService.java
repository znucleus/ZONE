package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;
import top.zbeboy.zone.domain.tables.records.StudentRecord;

import java.util.List;
import java.util.Optional;

public interface AttendUsersService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AttendUsers findById(String id);

    /**
     * 根据发布表id查询名单数据
     *
     * @return 数据
     */
    List<AttendUsers> findByAttendReleaseId(String attendReleaseId);

    /**
     * 根据发布表id查询名单数据
     *
     * @return 数据
     */
    Result<Record> findByAttendReleaseIdRelation(String attendReleaseId);

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
    Result<Record> findHasAttendedStudent(String attendReleaseId, int attendReleaseSubId);

    /**
     * 查询未签到学生数据
     *
     * @param attendReleaseId    主表id
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    Result<Record> findNotAttendedStudent(String attendReleaseId, int attendReleaseSubId);

    /**
     * 查询未在名单中的学生
     *
     * @param attendReleaseId 主表ID
     * @param organizeId      班级ID
     * @return 数据
     */
    Result<Record> findStudentNotExistsAttendUsers(String attendReleaseId, int organizeId);

    /**
     * 保存
     *
     * @param attendUsers 数据
     */
    void save(AttendUsers attendUsers);

    /**
     * 保存
     *
     * @param attendUsers 数据
     */
    void batchSave(List<AttendUsers> attendUsers);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);

    /**
     * 更新
     *
     * @param attendUsers 数据
     */
    void update(AttendUsers attendUsers);
}
