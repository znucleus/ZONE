package top.zbeboy.zone.web.data.schoolroom;

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
import top.zbeboy.zone.domain.tables.pojos.Schoolroom;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.service.data.SchoolroomService;
import top.zbeboy.zone.web.bean.data.schoolroom.SchoolroomBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomAddVo;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomEditVo;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SchoolroomRestController {

    @Resource
    private SchoolroomService schoolroomService;

    /**
     * 获取楼下全部有效教室
     *
     * @param schoolroomSearchVo 查询参数
     * @return 教室数据
     */
    @GetMapping("/users/data/schoolroom")
    public ResponseEntity<Map<String, Object>> usersData(SchoolroomSearchVo schoolroomSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<SchoolroomRecord> schoolrooms = schoolroomService.findByBuildingIdAndSchoolroomIsDel(schoolroomSearchVo.getBuildingId(), BooleanUtil.toByte(false));
        schoolrooms.forEach(schoolroom -> select2Data.add(schoolroom.getSchoolroomId().toString(), schoolroom.getBuildingCode()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/schoolroom/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("schoolroomId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("buildingName");
        headers.add("buildingCode");
        headers.add("schoolroomIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = schoolroomService.findAllByPage(dataTablesUtil);
        List<SchoolroomBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SchoolroomBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(schoolroomService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(schoolroomService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验教室是否重复
     *
     * @param buildingCode 教室名
     * @param buildingId   楼id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/schoolroom/check/add/code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("buildingCode") String buildingCode, @RequestParam("buildingId") int buildingId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(buildingCode);
        Result<SchoolroomRecord> records = schoolroomService.findByBuildingCodeAndBuildingId(param, buildingId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("教室不重复");
        } else {
            ajaxUtil.fail().msg("教室重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存教室信息
     *
     * @param schoolroomAddVo 教室
     * @param bindingResult   检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/schoolroom/save")
    public ResponseEntity<Map<String, Object>> save(@Valid SchoolroomAddVo schoolroomAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Schoolroom schoolroom = new Schoolroom();
            schoolroom.setSchoolroomIsDel(ByteUtil.toByte(1).equals(schoolroomAddVo.getSchoolroomIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            schoolroom.setBuildingCode(schoolroomAddVo.getBuildingCode());
            schoolroom.setBuildingId(schoolroomAddVo.getBuildingId());
            schoolroomService.save(schoolroom);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时教室重复
     *
     * @param schoolroomId 教室id
     * @param buildingCode 教室
     * @param buildingId   楼id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/schoolroom/check/edit/code")
    public ResponseEntity<Map<String, Object>> checkEditCode(@RequestParam("schoolroomId") int schoolroomId,
                                                             @RequestParam("buildingCode") String buildingCode,
                                                             @RequestParam("buildingId") int buildingId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(buildingCode);
        Result<SchoolroomRecord> records = schoolroomService.findByBuildingCodeAndBuildingIdNeSchoolroomId(param, buildingId, schoolroomId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("教室不重复");
        } else {
            ajaxUtil.fail().msg("教室重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存教室更改
     *
     * @param schoolroomEditVo 教室
     * @param bindingResult    检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/schoolroom/update")
    public ResponseEntity<Map<String, Object>> update(@Valid SchoolroomEditVo schoolroomEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Schoolroom schoolroom = schoolroomService.findById(schoolroomEditVo.getSchoolroomId());
            if (Objects.nonNull(schoolroom)) {
                schoolroom.setSchoolroomIsDel(ByteUtil.toByte(1).equals(schoolroomEditVo.getSchoolroomIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                schoolroom.setBuildingCode(schoolroomEditVo.getBuildingCode());
                schoolroom.setBuildingId(schoolroomEditVo.getBuildingId());
                schoolroomService.update(schoolroom);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据教室ID未查询到教室数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改教室状态
     *
     * @param schoolroomIds 教室ids
     * @param isDel         is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/schoolroom/status")
    public ResponseEntity<Map<String, Object>> status(String schoolroomIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(schoolroomIds)) {
            schoolroomService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(schoolroomIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择教室");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
