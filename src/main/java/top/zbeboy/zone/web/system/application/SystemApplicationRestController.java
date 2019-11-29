package top.zbeboy.zone.web.system.application;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.service.system.ApplicationService;
import top.zbeboy.zone.web.bean.system.application.ApplicationBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class SystemApplicationRestController {

    @Resource
    private ApplicationService applicationService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/application/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();
        headers.add("select");
        headers.add("applicationName");
        headers.add("applicationEnName");
        headers.add("applicationPid");
        headers.add("applicationUrl");
        headers.add("icon");
        headers.add("applicationSort");
        headers.add("applicationCode");
        headers.add("applicationDataUrlStartWith");
        headers.add("operator");

        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = applicationService.findAllByPage(dataTablesUtil);
        List<ApplicationBean> applicationBeen = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            applicationBeen = records.into(ApplicationBean.class);
            applicationBeen.forEach(a->{
                if(StringUtils.equals("0",a.getApplicationPid())){
                    a.setApplicationPidName("无");
                } else {
                    Application application = applicationService.findById(a.getApplicationPid());
                    a.setApplicationPidName(application.getApplicationName());
                }
            });
        }
        dataTablesUtil.setData(applicationBeen);
        dataTablesUtil.setiTotalRecords(applicationService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(applicationService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
