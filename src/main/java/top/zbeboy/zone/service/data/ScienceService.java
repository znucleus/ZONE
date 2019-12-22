package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Science;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;

public interface ScienceService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Science findById(int id);

    /**
     * 根据系ID和状态查询全部专业
     *
     * @param departmentId 系ID
     * @param scienceIsDel 状态
     * @return 全部专业
     */
    Result<ScienceRecord> findByDepartmentIdAndScienceIsDel(int departmentId, Byte scienceIsDel);
}
