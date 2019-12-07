package top.zbeboy.zone.web.system.log;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.system.SystemLogService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.system.log.SystemOperatorLogBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class SystemLogRestController {

    @Resource
    private SystemLogService systemLogService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/log/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("username");
        headers.add("behavior");
        headers.add("operatingTimeNew");
        headers.add("ipAddress");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = systemLogService.findAllByPage(dataTablesUtil);
        List<SystemOperatorLogBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SystemOperatorLogBean.class);
            beans.forEach(b -> b.setOperatingTimeNew(DateTimeUtil.defaultFormatSqlTimestamp(b.getOperatingTime())));
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(systemLogService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(systemLogService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
