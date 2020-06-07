package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.hystrix.data.StudentHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.student.StudentAddVo;
import top.zbeboy.zone.web.vo.data.student.StudentEditVo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = StudentHystrixClientFallbackFactory.class)
public interface StudentService {

    /**
     * 获取学生
     *
     * @param id 学生主键
     * @return 学生数据
     */
    @GetMapping("/base/student_relation/{id}")
    StudentBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取学生
     *
     * @param username 学生账号
     * @return 学生数据
     */
    @GetMapping("/base/student_username/{username}")
    StudentBean findByUsername(@PathVariable("username") String username);

    /**
     * 获取学生
     *
     * @param studentNumber 学号
     * @return 学生数据
     */
    @GetMapping("/base/student_number/{studentNumber}")
    Student findByStudentNumber(@PathVariable("studentNumber") String studentNumber);

    /**
     * 获取学生
     *
     * @param username 学生账号
     * @return 学生数据
     */
    @GetMapping("/base/student_username_relation/{username}")
    StudentBean findByUsernameRelation(@PathVariable("username") String username);


    /**
     * 根据班级id获取正常学生
     *
     * @param organizeIds 班级id
     * @return 学生数据
     */
    @PostMapping("/base/students_normal_in_organize_ids")
    List<StudentBean> findNormalInOrganizeIds(@RequestBody List<Integer> organizeIds);

    /**
     * 根据班级id获取正常学生
     *
     * @param organizeId 班级id
     * @return 学生数据
     */
    @GetMapping("/base/students_normal_organize_id/{organizeId}")
    List<StudentBean> findNormalByOrganizeId(@PathVariable("organizeId") int organizeId);

    /**
     * 根据学号获取正常学生
     *
     * @param studentNumber 学号
     * @return 学生数据
     */
    @GetMapping("/base/students_normal_number_relation/{studentNumber}")
    StudentBean findNormalByStudentNumberRelation(@PathVariable("studentNumber") String studentNumber);

    /**
     * 根据账号和系id获取正常学生
     *
     * @param username     账号
     * @param departmentId 系id
     * @return 学生数据
     */
    @GetMapping("/base/students_normal_username_department_id/{username}/{departmentId}")
    StudentBean findNormalByUsernameAndDepartmentId(@PathVariable("username") String username, @PathVariable("departmentId") int departmentId);

    /**
     * 根据学号和系id获取正常学生
     *
     * @param studentNumber 学号
     * @param departmentId  系id
     * @return 学生数据
     */
    @GetMapping("/base/students_normal_number_department_id/{studentNumber}/{departmentId}")
    StudentBean findNormalByStudentNumberAndDepartmentId(@PathVariable("studentNumber") String studentNumber, @PathVariable("departmentId") int departmentId);

    /**
     * 根据角色和院id获取正常学生
     *
     * @param authority 权限
     * @param collegeId 院id
     * @return 教职工数据
     */
    @GetMapping("/base/student_authority_college_id/{authority}/{collegeId}")
    List<Users> findByAuthorityAndCollegeId(@PathVariable("authority") String authority, @PathVariable("collegeId") int collegeId);

    /**
     * 检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/base/student/anyone_check_student_number")
    AjaxUtil<Map<String, Object>> anyoneCheckStudentNumber(@RequestParam("studentNumber") String studentNumber);

    /**
     * 更新时检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/base/student/user_check_student_number")
    AjaxUtil<Map<String, Object>> userCheckStudentNumber(@RequestParam("username") String username, @RequestParam("studentNumber") String studentNumber);

    /**
     * 根据学号检验是否存在以及该用户状态是否正常
     *
     * @param studentNumber 学号
     * @return 是否存在以及该用户状态是否正常
     */
    @PostMapping("/base/student/user_check_student_status")
    AjaxUtil<Map<String, Object>> userCheckStatusByStudentNumber(@RequestParam("studentNumber") String studentNumber);

    /**
     * 学生注册
     *
     * @param studentAddVo 学生数据
     * @return 注册
     */
    @PostMapping("/base/student/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody StudentAddVo studentAddVo);

    /**
     * 学生班级更新
     *
     * @param studentEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/base/student/update_school")
    AjaxUtil<Map<String, Object>> userStudentUpdateSchool(@RequestBody StudentEditVo studentEditVo);

    /**
     * 更新信息
     *
     * @param studentEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/base/student/update_info")
    AjaxUtil<Map<String, Object>> userStudentUpdateInfo(@RequestBody @Valid StudentEditVo studentEditVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/students/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 用户角色数据
     *
     * @param username       用户账号
     * @param targetUsername 目标账号
     * @return 数据
     */
    @PostMapping("/base/data/student/roles")
    List<Role> roleData(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @return success or false
     */
    @PostMapping("/base/data/student/role/save")
    AjaxUtil<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername, @RequestParam("roles") String roles);

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/base/data/student/update_enabled")
    AjaxUtil<Map<String, Object>> updateEnabled(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds, @RequestParam("enabled") Byte enabled);

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/base/data/student/update_locked")
    AjaxUtil<Map<String, Object>> updateLocked(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds, @RequestParam("locked") Byte locked);

    /**
     * 更新密码
     *
     * @param username       账号
     * @param targetUsername 目标账号
     * @return success or fail
     */
    @PostMapping("/base/data/student/update_password")
    AjaxUtil<Map<String, Object>> updatePassword(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/base/data/student/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds);
}
