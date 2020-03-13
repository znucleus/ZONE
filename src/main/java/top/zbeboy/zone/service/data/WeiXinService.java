package top.zbeboy.zone.service.data;

import org.jooq.Record;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.domain.tables.records.WeiXinRecord;

import java.util.Optional;

public interface WeiXinService {

    /**
     * 通过账号查询
     *
     * @param username 账号
     * @param appId    应该id
     * @return 信息
     */
    Optional<WeiXinRecord> findByUsernameAndAppId(String username, String appId);

    /**
     * 通过appId查询
     *
     * @param studentId 学生id
     * @param appId     appId
     * @return 数据
     */
    Optional<Record> findByStudentIdAndAppId(int studentId, String appId);

    /**
     * 保存
     *
     * @param weiXin 微信
     */
    void save(WeiXin weiXin);

    /**
     * 更新
     *
     * @param weiXin 微信
     */
    void update(WeiXin weiXin);
}
