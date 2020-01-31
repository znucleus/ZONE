package top.zbeboy.zone.web.platform.app;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.platform.OauthClientUsersService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class AppRestController {

    @Resource
    private OauthClientUsersService oauthClientUsersService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/app/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("appName");
        headers.add("username");
        headers.add("realName");
        headers.add("clientId");
        headers.add("secret");
        headers.add("webServerRedirectUri");
        headers.add("remark");
        headers.add("createDate");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = oauthClientUsersService.findAllByPage(dataTablesUtil);
        List<OauthClientUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(OauthClientUsersBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(oauthClientUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(oauthClientUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
