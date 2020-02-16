package top.zbeboy.zone.service.system;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import top.zbeboy.zone.domain.tables.pojos.Authorities;
import top.zbeboy.zone.domain.tables.records.AuthoritiesRecord;

import java.util.List;

public interface AuthoritiesService {

    /**
     * 通过账号和权限查询
     *
     * @param username    账号
     * @param authorities 权限
     * @return 数据
     */
    Result<Record> findByUsernameAndInAuthorities(String username, List<String> authorities);

    /**
     * 根据用户账号查询权限
     *
     * @param username 账号
     * @return 用户权限
     */
    List<Authorities> findByUsername(String username);

    /**
     * Check if users is login by remember me cookie, refer
     * org.springframework.security.authentication.AuthenticationTrustResolverImpl
     *
     * @return true or false
     */
    Boolean isAnonymousAuthenticated();

    /**
     * 权限exist
     *
     * @return exist
     */
    Select<AuthoritiesRecord> existsAuthoritiesSelect();

    /**
     * 批量保存
     *
     * @param authorities 数据
     */
    void batchSave(List<Authorities> authorities);

    /**
     * 通过权限删除
     *
     * @param authorities 权限
     */
    void deleteByAuthorities(String authorities);

    /**
     * 通过账号删除
     *
     * @param username 账号
     */
    void deleteByUsername(String username);
}
