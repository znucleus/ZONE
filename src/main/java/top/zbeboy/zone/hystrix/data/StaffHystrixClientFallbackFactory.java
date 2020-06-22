package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.staff.StaffAddVo;
import top.zbeboy.zbase.vo.data.staff.StaffEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StaffHystrixClientFallbackFactory implements StaffService {
    @Override
    public StaffBean findByUsername(String username) {
        return new StaffBean();
    }

    @Override
    public StaffBean findByIdRelation(int id) {
        return new StaffBean();
    }

    @Override
    public StaffBean findByUsernameRelation(String username) {
        return new StaffBean();
    }

    @Override
    public StaffBean findByUsernameOrStaffNumberRelation(String param) {
        return new StaffBean();
    }

    @Override
    public List<StaffBean> findNormalByDepartmentIdRelation(int departmentId) {
        return new ArrayList<>();
    }

    @Override
    public List<Users> findByAuthorityAndCollegeId(String authority, int collegeId) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckStaffNumber(String staffNumber) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCheckStaffNumber(String username, String staffNumber) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(StaffAddVo staffAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userStaffUpdateSchool(StaffEditVo staffEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userStaffUpdateInfo(StaffEditVo staffEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public List<Role> roleData(String username, String targetUsername) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> roleSave(String username, String targetUsername, String roles) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updateEnabled(String username, String userIds, Byte enabled) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updateLocked(String username, String userIds, Byte locked) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> updatePassword(String username, String targetUsername) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String userIds) {
        return AjaxUtil.of();
    }
}
