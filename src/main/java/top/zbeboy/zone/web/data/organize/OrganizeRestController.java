package top.zbeboy.zone.web.data.organize;

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
import top.zbeboy.zone.domain.tables.pojos.Grade;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.GradeRecord;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;
import top.zbeboy.zone.service.data.GradeService;
import top.zbeboy.zone.service.data.OrganizeService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.organize.OrganizeAddVo;
import top.zbeboy.zone.web.vo.data.organize.OrganizeEditVo;
import top.zbeboy.zone.web.vo.data.organize.OrganizeSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class OrganizeRestController {

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private StaffService staffService;

    /**
     * 获取年级下全部有效班级
     *
     * @param organizeSearchVo 查询参数
     * @return 班级数据
     */
    @GetMapping("/anyone/data/organize")
    public ResponseEntity<Map<String, Object>> anyoneData(OrganizeSearchVo organizeSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<OrganizeRecord> organizes = organizeService.findByGradeIdAndOrganizeIsDel(organizeSearchVo.getGradeId(), BooleanUtil.toByte(false));
        organizes.forEach(organize -> select2Data.add(organize.getOrganizeId().toString(), organize.getOrganizeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/organize/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("organizeId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("scienceName");
        headers.add("grade");
        headers.add("organizeName");
        headers.add("realName");
        headers.add("organizeIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = organizeService.findAllByPage(dataTablesUtil);
        List<OrganizeBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(OrganizeBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(organizeService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(organizeService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验班级名是否重复
     *
     * @param organizeName 班级名
     * @param scienceId    专业id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/organize/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("organizeName") String organizeName,
                                                            @RequestParam("scienceId") int scienceId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(organizeName);
        Result<Record> scienceRecords = organizeService.findByOrganizeNameAndScienceId(param, scienceId);
        if (scienceRecords.isEmpty()) {
            ajaxUtil.success().msg("班级名不重复");
        } else {
            ajaxUtil.fail().msg("专业名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验教职工账号
     *
     * @param staff 账号/工号
     * @return true or false
     */
    @PostMapping("/web/data/organize/check/add/staff")
    public ResponseEntity<Map<String, Object>> checkAddStaff(@RequestParam("staff") String staff) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(staff);
        Optional<Record> staffRecord = staffService.findByUsernameOrStaffNumberRelation(param);
        if (staffRecord.isPresent()) {
            Users users = staffRecord.get().into(Users.class);
            Map<String, String> map = new HashMap<>();
            map.put("realName", users.getRealName());
            map.put("mobile", users.getMobile());
            ajaxUtil.success().msg("查询信息正常").put("staff", map);
        } else {
            ajaxUtil.fail().msg("未查询到教职工信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存班级信息
     *
     * @param organizeAddVo 班级
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/organize/save")
    public ResponseEntity<Map<String, Object>> save(@Valid OrganizeAddVo organizeAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Optional<GradeRecord> gradeRecord = gradeService.findByScienceIdAndGrade(organizeAddVo.getScienceId(),
                    organizeAddVo.getGrade());
            int gradeId = 0;
            if (gradeRecord.isPresent()) {
                Grade grade = gradeRecord.get().into(Grade.class);
                gradeId = grade.getGradeId();
            } else {
                // 保存年级
                Grade grade = new Grade();
                grade.setGrade(organizeAddVo.getGrade());
                grade.setGradeIsDel(BooleanUtil.toByte(false));
                grade.setScienceId(organizeAddVo.getScienceId());
                GradeRecord record = gradeService.save(grade);

                if (Objects.nonNull(record)) {
                    gradeId = record.getGradeId();
                }
            }

            if (gradeId > 0) {
                Organize organize = new Organize();
                organize.setOrganizeIsDel(ByteUtil.toByte(1).equals(organizeAddVo.getOrganizeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                organize.setOrganizeName(organizeAddVo.getOrganizeName());
                organize.setGradeId(gradeId);
                if (StringUtils.isNotBlank(organizeAddVo.getStaff())) {
                    Optional<Record> staffRecord = staffService.findByUsernameOrStaffNumberRelation(organizeAddVo.getStaff());
                    if (staffRecord.isPresent()) {
                        Staff staff = staffRecord.get().into(Staff.class);
                        organize.setStaffId(staff.getStaffId());
                        organizeService.save(organize);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("未查询到教职工信息");
                    }
                } else {
                    organizeService.save(organize);
                    ajaxUtil.success().msg("保存成功");
                }
            } else {
                ajaxUtil.fail().msg("获取年级信息失败");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验班级名是否重复
     *
     * @param organizeId   班级id
     * @param organizeName 班级名
     * @param scienceId    专业id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/organize/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("organizeId") int organizeId,
                                                             @RequestParam("organizeName") String organizeName,
                                                             @RequestParam("scienceId") int scienceId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(organizeName);
        Result<Record> organizeRecords = organizeService.findByOrganizeNameAndScienceIdNeOrganizeId(param, scienceId, organizeId);
        if (organizeRecords.isEmpty()) {
            ajaxUtil.success().msg("班级名不重复");
        } else {
            ajaxUtil.fail().msg("专业名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新班级信息
     *
     * @param organizeEditVo 班级
     * @param bindingResult  检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/organize/update")
    public ResponseEntity<Map<String, Object>> update(@Valid OrganizeEditVo organizeEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Organize organize = organizeService.findById(organizeEditVo.getOrganizeId());
            organize.setOrganizeIsDel(ByteUtil.toByte(1).equals(organizeEditVo.getOrganizeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            organize.setOrganizeName(organizeEditVo.getOrganizeName());
            organize.setGradeId(organizeEditVo.getGradeId());
            if (StringUtils.isNotBlank(organizeEditVo.getStaff())) {
                Optional<Record> staffRecord = staffService.findByUsernameOrStaffNumberRelation(organizeEditVo.getStaff());
                if (staffRecord.isPresent()) {
                    Staff staff = staffRecord.get().into(Staff.class);
                    organize.setStaffId(staff.getStaffId());
                    organizeService.update(organize);
                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("未查询到教职工信息");
                }
            } else {
                organizeService.update(organize);
                ajaxUtil.success().msg("更新成功");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改班级状态
     *
     * @param organizeIds 班级ids
     * @param isDel       is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/organize/status")
    public ResponseEntity<Map<String, Object>> status(String organizeIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(organizeIds)) {
            organizeService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(organizeIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择班级");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
