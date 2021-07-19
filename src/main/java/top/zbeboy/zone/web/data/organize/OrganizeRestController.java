package top.zbeboy.zone.web.data.organize;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Organize;
import top.zbeboy.zbase.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.OrganizeService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.organize.OrganizeAddVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeEditVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeSearchVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class OrganizeRestController {

    @Resource
    private OrganizeService organizeService;

    @Resource
    private SystemLogService systemLogService;

    /**
     * 获取年级下全部有效班级
     *
     * @param organizeSearchVo 查询参数
     * @return 班级数据
     */
    @ApiLoggingRecord(remark = "班级数据", channel = Workbook.channel.WEB)
    @GetMapping("/anyone/data/organize")
    public ResponseEntity<Map<String, Object>> anyoneData(OrganizeSearchVo organizeSearchVo, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        Optional<List<Organize>> organizes = organizeService.search(organizeSearchVo);
        organizes.ifPresent(organizeList -> organizeList.forEach(organize -> select2Data.add(organize.getOrganizeId().toString(), organize.getOrganizeName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/organize/paging")
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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(organizeService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验班级名是否重复
     *
     * @param organizeName 班级名
     * @param scienceId    专业id
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/organize/check-add-name")
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
    @GetMapping("/web/data/organize/check-staff")
    public ResponseEntity<Map<String, Object>> checkStaff(@RequestParam("staff") String staff) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.checkStaff(staff);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存班级信息
     *
     * @param organizeAddVo 班级
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/organize/save")
    public ResponseEntity<Map<String, Object>> save(OrganizeAddVo organizeAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.save(organizeAddVo);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "添加班级[" + organizeAddVo.getOrganizeName() + "]",
                DateTimeUtil.getNowLocalDateTime(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
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
    @PostMapping("/web/data/organize/check-edit-name")
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
    public ResponseEntity<Map<String, Object>> update(OrganizeEditVo organizeEditVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.update(organizeEditVo);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "更新班级" + organizeEditVo.getOrganizeId() + "[" + organizeEditVo.getOrganizeName() + "]",
                DateTimeUtil.getNowLocalDateTime(), users.getUsername(), RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
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
    public ResponseEntity<Map<String, Object>> status(String organizeIds, Byte isDel, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = organizeService.status(organizeIds, isDel);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "修改班级状态[" + organizeIds + "]" + isDel,
                DateTimeUtil.getNowLocalDateTime(), users.getUsername(), RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
