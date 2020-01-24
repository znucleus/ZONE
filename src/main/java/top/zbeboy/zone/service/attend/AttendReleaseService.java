package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.records.AttendReleaseRecord;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

public interface AttendReleaseService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AttendRelease findById(String id);

    /**
     * 查询自动生成数据
     *
     * @return 数据
     */
    Result<AttendReleaseRecord> findIsAuto();

    /**
     * 分布查询
     *
     * @param paginationUtil 数据
     * @return 数据
     */
    Result<Record> findAllByPage(SimplePaginationUtil paginationUtil);

    /**
     * 总数
     *
     * @return 总数
     */
    int countAll(SimplePaginationUtil paginationUtil);

    /**
     * 保存
     *
     * @param attendRelease 数据
     */
    void save(AttendRelease attendRelease);

    /**
     * 更新
     *
     * @param attendRelease 数据
     */
    void update(AttendRelease attendRelease);
}
