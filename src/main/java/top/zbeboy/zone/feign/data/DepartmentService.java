package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.hystrix.data.DepartmentHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentAddVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentEditVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = DepartmentHystrixClientFallbackFactory.class)
public interface DepartmentService {

    /**
     * 获取系
     *
     * @param id 系主键
     * @return 系数据
     */
    @GetMapping("/base/data/department/{id}")
    Department findById(@PathVariable("id") int id);

    /**
     * 获取系
     *
     * @param id 系主键
     * @return 系数据
     */
    @GetMapping("/base/data/department_relation/{id}")
    DepartmentBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取院下全部有效系
     *
     * @param departmentSearchVo 查询参数
     * @return 系数据
     */
    @PostMapping("/base/data/departments/search")
    List<Department> findByCollegeIdAndDepartmentIsDel(@RequestBody DepartmentSearchVo departmentSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/departments/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验系名是否重复
     *
     * @param departmentName 系名
     * @param collegeId      院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/department/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("departmentName") String departmentName, @RequestParam("collegeId") int collegeId);

    /**
     * 保存系信息
     *
     * @param departmentAddVo 系
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/department/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody DepartmentAddVo departmentAddVo);

    /**
     * 检验编辑时系名重复
     *
     * @param departmentId   系id
     * @param departmentName 系名
     * @param collegeId      院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/department/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("departmentId") int departmentId, @RequestParam("departmentName") String departmentName, @RequestParam("collegeId") int collegeId);

    /**
     * 保存系更改
     *
     * @param departmentEditVo 系
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/department/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody DepartmentEditVo departmentEditVo);

    /**
     * 批量更改系状态
     *
     * @param departmentIds 系ids
     * @param isDel         is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/departments/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "departmentIds", required = false) String departmentIds, @RequestParam("isDel") Byte isDel);
}
