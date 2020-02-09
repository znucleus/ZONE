package top.zbeboy.zone.service.data;

import org.jooq.Record;
import org.jooq.Record21;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;

import java.sql.Date;
import java.util.Optional;

public interface StaffService {

    /**
     * 根据主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(int id);

    /**
     * 根据账号查询信息
     *
     * @param username 账号
     * @return 信息
     */
    Optional<StaffRecord> findByUsername(String username);

    /**
     * 根据账号查询所有信息
     * 缓存:是
     *
     * @param username 账号
     * @return 所有信息
     */
    Optional<Record> findByUsernameRelation(String username);

    /**
     * 根据账号或工号查询
     *
     * @param staff 账号/工号
     * @return 数据
     */
    Optional<Record> findByUsernameOrStaffNumberRelation(String staff);

    /**
     * 查询院管理员
     *
     * @param authority 权限
     * @param collegeId 院id
     * @return 数据
     */
    Result<Record> findAdmin(String authority, int collegeId);

    /**
     * 根据工号查询教职工信息(全部)
     *
     * @param staffNumber 工号
     * @return 教职工全部信息
     */
    Staff findByStaffNumber(String staffNumber);

    /**
     * 更新时检验账号是否被占用
     *
     * @param staffNumber 工号
     * @param username    当前账号
     * @return 检验工号
     */
    Result<StaffRecord> findByStaffNumberNeUsername(String staffNumber, String username);

    /**
     * 根据系id查询状态正常的教职工
     *
     * @param departmentId 系id
     * @return 数据
     */
    Result<Record> findNormalByDepartmentIdRelation(int departmentId);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>> findAllByPage(DataTablesUtil dataTablesUtil);

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
     * 保存
     *
     * @param staff 数据
     */
    void save(Staff staff);

    /**
     * jooq 事务性保存
     *
     * @param staffAddVo 数据
     */
    void saveWithUsers(StaffAddVo staffAddVo);

    /**
     * 更新
     *
     * @param staff 数据
     */
    void update(Staff staff);
}
