package top.zbeboy.zone.service.notify;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Optional;

public interface UserNotifyService {

    /**
     * 通过主键与接收人账号关联查询
     *
     * @param id         主键
     * @param acceptUser 接收人账号
     * @return 数据
     */
    Optional<Record> findByIdAndAcceptUserRelation(String id, String acceptUser);

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
     * 通过条件统计
     *
     * @param acceptUser 接收人
     * @param isSee      是否已看
     * @return 数量
     */
    int countByAcceptUserAndIsSee(String acceptUser, Byte isSee);

    /**
     * 更新
     *
     * @param userNotify 数据
     */
    void update(UserNotify userNotify);

    /**
     * 保存
     *
     * @param userNotify 数据
     */
    void save(UserNotify userNotify);
}
