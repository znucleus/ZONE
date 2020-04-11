package top.zbeboy.zone.service.register;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

public interface LeaverRegisterReleaseService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    LeaverRegisterRelease findById(String id);

    /**
     * 分页查询
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
     * @param leaverRegisterRelease 数据
     */
    void save(LeaverRegisterRelease leaverRegisterRelease);

    /**
     * 更新
     *
     * @param leaverRegisterRelease 数据
     */
    void update(LeaverRegisterRelease leaverRegisterRelease);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
