package top.zbeboy.zone.web.data.college;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.College;
import top.zbeboy.zbase.domain.tables.pojos.CollegeApplication;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.CollegeService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.plugin.treeview.TreeViewData;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.college.CollegeAddVo;
import top.zbeboy.zbase.vo.data.college.CollegeEditVo;
import top.zbeboy.zbase.vo.data.college.CollegeSearchVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @ApiLoggingRecord(remark = "院数据", channel = Workbook.channel.WEB)
    @GetMapping("/anyone/data/college")
    public ResponseEntity<Map<String, Object>> anyoneData(CollegeSearchVo collegeSearchVo, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        Optional<List<College>> optionalColleges = collegeService.findBySchoolIdAndCollegeIsDel(collegeSearchVo);
        optionalColleges.ifPresent(colleges -> colleges.forEach(college -> select2Data.add(college.getCollegeId().toString(), college.getCollegeName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/college/paging")
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
        headers.add("collegeCoordinate");
        headers.add("collegeIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(collegeService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验院名是否重复
     *
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/college/check-add-name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("collegeName") String collegeName,
                                                            @RequestParam("schoolId") int schoolId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.checkAddName(collegeName, schoolId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验院代码是否重复
     *
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/college/check-add-code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("collegeCode") String collegeCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.checkAddCode(collegeCode);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存院信息
     *
     * @param collegeAddVo 院
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/college/save")
    public ResponseEntity<Map<String, Object>> save(CollegeAddVo collegeAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.save(collegeAddVo);
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
    @GetMapping("/web/data/college/check-edit-name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("collegeId") int collegeId,
                                                             @RequestParam("collegeName") String collegeName,
                                                             @RequestParam("schoolId") int schoolId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.checkEditName(collegeId, collegeName, schoolId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时院代码重复
     *
     * @param collegeId   院id
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/college/check-edit-code")
    public ResponseEntity<Map<String, Object>> checkEditCode(@RequestParam("collegeId") int collegeId,
                                                             @RequestParam("collegeCode") String collegeCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.checkEditCode(collegeId, collegeCode);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存院更改
     *
     * @param collegeEditVo 院
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/college/update")
    public ResponseEntity<Map<String, Object>> update(CollegeEditVo collegeEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.update(collegeEditVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.status(collegeIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据json
     *
     * @return json
     */
    @GetMapping("/web/data/college/application/json")
    public ResponseEntity<Map<String, Object>> applicationJson() {
        AjaxUtil<TreeViewData> ajaxUtil = AjaxUtil.of();
        List<TreeViewData> list = new ArrayList<>();
        Optional<List<TreeViewData>> optionalTreeViewData = collegeService.collegeApplicationJson();
        if(optionalTreeViewData.isPresent()){
            list = optionalTreeViewData.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 院与应用数据
     *
     * @param collegeId 院id
     * @return 数据
     */
    @PostMapping("/web/data/college/application/data")
    public ResponseEntity<Map<String, Object>> collegeApplicationData(@RequestParam("collegeId") int collegeId) {
        AjaxUtil<CollegeApplication> ajaxUtil = AjaxUtil.of();
        List<CollegeApplication> list = new ArrayList<>();
        Optional<List<CollegeApplication>> optionalCollegeApplications = collegeService.collegeApplicationData(collegeId);
        if(optionalCollegeApplications.isPresent()){
            list = optionalCollegeApplications.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新应用挂载
     *
     * @param collegeId      院id
     * @param applicationIds 应用ids
     * @return true 更新成功
     */
    @PostMapping("/web/data/college/mount")
    public ResponseEntity<Map<String, Object>> mount(@RequestParam("collegeId") int collegeId, String applicationIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = collegeService.collegeApplicationMount(collegeId, applicationIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
