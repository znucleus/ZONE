package top.zbeboy.zone.web.system.role;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.RoleApplication;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.SystemRoleService;
import top.zbeboy.zbase.tools.web.plugin.treeview.TreeViewData;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SystemRoleRestController {

    @Resource
    private SystemRoleService systemRoleService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/role/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("roleName");
        headers.add("roleEnName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(systemRoleService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 更新时检验角色名
     *
     * @param roleName 角色名
     * @param roleId   角色id
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/system/role/check-edit-name")
    public ResponseEntity<Map<String, Object>> checkName(@RequestParam("roleName") String roleName, @RequestParam("roleId") String roleId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = systemRoleService.checkName(roleName, roleId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新角色
     *
     * @param roleId         角色id
     * @param roleName       角色名
     * @param applicationIds 应用ids
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/role/update")
    public ResponseEntity<Map<String, Object>> roleUpdate(@RequestParam("roleId") String roleId,
                                                          @RequestParam("roleName") String roleName,
                                                          String applicationIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = systemRoleService.roleUpdate(roleId, roleName, applicationIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取角色id 下的 应用id
     *
     * @param roleId 角色id
     * @return 应用
     */
    @PostMapping("/web/system/role/application/data")
    public ResponseEntity<Map<String, Object>> roleApplicationData(@RequestParam("roleId") String roleId) {
        AjaxUtil<RoleApplication> ajaxUtil = AjaxUtil.of();
        List<RoleApplication> list = new ArrayList<>();
        Optional<List<RoleApplication>> optionalRoleApplications = systemRoleService.roleApplicationData(roleId);
        if (optionalRoleApplications.isPresent()) {
            list = optionalRoleApplications.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据json
     *
     * @return json
     */
    @GetMapping("/web/system/role/application/json")
    public ResponseEntity<Map<String, Object>> applicationJson() {
        AjaxUtil<TreeViewData> ajaxUtil = AjaxUtil.of();
        List<TreeViewData> list = new ArrayList<>();
        Optional<List<TreeViewData>> optionalTreeViewData = systemRoleService.applicationJson();
        if (optionalTreeViewData.isPresent()) {
            list = optionalTreeViewData.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
