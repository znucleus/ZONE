package top.zbeboy.zone.web.system.role;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.domain.tables.records.RoleApplicationRecord;
import top.zbeboy.zone.service.platform.RoleApplicationService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.system.ApplicationService;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SystemRoleRestController {

    @Resource
    private RoleService roleService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private RoleApplicationService roleApplicationService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/role/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("roleName");
        headers.add("roleEnName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        JSONObject otherCondition = dataTablesUtil.getSearch();
        otherCondition.put("roleType", 1);
        Result<Record> records = roleService.findAllByPage(dataTablesUtil);
        List<RoleBean> roleBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            roleBean = records.into(RoleBean.class);
        }
        dataTablesUtil.setData(roleBean);
        dataTablesUtil.setiTotalRecords(roleService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(roleService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 更新时检验角色名
     *
     * @param roleName 角色名
     * @param roleId   角色id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/system/role/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkName(@RequestParam("roleName") String roleName, @RequestParam("roleId") String roleId) {
        String param = StringUtils.deleteWhitespace(roleName);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(param)) {
            Result<Record> records = roleService.findByRoleNameAndRoleTypeNeRoleId(param, 1, roleId);
            if (records.isEmpty()) {
                ajaxUtil.success().msg("角色名不重复");
            } else {
                ajaxUtil.fail().msg("角色名重复");
            }
        }
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
                                                          @RequestParam("roleName") String roleName, String applicationIds) {
        AjaxUtil<RoleApplication> ajaxUtil = AjaxUtil.of();
        Role role = roleService.findById(roleId);
        if (Objects.nonNull(role)) {
            role.setRoleName(roleName);
            roleService.update(role);
            roleApplicationService.deleteByRoleId(roleId);
            if (StringUtils.isNotBlank(applicationIds)) {
                List<RoleApplication> roleApplications = new ArrayList<>();
                List<String> ids = SmallPropsUtil.StringIdsToStringList(applicationIds);
                ids.forEach(id -> roleApplications.add(new RoleApplication(roleId, id)));
                roleApplicationService.batchSave(roleApplications);
            }
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg("未查询到角色信息，更新失败");
        }
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
        Result<RoleApplicationRecord> roleApplicationRecords = roleApplicationService.findByRoleId(roleId);
        List<RoleApplication> roleApplications = new ArrayList<>();
        if (roleApplicationRecords.isNotEmpty()) {
            roleApplications = roleApplicationRecords.into(RoleApplication.class);
        }
        ajaxUtil.success().list(roleApplications).msg("获取数据成功");
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
        ajaxUtil.success().list(toJson("0")).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据转换为json
     *
     * @param id 父id
     * @return json 数据
     */
    private List<TreeViewData> toJson(String id) {
        List<Application> applications = applicationService.findByPid(id);
        List<TreeViewData> trees = new ArrayList<>();
        if (Objects.nonNull(applications)) {
            applications.forEach(a -> trees.add(new TreeViewData(a.getApplicationName(), toJson(a.getApplicationId()), a.getApplicationId())));
        }
        return trees;
    }
}
