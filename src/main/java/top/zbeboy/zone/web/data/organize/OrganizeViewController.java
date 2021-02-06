package top.zbeboy.zone.web.data.organize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.organize.OrganizeBean;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.OrganizeService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class OrganizeViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private RoleService roleService;

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
        Users users = SessionUtil.getUserFromSession();
        if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (optionalStaffBean.isPresent()) {
                        collegeId = optionalStaffBean.get().getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        collegeId = optionalStudentBean.get().getCollegeId();
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
        Optional<OrganizeBean> optionalOrganizeBean = organizeService.findByIdRelation(id);
        if (optionalOrganizeBean.isPresent()) {
            OrganizeBean organizeBean = optionalOrganizeBean.get();
            if (Objects.nonNull(organizeBean.getStaffId())) {
                Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(organizeBean.getStaffId());
                if (optionalStaffBean.isPresent()) {
                    StaffBean bean = optionalStaffBean.get();
                    modelMap.addAttribute("username", bean.getUsername());
                    modelMap.addAttribute("realName", bean.getRealName());
                    modelMap.addAttribute("mobile", bean.getMobile());
                }
            }
            modelMap.addAttribute("organize", organizeBean);
            Users users = SessionUtil.getUserFromSession();
            if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
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
