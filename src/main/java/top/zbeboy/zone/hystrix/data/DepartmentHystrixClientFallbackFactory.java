package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.feign.data.DepartmentService;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentAddVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentEditVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentHystrixClientFallbackFactory implements DepartmentService {
    @Override
    public Department findById(int id) {
        return new Department();
    }

    @Override
    public DepartmentBean findByIdRelation(int id) {
        return new DepartmentBean();
    }

    @Override
    public List<Department> findByCollegeIdAndDepartmentIsDel(DepartmentSearchVo departmentSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String departmentName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(DepartmentAddVo departmentAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int departmentId, String departmentName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(DepartmentEditVo departmentEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String departmentIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
