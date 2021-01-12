package top.zbeboy.zone.service.theory;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;

public interface TheoryAttendMyService {
    /**
     * 全量查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAll(TableSawUtil paginationUtil);
}
