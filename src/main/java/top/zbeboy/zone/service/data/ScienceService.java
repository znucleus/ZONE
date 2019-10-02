package top.zbeboy.zone.service.data;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;

public interface ScienceService {

    /**
     * 根据系ID和状态查询全部专业
     *
     * @param departmentId 系ID
     * @param scienceIsDel 状态
     * @return 全部专业
     */
    Result<ScienceRecord> findByDepartmentIdAndScienceIsDel(int departmentId, Byte scienceIsDel);
}
