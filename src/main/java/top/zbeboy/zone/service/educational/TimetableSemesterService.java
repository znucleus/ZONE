package top.zbeboy.zone.service.educational;

import org.jooq.Result;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.records.TimetableSemesterRecord;

public interface TimetableSemesterService {

    /**
     * 查询全部学年
     *
     * @return 数据
     */
    Result<TimetableSemesterRecord> findAll();

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TimetableSemester findById(int id);

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
