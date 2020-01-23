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
import top.zbeboy.zone.domain.tables.records.GradeRecord;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;
import top.zbeboy.zone.service.data.GradeService;
import top.zbeboy.zone.service.data.OrganizeService;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.organize.OrganizeAddVo;
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
            if (gradeRecord.isPresent()) {
                Grade grade = gradeRecord.get().into(Grade.class);
                Organize organize = new Organize();
                organize.setOrganizeIsDel(ByteUtil.toByte(1).equals(organizeAddVo.getOrganizeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                organize.setOrganizeName(organizeAddVo.getOrganizeName());
                organize.setGradeId(grade.getGradeId());
                // TODO:班主任忘记了
                organizeService.save(organize);
                ajaxUtil.success().msg("保存成功");
            } else {
                // 保存年级
                Grade grade = new Grade();
                grade.setGrade(organizeAddVo.getGrade());
                grade.setGradeIsDel(BooleanUtil.toByte(false));
                grade.setScienceId(organizeAddVo.getScienceId());
                GradeRecord record = gradeService.save(grade);

                if (Objects.nonNull(record)) {
                    Organize organize = new Organize();
                    organize.setOrganizeIsDel(ByteUtil.toByte(1).equals(organizeAddVo.getOrganizeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                    organize.setOrganizeName(organizeAddVo.getOrganizeName());
                    organize.setGradeId(record.getGradeId());
                    // TODO:班主任忘记了
                    organizeService.save(organize);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("保存年级失败");
                }
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
