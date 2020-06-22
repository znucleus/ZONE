package top.zbeboy.zone.web.system.log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(systemLogService.data(dataTablesUtil), HttpStatus.OK);
    }
}
