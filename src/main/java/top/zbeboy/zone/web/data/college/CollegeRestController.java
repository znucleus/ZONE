package top.zbeboy.zone.web.data.college;

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
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;
import top.zbeboy.zone.service.data.CollegeService;
import top.zbeboy.zone.web.bean.data.college.CollegeBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.college.CollegeAddVo;
import top.zbeboy.zone.web.vo.data.college.CollegeEditVo;
import top.zbeboy.zone.web.vo.data.college.CollegeSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CollegeRestController {

    @Resource
    private CollegeService collegeService;

    /**
     * 获取学校下全部有效院
     *
     * @param collegeSearchVo 查询参数
     * @return 院数据
     */
    @GetMapping("/anyone/data/college")
    public ResponseEntity<Map<String, Object>> anyoneData(CollegeSearchVo collegeSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<CollegeRecord> colleges = collegeService.findBySchoolIdAndCollegeIsDel(collegeSearchVo.getSchoolId(), BooleanUtil.toByte(false));
        colleges.forEach(college -> select2Data.add(college.getCollegeId().toString(), college.getCollegeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/college/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("collegeId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("collegeCode");
        headers.add("collegeAddress");
        headers.add("collegeIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = collegeService.findAllByPage(dataTablesUtil);
        List<CollegeBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(CollegeBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(collegeService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(collegeService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验院名是否重复
     *
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/college/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("collegeName") String collegeName,
                                                            @RequestParam("schoolId") int schoolId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(collegeName);
        Result<CollegeRecord> collegeRecords = collegeService.findByCollegeNameAndSchoolId(param, schoolId);
        if (collegeRecords.isEmpty()) {
            ajaxUtil.success().msg("院名不重复");
        } else {
            ajaxUtil.fail().msg("院名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验院代码是否重复
     *
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/college/check/add/code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("collegeCode") String collegeCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(collegeCode);
        List<College> colleges = collegeService.findByCollegeCode(param);
        if (Objects.isNull(colleges) || colleges.isEmpty()) {
            ajaxUtil.success().msg("院代码不重复");
        } else {
            ajaxUtil.fail().msg("院代码重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存院信息
     *
     * @param collegeAddVo  院
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/college/save")
    public ResponseEntity<Map<String, Object>> save(@Valid CollegeAddVo collegeAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            College college = new College();
            college.setCollegeIsDel(ByteUtil.toByte(1).equals(collegeAddVo.getCollegeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            college.setCollegeName(collegeAddVo.getCollegeName());
            college.setCollegeCode(collegeAddVo.getCollegeCode());
            college.setCollegeAddress(collegeAddVo.getCollegeAddress());
            college.setSchoolId(collegeAddVo.getSchoolId());
            collegeService.save(college);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时院名重复
     *
     * @param collegeId   院id
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/college/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("collegeId") int collegeId,
                                                             @RequestParam("collegeName") String collegeName,
                                                             @RequestParam("schoolId") int schoolId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(collegeName);
        Result<CollegeRecord> collegeRecords = collegeService.findByCollegeNameAndSchoolIdNeCollegeId(param, collegeId, schoolId);
        if (collegeRecords.isEmpty()) {
            ajaxUtil.success().msg("院名不重复");
        } else {
            ajaxUtil.fail().msg("院名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时院代码重复
     *
     * @param collegeId   院id
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/college/check/edit/code")
    public ResponseEntity<Map<String, Object>> checkEditCode(@RequestParam("collegeId") int collegeId,
                                                             @RequestParam("collegeCode") String collegeCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(collegeCode);
        Result<CollegeRecord> collegeRecords = collegeService.findByCollegeCodeNeCollegeId(param, collegeId);
        if (collegeRecords.isEmpty()) {
            ajaxUtil.success().msg("院代码不重复");
        } else {
            ajaxUtil.fail().msg("院代码重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存院更改
     *
     * @param collegeEditVo 院
     * @param bindingResult 检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/college/update")
    public ResponseEntity<Map<String, Object>> update(@Valid CollegeEditVo collegeEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            College college = collegeService.findById(collegeEditVo.getCollegeId());
            if (Objects.nonNull(college)) {
                college.setCollegeIsDel(ByteUtil.toByte(1).equals(collegeEditVo.getCollegeIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                college.setCollegeName(collegeEditVo.getCollegeName());
                college.setCollegeCode(collegeEditVo.getCollegeCode());
                college.setCollegeAddress(collegeEditVo.getCollegeAddress());
                college.setSchoolId(collegeEditVo.getSchoolId());
                collegeService.update(college);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据院ID未查询到院数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改院状态
     *
     * @param collegeIds 院ids
     * @param isDel      is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/college/status")
    public ResponseEntity<Map<String, Object>> status(String collegeIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(collegeIds)) {
            collegeService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(collegeIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择院");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
