package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class AuthorizeRestController {

    @Resource
    private RoleApplyService roleApplyService;

    @Resource
    private AuthorizeTypeService authorizeTypeService;

    @Resource
    private CollegeRoleService collegeRoleService;

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
    @GetMapping("/web/platform/authorize/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("realName");
        headers.add("username");
        headers.add("authorizeTypeName");
        headers.add("organizeName");
        headers.add("roleName");
        headers.add("duration");
        headers.add("validDate");
        headers.add("expireDate");
        headers.add("applyStatus");
        headers.add("createDate");
        headers.add("reason");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = roleApplyService.findAllByPage(dataTablesUtil);
        List<RoleApplyBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(RoleApplyBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(roleApplyService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(roleApplyService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 根据全部权限类型
     *
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/type")
    public ResponseEntity<Map<String, Object>> authorizeTypeData() {
        Select2Data select2Data = Select2Data.of();
        List<AuthorizeType> all = authorizeTypeService.findAll();
        all.forEach(authorizeType -> select2Data.add(authorizeType.getAuthorizeTypeId().toString(), authorizeType.getAuthorizeTypeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 根据全部权限类型
     *
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/role")
    public ResponseEntity<Map<String, Object>> roleData() {
        Select2Data select2Data = Select2Data.of();
        int collegeId = 0;
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

        if (collegeId > 0) {
            Result<Record> all = collegeRoleService.findByCollegeIdRelation(collegeId);
            if (all.isNotEmpty()) {
                List<Role> roles = all.into(Role.class);
                roles.forEach(role -> select2Data.add(role.getRoleId(), role.getRoleName()));
            }
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }


}
