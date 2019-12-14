package top.zbeboy.zone.web.platform.role;

import com.alibaba.fastjson.JSONObject;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.platform.CollegeRoleService;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RoleRestController {

    @Resource
    private CollegeRoleService collegeRoleService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/role/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("roleEnName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        JSONObject otherCondition = dataTablesUtil.getSearch();
        otherCondition.put("roleType", 2);
        Result<Record> records = collegeRoleService.findAllByPage(dataTablesUtil);
        List<RoleBean> roleBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            roleBean = records.into(RoleBean.class);
        }
        dataTablesUtil.setData(roleBean);
        dataTablesUtil.setiTotalRecords(collegeRoleService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(collegeRoleService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
