package top.zbeboy.zone.web.internship.common;

import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.domain.tables.pojos.Department;
import top.zbeboy.zbase.domain.tables.pojos.Organize;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InternshipControllerCommon {

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private StaffService staffService;

    /**
     * 实习教职工数据
     *
     * @param id 实习发布id
     */
    public List<StaffBean> internshipReleaseStaffData(String id) {
        List<StaffBean> beans = new ArrayList<>();
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            Department department = record.get().into(Department.class);
            Optional<List<StaffBean>> optionalStaffBeans = staffService.findNormalByDepartmentIdRelation(department.getDepartmentId());
            if(optionalStaffBeans.isPresent()){
                beans = optionalStaffBeans.get();
            }
        }

        return beans;
    }

    /**
     * 实习申请班级数据
     *
     * @param id 实习发布id
     */
    public Select2Data internshipApplyOrganizeData(String id) {
        Select2Data select2Data = Select2Data.of();
        List<Organize> organizes = new ArrayList<>();
        Result<Record2<Integer, String>> records = internshipApplyService.findDistinctOrganize(id);
        if (records.isNotEmpty()) {
            organizes = records.into(Organize.class);
        }
        organizes.forEach(organize -> select2Data.add(organize.getOrganizeId().toString(), organize.getOrganizeName()));
        return select2Data;
    }
}
