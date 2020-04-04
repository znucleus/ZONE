package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.web.util.pagination.TableSawUtil;

public interface TrainingAttendMyService {

    /**
     * 全量查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAll(TableSawUtil paginationUtil);
}
