package top.zbeboy.zone.web.system.mailbox;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.feign.system.SystemMailboxLogService;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        return new ResponseEntity<>(systemMailboxLogService.data(dataTablesUtil), HttpStatus.OK);
    }
}
