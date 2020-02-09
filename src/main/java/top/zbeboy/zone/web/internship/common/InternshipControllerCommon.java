package top.zbeboy.zone.web.internship.common;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InternshipControllerCommon {

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private StaffService staffService;

    /**
     * 实习发布数据
     *
     * @param ajaxUtil             回调
     * @param simplePaginationUtil 工具
     */
    public void InternshipReleaseData(AjaxUtil<InternshipReleaseBean> ajaxUtil, SimplePaginationUtil simplePaginationUtil) {
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime())));
            beans.forEach(bean -> bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime())));
            beans.forEach(bean -> bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime())));
            beans.forEach(bean -> bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime())));
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.canOperator(bean.getInternshipReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
    }

    /**
     * 实习教职工数据
     *
     * @param id          实习发布id
     */
    public List<StaffBean> internshipReleaseStaffData(String id) {
        List<StaffBean> beans = new ArrayList<>();
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            Department department = record.get().into(Department.class);
            Result<Record> staffRecord = staffService.findNormalByDepartmentIdRelation(department.getDepartmentId());
            if (staffRecord.isNotEmpty()) {
                beans = staffRecord.into(StaffBean.class);
            }
        }

        return beans;
    }
}
