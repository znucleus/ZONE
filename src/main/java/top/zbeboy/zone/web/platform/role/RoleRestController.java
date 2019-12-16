package top.zbeboy.zone.web.platform.role;

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
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.CollegeApplicationService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class RoleRestController {

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private CollegeApplicationService collegeApplicationService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

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
        JSONObject otherCondition = dataTablesUtil.getSearch();
        otherCondition.put("roleType", 2);
        Result<Record> records = collegeRoleService.findAllByPage(dataTablesUtil);
        List<RoleBean> roleBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            roleBean = records.into(RoleBean.class);
        }
        dataTablesUtil.setData(roleBean);
        dataTablesUtil.setiTotalRecords(collegeRoleService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(collegeRoleService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/role/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("roleName") String roleName, int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(roleName);

        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    collegeId = record.get().into(College.class).getCollegeId();
                }
            }
        }

        if(collegeId > 0){
            Result<Record> records = collegeRoleService.findByRoleNameAndCollegeId(param, collegeId);

            if(records.isEmpty()){
                ajaxUtil.success().msg("角色名不重复");
            } else {
                ajaxUtil.fail().msg("角色名重复");
            }
        } else {
            ajaxUtil.fail().msg("未查询到用户院ID或未选择院");
        }

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
        ajaxUtil.success().list(toJson("0", collegeId)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据转换为json
     *
     * @param id 父id
     * @return json 数据
     */
    private List<TreeViewData> toJson(String id, int collegeId) {
        List<Application> applications = collegeApplicationService.findByPidAndCollegeId(id, collegeId);
        List<TreeViewData> trees = new ArrayList<>();
        if (Objects.nonNull(applications)) {
            applications.forEach(a -> trees.add(new TreeViewData(a.getApplicationName(), toJson(a.getApplicationId(), collegeId), a.getApplicationId())));
        }
        return trees;
    }
}
