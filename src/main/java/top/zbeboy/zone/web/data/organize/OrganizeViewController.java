package top.zbeboy.zone.web.data.organize;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.OrganizeService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class OrganizeViewController {

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

    @Resource
    private OrganizeService organizeService;

    /**
     * 班级数据
     *
     * @return 班级数据页面
     */
    @GetMapping("/web/menu/data/organize")
    public String index() {
        return "web/data/organize/organize_data::#page-wrapper";
    }

    /**
     * 班级数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/organize/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                    if (record.isPresent()) {
                        College college = record.get().into(College.class);
                        collegeId = college.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                    if (record.isPresent()) {
                        College college = record.get().into(College.class);
                        collegeId = college.getCollegeId();
                    }
                }

                if (collegeId > 0) {
                    modelMap.addAttribute("collegeId", collegeId);
                    page = "web/data/organize/organize_add::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到院信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            modelMap.addAttribute("collegeId", 0);
            page = "web/data/organize/organize_add::#page-wrapper";
        }
        return page;
    }

    /**
     * 班级数据编辑
     *
     * @param id       班级id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/data/organize/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = organizeService.findByIdRelation(id);
        if (record.isPresent()) {
            OrganizeBean organizeBean = record.get().into(OrganizeBean.class);
            if (Objects.nonNull(organizeBean.getStaffId())) {
                Optional<Record> staffRecord = staffService.findByIdRelation(organizeBean.getStaffId());
                if (staffRecord.isPresent()) {
                    StaffBean staffBean = staffRecord.get().into(StaffBean.class);
                    modelMap.addAttribute("username", staffBean.getUsername());
                    modelMap.addAttribute("realName", staffBean.getRealName());
                    modelMap.addAttribute("mobile", staffBean.getMobile());
                }
            }
            modelMap.addAttribute("organize", organizeBean);
            if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                modelMap.addAttribute("collegeId", organizeBean.getCollegeId());
            } else {
                modelMap.addAttribute("collegeId", 0);
            }
            page = "web/data/organize/organize_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到班级数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
