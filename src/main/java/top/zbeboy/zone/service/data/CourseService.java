package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.domain.tables.records.CourseRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

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
    Result<CourseRecord> findBySchoolYearAndTermAndCollegeIdAndCourseIsDel(Byte schoolYear, Byte term, int collegeId, Byte courseIsDel);

    /**
     * 院下 课程名查询 注：等于课程名
     *
     * @param courseName 课程名
     * @param collegeId  院id
     * @return 数据
     */
    Result<CourseRecord> findByCourseNameAndCollegeId(String courseName, int collegeId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param course 课程
     */
    void save(Course course);
}
