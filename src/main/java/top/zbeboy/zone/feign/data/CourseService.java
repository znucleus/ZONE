package top.zbeboy.zone.feign.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.domain.tables.records.CourseRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Course findById(int id);

    /**
     * 通过课程id查询所有信息
     * 缓存:是
     *
     * @param id 课程id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据院id，状态查询有效课程
     *
     * @param collegeId   院id
     * @param courseIsDel 状态
     * @return 数据
     */
    Result<CourseRecord> findByCollegeIdAndCourseIsDel(int collegeId, Byte courseIsDel);

    /**
     * 院下 课程名查询 注：等于课程名
     *
     * @param courseName 课程名
     * @param collegeId  院id
     * @return 数据
     */
    Result<CourseRecord> findByCourseNameAndCollegeId(String courseName, int collegeId);

    /**
     * 查找院下不等于该课程id的课程名
     *
     * @param courseName 课程名
     * @param courseId   课程id
     * @param collegeId  院id
     * @return 数据
     */
    Result<CourseRecord> findByCourseNameAndCollegeIdNeCourseId(String courseName, int collegeId, int courseId);

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

    /**
     * 更新
     *
     * @param course 数据
     */
    void update(Course course);

    /**
     * 更新状态
     *
     * @param ids   ids
     * @param isDel 状态
     */
    void updateIsDel(List<Integer> ids, Byte isDel);
}
