package top.zbeboy.zone.web.data.department;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.feign.data.DepartmentService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentAddVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentEditVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DepartmentRestController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 获取院下全部有效系
     *
     * @param departmentSearchVo 查询参数
     * @return 系数据
     */
    @GetMapping("/anyone/data/department")
    public ResponseEntity<Map<String, Object>> anyoneData(DepartmentSearchVo departmentSearchVo) {
        Select2Data select2Data = Select2Data.of();
        List<Department> departments = departmentService.findByCollegeIdAndDepartmentIsDel(departmentSearchVo);
        departments.forEach(department -> select2Data.add(department.getDepartmentId().toString(), department.getDepartmentName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/department/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("departmentId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("departmentIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        return new ResponseEntity<>(departmentService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验系名是否重复
     *
     * @param departmentName 系名
     * @param collegeId      院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/department/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("departmentName") String departmentName, @RequestParam("collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = departmentService.checkAddName(departmentName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存系信息
     *
     * @param departmentAddVo 系
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/department/save")
    public ResponseEntity<Map<String, Object>> save(DepartmentAddVo departmentAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = departmentService.save(departmentAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时系名重复
     *
     * @param departmentId   系id
     * @param departmentName 系名
     * @param collegeId      院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/department/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("departmentId") int departmentId,
                                                             @RequestParam("departmentName") String departmentName,
                                                             @RequestParam("collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = departmentService.checkEditName(departmentId, departmentName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存系更改
     *
     * @param departmentEditVo 系
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/department/update")
    public ResponseEntity<Map<String, Object>> update(DepartmentEditVo departmentEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = departmentService.update(departmentEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改系状态
     *
     * @param departmentIds 系ids
     * @param isDel         is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/department/status")
    public ResponseEntity<Map<String, Object>> status(String departmentIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = departmentService.status(departmentIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
