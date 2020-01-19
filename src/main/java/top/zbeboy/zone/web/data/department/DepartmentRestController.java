package top.zbeboy.zone.web.data.department;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;
import top.zbeboy.zone.service.data.DepartmentService;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentAddVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentEditVo;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Result<DepartmentRecord> departments = departmentService.findByCollegeIdAndDepartmentIsDel(departmentSearchVo.getCollegeId(), BooleanUtil.toByte(false));
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
        Result<Record> records = departmentService.findAllByPage(dataTablesUtil);
        List<DepartmentBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(DepartmentBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(departmentService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(departmentService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验系名是否重复
     *
     * @param departmentName 系名
     * @param collegeId      院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/department/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("departmentName") String departmentName, @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(departmentName);
        Result<DepartmentRecord> departmentRecords = departmentService.findByDepartmentNameAndCollegeId(param, collegeId);
        if (departmentRecords.isEmpty()) {
            ajaxUtil.success().msg("系名不重复");
        } else {
            ajaxUtil.fail().msg("系名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存系信息
     *
     * @param departmentAddVo 系
     * @param bindingResult   检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/department/save")
    public ResponseEntity<Map<String, Object>> save(@Valid DepartmentAddVo departmentAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Department department = new Department();
            department.setDepartmentIsDel(ByteUtil.toByte(1).equals(departmentAddVo.getDepartmentIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            department.setDepartmentName(departmentAddVo.getDepartmentName());
            department.setCollegeId(departmentAddVo.getCollegeId());
            departmentService.save(department);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
                                                             @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(departmentName);
        Result<DepartmentRecord> departmentRecords = departmentService.findByDepartmentNameAndCollegeIdNeDepartmentId(param, collegeId, departmentId);
        if (departmentRecords.isEmpty()) {
            ajaxUtil.success().msg("系名不重复");
        } else {
            ajaxUtil.fail().msg("系名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存系更改
     *
     * @param departmentEditVo 系
     * @param bindingResult    检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/department/update")
    public ResponseEntity<Map<String, Object>> update(@Valid DepartmentEditVo departmentEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Department department = departmentService.findById(departmentEditVo.getDepartmentId());
            if (Objects.nonNull(department)) {
                department.setDepartmentIsDel(ByteUtil.toByte(1).equals(departmentEditVo.getDepartmentIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                department.setDepartmentName(departmentEditVo.getDepartmentName());
                department.setCollegeId(departmentEditVo.getCollegeId());
                departmentService.update(department);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据系ID未查询到系数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(departmentIds)) {
            departmentService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(departmentIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择系");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
