package top.zbeboy.zone.web.data.science;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.data.ScienceService;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class ScienceViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private ScienceService scienceService;

    /**
     * 专业数据
     *
     * @return 专业数据页面
     */
    @GetMapping("/web/menu/data/science")
    public String index() {
        return "web/data/science/science_data::#page-wrapper";
    }

    /**
     * 专业数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/science/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }

                if (collegeId > 0) {
                    modelMap.addAttribute("collegeId", collegeId);
                    page = "web/data/science/science_add::#page-wrapper";
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
            page = "web/data/science/science_add::#page-wrapper";
        }
        return page;
    }

    /**
     * 专业数据编辑
     *
     * @param id       专业id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/data/science/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        ScienceBean scienceBean = scienceService.findByIdRelation(id);
        if (Objects.nonNull(scienceBean) && scienceBean.getScienceId() > 0) {
            modelMap.addAttribute("science", scienceBean);
            if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                modelMap.addAttribute("collegeId", scienceBean.getCollegeId());
            } else {
                modelMap.addAttribute("collegeId", 0);
            }

            page = "web/data/science/science_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到专业数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
