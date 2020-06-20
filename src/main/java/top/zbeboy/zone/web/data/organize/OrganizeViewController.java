package top.zbeboy.zone.web.data.organize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.data.OrganizeService;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.RoleService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class OrganizeViewController {

    @Resource
    private RoleService roleService;

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
        if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
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
        OrganizeBean organizeBean= organizeService.findByIdRelation(id);
        if (Objects.nonNull(organizeBean.getOrganizeId()) && organizeBean.getOrganizeId() > 0) {
            if (Objects.nonNull(organizeBean.getStaffId())) {
                StaffBean bean = staffService.findByIdRelation(organizeBean.getStaffId());
                if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                    modelMap.addAttribute("username", bean.getUsername());
                    modelMap.addAttribute("realName", bean.getRealName());
                    modelMap.addAttribute("mobile", bean.getMobile());
                }
            }
            modelMap.addAttribute("organize", organizeBean);
            if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
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
