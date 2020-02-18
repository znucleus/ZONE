package top.zbeboy.zone.service.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendWxStudentSubscribe;
import top.zbeboy.zone.domain.tables.records.AttendWxStudentSubscribeRecord;

import java.util.Optional;

public interface AttendWxStudentSubscribeService {
    /**
     * 通过发布表id和学生id查询
     *
     * @param attendReleaseId 发布表id
     * @param studentId       学生id
     * @return 数据
     */
    Optional<AttendWxStudentSubscribeRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId);

    /**
     * 保存
     *
     * @param attendWxStudentSubscribe 数据
     */
    void save(AttendWxStudentSubscribe attendWxStudentSubscribe);

    /**
     * 通过发布表id和学生id删除
     *
     * @param attendReleaseId 发布表id
     * @param studentId       学生id
     */
    void deleteByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId);
}
