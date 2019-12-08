package top.zbeboy.zone.web.system.mailbox;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.system.SystemMailboxLogService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.system.mailbox.SystemMailboxLogBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class SystemMailboxLogRestController {

    @Resource
    private SystemMailboxLogService systemMailboxLogService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/mailbox/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("acceptMail");
        headers.add("sendTimeNew");
        headers.add("sendCondition");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = systemMailboxLogService.findAllByPage(dataTablesUtil);
        List<SystemMailboxLogBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SystemMailboxLogBean.class);
            beans.forEach(b -> b.setSendTimeNew(DateTimeUtil.defaultFormatSqlTimestamp(b.getSendTime())));
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(systemMailboxLogService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(systemMailboxLogService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
