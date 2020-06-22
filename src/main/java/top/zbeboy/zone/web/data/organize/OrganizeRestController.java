package top.zbeboy.zone.web.data.organize;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Organize;
import top.zbeboy.zone.feign.data.OrganizeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.organize.OrganizeAddVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeEditVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class OrganizeRestController {

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
        List<Organize> organizes = organizeService.findByGradeIdAndOrganizeIsDel(organizeSearchVo);
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
        return new ResponseEntity<>(organizeService.data(dataTablesUtil), HttpStatus.OK);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.checkAddName(organizeName, scienceId);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.checkAddStaff(staff);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存班级信息
     *
     * @param organizeAddVo 班级
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/organize/save")
    public ResponseEntity<Map<String, Object>> save(OrganizeAddVo organizeAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.save(organizeAddVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.checkEditName(organizeId, organizeName, scienceId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新班级信息
     *
     * @param organizeEditVo 班级
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/organize/update")
    public ResponseEntity<Map<String, Object>> update(OrganizeEditVo organizeEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.update(organizeEditVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.status(organizeIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
