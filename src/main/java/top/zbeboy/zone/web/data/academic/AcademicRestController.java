package top.zbeboy.zone.web.data.academic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.feign.data.AcademicTitleService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.academic.AcademicAddVo;
import top.zbeboy.zbase.vo.data.academic.AcademicEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AcademicRestController {

    @Resource
    private AcademicTitleService academicTitleService;

    /**
     * 获取全部职称
     *
     * @return 职称数据
     */
    @GetMapping("/anyone/data/academic")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<AcademicTitle> academicTitles = academicTitleService.findAll();
        academicTitles.forEach(academicTitle -> select2Data.add(academicTitle.getAcademicTitleId().toString(), academicTitle.getAcademicTitleName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/academic/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("academicTitleId");
        headers.add("academicTitleName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        return new ResponseEntity<>(academicTitleService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验职称是否重复
     *
     * @param academicTitleName 职称
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/academic/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("academicTitleName") String academicTitleName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = academicTitleService.checkAddName(academicTitleName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存职称信息
     *
     * @param academicAddVo 职称
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/academic/save")
    public ResponseEntity<Map<String, Object>> save(AcademicAddVo academicAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = academicTitleService.save(academicAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时职称名重复
     *
     * @param academicTitleId   职称id
     * @param academicTitleName 职称名
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/academic/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("academicTitleId") int academicTitleId,
                                                             @RequestParam("academicTitleName") String academicTitleName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = academicTitleService.checkEditName(academicTitleId, academicTitleName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param academicEditVo 职称
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/academic/update")
    public ResponseEntity<Map<String, Object>> update(AcademicEditVo academicEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = academicTitleService.update(academicEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
