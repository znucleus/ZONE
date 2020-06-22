package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.student.StudentAddVo;
import top.zbeboy.zbase.vo.data.student.StudentEditVo;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StudentHystrixClientFallbackFactory implements StudentService {
    @Override
    public StudentBean findByIdRelation(int id) {
        return new StudentBean();
    }

    @Override
    public StudentBean findByUsername(String username) {
        return new StudentBean();
    }

    @Override
    public Student findByStudentNumber(String studentNumber) {
        return new Student();
    }

    @Override
    public StudentBean findByUsernameRelation(String username) {
        return new StudentBean();
    }

    @Override
    public List<StudentBean> findNormalByOrganizeId(int organizeId) {
        return new ArrayList<>();
    }

    @Override
    public List<StudentBean> findNormalInOrganizeIds(List<Integer> organizeIds) {
        return new ArrayList<>();
    }

    @Override
    public StudentBean findNormalByStudentNumberRelation(String studentNumber) {
        return new StudentBean();
    }

    @Override
    public StudentBean findNormalByUsernameAndDepartmentId(String username, int departmentId) {
        return new StudentBean();
    }

    @Override
    public StudentBean findNormalByStudentNumberAndDepartmentId(String studentNumber, int departmentId) {
        return new StudentBean();
    }

    @Override
    public List<Users> findByAuthorityAndCollegeId(String authority, int collegeId) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneCheckStudentNumber(String studentNumber) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCheckStudentNumber(String username, String studentNumber) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userCheckStatusByStudentNumber(String studentNumber) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(StudentAddVo studentAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userStudentUpdateSchool(StudentEditVo studentEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userStudentUpdateInfo(@Valid StudentEditVo studentEditVo) {
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
