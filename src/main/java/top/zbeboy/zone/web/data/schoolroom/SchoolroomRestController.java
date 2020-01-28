package top.zbeboy.zone.web.data.schoolroom;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.service.data.SchoolroomService;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zone.web.bean.data.schoolroom.SchoolroomBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SchoolroomRestController {

    @Resource
    private SchoolroomService schoolroomService;

    /**
     * 获取楼下全部有效教室
     *
     * @param schoolroomSearchVo 查询参数
     * @return 教室数据
     */
    @GetMapping("/users/data/schoolroom")
    public ResponseEntity<Map<String, Object>> usersData(SchoolroomSearchVo schoolroomSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<SchoolroomRecord> schoolrooms = schoolroomService.findByBuildingIdAndSchoolroomIsDel(schoolroomSearchVo.getBuildingId(), BooleanUtil.toByte(false));
        schoolrooms.forEach(schoolroom -> select2Data.add(schoolroom.getSchoolroomId().toString(), schoolroom.getBuildingCode()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/schoolroom/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("schoolroomId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("buildingName");
        headers.add("buildingCode");
        headers.add("schoolroomIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = schoolroomService.findAllByPage(dataTablesUtil);
        List<SchoolroomBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SchoolroomBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(schoolroomService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(schoolroomService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
