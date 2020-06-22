package top.zbeboy.zone.web.data.science;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Science;
import top.zbeboy.zone.feign.data.ScienceService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.science.ScienceAddVo;
import top.zbeboy.zbase.vo.data.science.ScienceEditVo;
import top.zbeboy.zbase.vo.data.science.ScienceSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ScienceRestController {

    @Resource
    private ScienceService scienceService;

    /**
     * 获取系下全部有效专业
     *
     * @param scienceSearchVo 查询参数
     * @return 专业数据
     */
    @GetMapping("/anyone/data/science")
    public ResponseEntity<Map<String, Object>> anyoneData(ScienceSearchVo scienceSearchVo) {
        Select2Data select2Data = Select2Data.of();
        List<Science> sciences = scienceService.findByDepartmentIdAndScienceIsDel(scienceSearchVo);
        sciences.forEach(science -> select2Data.add(science.getScienceId().toString(), science.getScienceName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/science/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("scienceId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("scienceName");
        headers.add("scienceCode");
        headers.add("scienceIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        return new ResponseEntity<>(scienceService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验专业名是否重复
     *
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("scienceName") String scienceName,
                                                            @RequestParam("departmentId") int departmentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.checkAddName(scienceName, departmentId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存时检验专业代码是否重复
     *
     * @param scienceCode 专业代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/add/code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("scienceCode") String scienceCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.checkAddCode(scienceCode);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存专业信息
     *
     * @param scienceAddVo 专业
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/science/save")
    public ResponseEntity<Map<String, Object>> save(ScienceAddVo scienceAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.save(scienceAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时专业名重复
     *
     * @param scienceId    专业id
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("scienceId") int scienceId,
                                                             @RequestParam("scienceName") String scienceName,
                                                             @RequestParam("departmentId") int departmentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.checkEditName(scienceId, scienceName, departmentId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时专业代码重复
     *
     * @param scienceId   专业id
     * @param scienceCode 专业代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/edit/code")
    public ResponseEntity<Map<String, Object>> checkEditCode(@RequestParam("scienceId") int scienceId, @RequestParam("scienceCode") String scienceCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.checkEditCode(scienceId, scienceCode);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存专业更改
     *
     * @param scienceEditVo 专业
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/science/update")
    public ResponseEntity<Map<String, Object>> update(ScienceEditVo scienceEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.update(scienceEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改专业状态
     *
     * @param scienceIds 专业ids
     * @param isDel      is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/science/status")
    public ResponseEntity<Map<String, Object>> status(String scienceIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = scienceService.status(scienceIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
