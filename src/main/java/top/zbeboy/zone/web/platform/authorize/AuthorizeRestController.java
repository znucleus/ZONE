package top.zbeboy.zone.web.platform.authorize;

import com.alibaba.fastjson.JSONObject;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.platform.RoleApplyService;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class AuthorizeRestController {

    @Resource
    private RoleApplyService roleApplyService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("realName");
        headers.add("username");
        headers.add("authorizeTypeName");
        headers.add("organizeName");
        headers.add("roleName");
        headers.add("duration");
        headers.add("validDate");
        headers.add("expireDate");
        headers.add("applyStatus");
        headers.add("createDate");
        headers.add("reason");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = roleApplyService.findAllByPage(dataTablesUtil);
        List<RoleApplyBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(RoleApplyBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(roleApplyService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(roleApplyService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
