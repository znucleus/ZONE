package top.zbeboy.zone.service.platform;

import org.jooq.Record12;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.UsersRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

public interface UsersService {

    /**
     * 根据账号查询用户信息(全部)
     *
     * @param username 账号
     * @return 用户全部信息
     */
    Users findByUsername(String username);

    /**
     * 查询正常状态的账号
     *
     * @param username 账号
     * @return 正常账号
     */
    UsersRecord findNormalByUsername(String username);

    /**
     * 账号大写查询
     *
     * @param username 账号
     * @return 查询结果
     */
    UsersRecord findByUsernameUpper(String username);

    /**
     * 检验账号是否被占用
     *
     * @param username 账号
     * @param own      当前账号
     * @return 检验账号
     */
    Result<UsersRecord> findByUsernameNeOwn(String username, String own);

    /**
     * 检查身份证号是否唯一
     *
     * @param idCard 身份证号
     * @param own    账号
     * @return 检验身份证号
     */
    Result<UsersRecord> findByIdCardNeOwn(String idCard, String own);

    /**
     * 检查邮箱是否占用
     *
     * @param email 邮箱
     * @param own   账号
     * @return 检验邮箱
     */
    Result<UsersRecord> findByEmailNeOwn(String email, String own);

    /**
     * 检查手机号是否占用
     *
     * @param mobile 手机号
     * @param own    账号
     * @return 检验手机号
     */
    Result<UsersRecord> findByMobileNeOwn(String mobile, String own);

    /**
     * 根据邮箱查询用户信息(全部)
     *
     * @param email 账号
     * @return 用户全部信息
     */
    Users findByEmail(String email);

    /**
     * 根据手机号查询用户信息(全部)
     *
     * @param mobile 手机号
     * @return 用户全部信息
     */
    Users findByMobile(String mobile);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record12<String, String, String, String, String, Byte, String, String, Byte, Byte, String, Date>> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 注意 用于定时 查询未验证用户
     *
     * @param joinDate      加入时间
     * @param verifyMailbox 验证否
     * @return 用户
     */
    Result<UsersRecord> findByJoinDateAndVerifyMailbox(Date joinDate, Byte verifyMailbox);

    /**
     * 从session中获取用户完整信息
     *
     * @return session中的用户信息
     */
    Users getUserFromSession();

    /**
     * 从oauth中获取用户完整信息
     *
     * @return oauth中的用户信息
     */
    Users getUserFromOauth(Principal principal);

    /**
     * 从session中获取用户完整权限
     *
     * @return session中的完整权限
     */
    List<String> getAuthoritiesFromSession();

    /**
     * 保存
     *
     * @param users 数据
     */
    void save(Users users);

    /**
     * 更新
     *
     * @param users 数据
     */
    void update(Users users);

    /**
     * 更新账号
     *
     * @param oldName 旧账号
     * @param newName 新账号
     */
    void updateUsername(String oldName, String newName);

    /**
     * 更新注销状态
     *
     * @param ids     ids
     * @param enabled 状态
     */
    void updateEnabled(List<String> ids, Byte enabled);

    /**
     * 更新锁定状态
     *
     * @param ids    ids
     * @param locked 锁定
     */
    void updateLocked(List<String> ids, Byte locked);

    /**
     * 更新密码
     *
     * @param username 账号
     * @param password 密码
     */
    void updatePassword(String username, String password);

    /**
     * 解锁用户
     */
    void unlockUsers();

    /**
     * 删除账号
     *
     * @param users 账号
     */
    void delete(List<Users> users);

    /**
     * 删除账号
     *
     * @param users 账号
     */
    void deleteById(List<String> users);
}
