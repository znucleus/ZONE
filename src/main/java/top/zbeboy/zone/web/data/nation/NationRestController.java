package top.zbeboy.zone.web.data.nation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Nation;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.NationService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.nation.NationAddVo;
import top.zbeboy.zbase.vo.data.nation.NationEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class NationRestController {

    @Resource
    private NationService nationService;

    /**
     * 获取全部民族
     *
     * @return 民族数据
     */
    @GetMapping("/anyone/data/nation")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<Nation> nations = nationService.findAll();
        nations.forEach(nation -> select2Data.add(nation.getNationId().toString(), nation.getNationName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/nation/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("nationId");
        headers.add("nationName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(nationService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验民族是否重复
     *
     * @param nationName 民族
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/nation/check-add-name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("nationName") String nationName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = nationService.checkAddName(nationName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存民族信息
     *
     * @param nationAddVo 民族
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/nation/save")
    public ResponseEntity<Map<String, Object>> save(NationAddVo nationAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = nationService.save(nationAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时民族名重复
     *
     * @param nationId   民族id
     * @param nationName 民族名
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/nation/check-edit-name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("nationId") int nationId,
                                                             @RequestParam("nationName") String nationName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = nationService.checkEditName(nationId, nationName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param nationEditVo 民族
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/nation/update")
    public ResponseEntity<Map<String, Object>> update(NationEditVo nationEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = nationService.update(nationEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
