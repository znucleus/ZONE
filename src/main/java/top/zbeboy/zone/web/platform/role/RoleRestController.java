package top.zbeboy.zone.web.platform.role;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.RoleApplication;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.tools.web.plugin.treeview.TreeViewData;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.role.RoleAddVo;
import top.zbeboy.zbase.vo.platform.role.RoleEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RoleRestController {

    @Resource
    private RoleService roleService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/role/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("roleEnName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        return new ResponseEntity<>(roleService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/role/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("roleName") String roleName,
                                                            int collegeId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = roleService.checkAddName(users.getUsername(), roleName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @param roleId    角色id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/role/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("roleName") String roleName,
                                                             int collegeId,
                                                             @RequestParam("roleId") String roleId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = roleService.checkEditName(users.getUsername(), roleName, collegeId, roleId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存角色
     *
     * @param roleAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/role/save")
    public ResponseEntity<Map<String, Object>> save(RoleAddVo roleAddVo) {
        Users users = SessionUtil.getUserFromSession();
        roleAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = roleService.save(roleAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param roleEditVo 数据
     * @return true 更新成功 false 更新失败
     */
    @PostMapping("/web/platform/role/update")
    public ResponseEntity<Map<String, Object>> update(RoleEditVo roleEditVo) {
        Users users = SessionUtil.getUserFromSession();
        roleEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = roleService.update(roleEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return true成功
     */
    @PostMapping("/web/platform/role/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("roleId") String roleId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = roleService.delete(users.getUsername(), roleId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据json
     *
     * @param collegeId 院id
     * @return json
     */
    @GetMapping("/web/platform/role/application/json")
    public ResponseEntity<Map<String, Object>> applicationJson(@RequestParam("collegeId") int collegeId) {
        AjaxUtil<TreeViewData> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(roleService.applicationJson(collegeId)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取角色id 下的 应用id
     *
     * @param roleId 角色id
     * @return 应用
     */
    @PostMapping("/web/platform/role/application/data")
    public ResponseEntity<Map<String, Object>> roleApplicationData(@RequestParam("roleId") String roleId) {
        AjaxUtil<RoleApplication> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(roleService.roleApplicationData(roleId)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
