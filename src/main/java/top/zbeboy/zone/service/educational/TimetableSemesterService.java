package top.zbeboy.zone.service.educational;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.records.TimetableSemesterRecord;

import java.util.Optional;

public interface TimetableSemesterService {

    /**
     * 通过主键查询
     *
     * @param timetableSemesterId 主键
     * @return 数据
     */
    TimetableSemester findByTimetableSemesterId(String timetableSemesterId);

    /**
     * 查询全部学年
     *
     * @return 数据
     */
    Result<Record> findAll();

    /**
     * 根据院id查询全部学年
     *
     * @param collegeId 院id
     * @return 数据
     */
    Result<Record> findByCollegeId(int collegeId);

    /**
     * 根据id与院id查询
     *
     * @param id        主键
     * @param collegeId 院id
     * @return 数据
     */
    Optional<TimetableSemesterRecord> findByIdAndCollegeId(int id, int collegeId);

    /**
     * 更新
     *
     * @param timetableSemester 数据
     */
    void update(TimetableSemester timetableSemester);

    /**
     * 保存
     *
     * @param timetableSemester 数据
     */
    void save(TimetableSemester timetableSemester);
}
