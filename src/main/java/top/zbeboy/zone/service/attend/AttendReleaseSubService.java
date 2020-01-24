package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.List;

public interface AttendReleaseSubService {

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
     * @param attendReleaseSub 数据
     */
    void save(AttendReleaseSub attendReleaseSub);

    /**
     * 复制主表
     *
     * @param attendReleaseId 发布id
     */
    void copyAttendRelease(String attendReleaseId);

    /**
     * 保存
     *
     * @param attendReleaseSubs 数据
     */
    void batchSave(List<AttendReleaseSub> attendReleaseSubs);
}
