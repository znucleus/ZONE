package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import top.zbeboy.zone.domain.tables.pojos.AttendData;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;

import java.util.Optional;

public interface AttendDataService {

    /**
     * 根据名单ID及子表ID查询
     *
     * @param attendUsersId      名单ID
     * @param attendReleaseSubId 子表ID
     * @return 数据
     */
    Optional<AttendDataRecord> findByAttendUsersIdAndAttendReleaseSubId(String attendUsersId, int attendReleaseSubId);

    /**
     * 根据学生ID和发布表ID查询
     *
     * @param studentId          学生ID
     * @param attendReleaseId    发布表ID
     * @param attendReleaseSubId 了表ID
     * @return 数据
     */
    Optional<Record> findByStudentIdAndAttendReleaseIdAndAttendReleaseSubId(int studentId, String attendReleaseId, int attendReleaseSubId);

    /**
     * 保存
     *
     * @param attendData 数据
     */
    void save(AttendData attendData);
}
