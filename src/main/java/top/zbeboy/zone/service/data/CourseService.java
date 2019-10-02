package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.CourseRecord;

public interface CourseService {

    /**
     * 根据学年，学期，院id，状态查询有效课程
     *
     * @param schoolYear  学年
     * @param term        学期
     * @param collegeId   院id
     * @param courseIsDel 状态
     * @return 数据
     */
    Result<CourseRecord> findBySchoolYearAndTermAndCollegeIdCourseIsDel(Byte schoolYear, Byte term, int collegeId, Byte courseIsDel);
}
