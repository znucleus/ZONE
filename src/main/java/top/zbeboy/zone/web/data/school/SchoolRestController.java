package top.zbeboy.zone.web.data.school;

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
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.service.data.SchoolService;
import top.zbeboy.zone.web.bean.data.school.SchoolBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.school.SchoolAddVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SchoolRestController {

    @Resource
    private SchoolService schoolService;

    /**
     * 获取全部有效学校
     *
     * @return 学校数据
     */
    @GetMapping("/anyone/data/school")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<School> schools = schoolService.findBySchoolIsDel(BooleanUtil.toByte(false));
        schools.forEach(school -> select2Data.add(school.getSchoolId().toString(), school.getSchoolName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/school/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("schoolId");
        headers.add("schoolName");
        headers.add("schoolIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = schoolService.findAllByPage(dataTablesUtil);
        List<SchoolBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SchoolBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(schoolService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(schoolService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验学校名是否重复
     *
     * @param schoolName 学校名
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/school/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("schoolName") String schoolName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(schoolName);

        List<School> schools = schoolService.findBySchoolName(param);
        if (Objects.isNull(schools) || schools.isEmpty()) {
            ajaxUtil.success().msg("学校名不重复");
        } else {
            ajaxUtil.fail().msg("学校名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存学校信息
     *
     * @param schoolAddVo   学校
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/school/save")
    public ResponseEntity<Map<String, Object>> save(@Valid SchoolAddVo schoolAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            School school = new School();
            school.setSchoolIsDel(ByteUtil.toByte(1).equals(schoolAddVo.getSchoolIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            school.setSchoolName(schoolAddVo.getSchoolName());
            schoolService.save(school);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
