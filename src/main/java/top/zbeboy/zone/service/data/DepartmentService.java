package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;

import java.util.Optional;

public interface DepartmentService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Department findById(int id);

    /**
     * 通过系id查询所有信息
     * 缓存:是
     *
     * @param id 系id
     * @return 所有信息
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据院ID和状态查询全部系
     *
     * @param collegeId       院ID
     * @param departmentIsDel 状态
     * @return 全部系
     */
    Result<DepartmentRecord> findByCollegeIdAndDepartmentIsDel(int collegeId, Byte departmentIsDel);
}
