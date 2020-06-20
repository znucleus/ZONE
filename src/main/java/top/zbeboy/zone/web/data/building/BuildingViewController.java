package top.zbeboy.zone.web.data.building;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.data.BuildingService;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class BuildingViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private BuildingService buildingService;

    /**
     * 楼数据
     *
     * @return 楼数据页面
     */
    @GetMapping("/web/menu/data/building")
    public String index() {
        return "web/data/building/building_data::#page-wrapper";
    }

    /**
     * 楼数据添加
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/data/building/add")
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
                    page = "web/data/building/building_add::#page-wrapper";
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
            page = "web/data/building/building_add::#page-wrapper";
        }
        return page;
    }


    /**
     * 楼数据编辑
     *
     * @param id       系id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/data/building/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        BuildingBean buildingBean = buildingService.findByIdRelation(id);
        if (Objects.nonNull(buildingBean.getBuildingId()) && buildingBean.getBuildingId() > 0) {
            modelMap.addAttribute("building", buildingBean);
            if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                modelMap.addAttribute("collegeId", buildingBean.getCollegeId());
            } else {
                modelMap.addAttribute("collegeId", 0);
            }

            page = "web/data/building/building_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到楼数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
