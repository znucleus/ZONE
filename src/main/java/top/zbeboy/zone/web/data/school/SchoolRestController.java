package top.zbeboy.zone.web.data.school;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.service.data.SchoolService;
import top.zbeboy.zone.web.bean.data.school.SchoolBean;
import top.zbeboy.zone.web.bean.system.application.ApplicationBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SchoolRestController {

    @Resource
    private SchoolService schoolService;

    /**
     * 获取全部有效学校
     *
     * @return 学校数据
     */
    @GetMapping("/anyone/data/school")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<School> schools = schoolService.findBySchoolIsDel(BooleanUtil.toByte(false));
        schools.forEach(school -> select2Data.add(school.getSchoolId().toString(), school.getSchoolName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/school/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("schoolId");
        headers.add("schoolName");
        headers.add("schoolIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = schoolService.findAllByPage(dataTablesUtil);
        List<SchoolBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SchoolBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(schoolService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(schoolService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
