package top.zbeboy.zone.service.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.List;
import java.util.Optional;

public interface AttendReleaseSubService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AttendReleaseSub findById(int id);

    /**
     * 根据主键关联查询
     * 缓存:是
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(int id);

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

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(int id);

    /**
     * 更新
     *
     * @param attendReleaseSub 数据
     */
    void update(AttendReleaseSub attendReleaseSub);
}
