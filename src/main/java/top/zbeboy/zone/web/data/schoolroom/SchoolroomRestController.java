package top.zbeboy.zone.web.data.schoolroom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Schoolroom;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.SchoolroomService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomAddVo;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomEditVo;
import top.zbeboy.zbase.vo.data.schoolroom.SchoolroomSearchVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Schoolroom> schoolrooms = schoolroomService.findByBuildingIdAndSchoolroomIsDel(schoolroomSearchVo);
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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(schoolroomService.data(dataTablesUtil), HttpStatus.OK);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolroomService.checkAddCode(buildingCode, buildingId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存教室信息
     *
     * @param schoolroomAddVo 教室
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/schoolroom/save")
    public ResponseEntity<Map<String, Object>> save(SchoolroomAddVo schoolroomAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolroomService.save(schoolroomAddVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolroomService.checkEditCode(schoolroomId, buildingCode, buildingId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存教室更改
     *
     * @param schoolroomEditVo 教室
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/schoolroom/update")
    public ResponseEntity<Map<String, Object>> update(SchoolroomEditVo schoolroomEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolroomService.update(schoolroomEditVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolroomService.status(schoolroomIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
