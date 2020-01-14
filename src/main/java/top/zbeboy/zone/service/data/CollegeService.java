package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Optional;

public interface CollegeService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    College findById(int id);

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

    /**
     * 学校下 院名查询 注：等于院名
     *
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return 数据
     */
    Result<CollegeRecord> findByCollegeNameAndSchoolId(String collegeName, int schoolId);

    /**
     * 查找学校下不等于该院id的院名
     *
     * @param collegeName 院名
     * @param collegeId   院id
     * @param schoolId    学校id
     * @return 院
     */
    Result<CollegeRecord> findByCollegeNameAndSchoolIdNeCollegeId(String collegeName, int collegeId, int schoolId);

    /**
     * 院代码查询 注：等于院代码
     *
     * @param collegeCode 院代码
     * @return 数据
     */
    List<College> findByCollegeCode(String collegeCode);

    /**
     * 学校下 院代码查询 注：不等于院id
     *
     * @param collegeCode 院代码
     * @param collegeId   院id
     * @return 数据
     */
    Result<CollegeRecord> findByCollegeCodeNeCollegeId(String collegeCode, int collegeId);

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
    int countAll();

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param college 数据
     */
    void save(College college);

    /**
     * 更新
     *
     * @param college 数据
     */
    void update(College college);

    /**
     * 更新状态
     *
     * @param ids     ids
     * @param isDel 状态
     */
    void updateIsDel(List<Integer> ids, Byte isDel);
}
