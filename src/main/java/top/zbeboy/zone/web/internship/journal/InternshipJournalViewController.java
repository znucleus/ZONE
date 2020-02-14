package top.zbeboy.zone.web.internship.journal;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class InternshipJournalViewController {

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private RoleService roleService;

    /**
     * 实习日志
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/menu/internship/journal")
    public String index() {
        return "web/internship/journal/internship_journal::#page-wrapper";
    }

    /**
     * 日志列表
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/internship/journal/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("internshipReleaseId",id);
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }

        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            modelMap.addAttribute("usersTypeName", usersType.getUsersTypeName());

            if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    Staff staff = record.get().into(Staff.class);
                    modelMap.addAttribute("staffId", staff.getStaffId());
                }
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    Student student = record.get().into(Student.class);
                    modelMap.addAttribute("studentId", student.getStudentId());
                }
            }
        }
        return "web/internship/journal/internship_journal_list::#page-wrapper";
    }
}
