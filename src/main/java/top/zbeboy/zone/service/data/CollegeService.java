package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;

import java.util.Optional;

public interface CollegeService {

    /**
     * 通过id关联查询
     *
     * @param id 院id
     * @return 数据
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据学校ID和状态查询全部院
     *
     * @param schoolId     学校ID
     * @param collegeIsDel 状态
     * @return 全部院
     */
    Result<CollegeRecord> findBySchoolIdAndCollegeIsDel(int schoolId, Byte collegeIsDel);
}
