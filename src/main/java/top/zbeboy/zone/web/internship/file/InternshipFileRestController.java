package top.zbeboy.zone.web.internship.file;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.internship.release.InternshipFileBean;
import top.zbeboy.zone.service.internship.InternshipFileService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class InternshipFileRestController {

    @Resource
    private InternshipFileService internshipFileService;

    /**
     * 获取实习文件
     *
     * @return 实习文件
     */
    @GetMapping("/users/data/internship_file/{id}")
    public ResponseEntity<Map<String, Object>> usersData(@PathVariable("id") String id) {
        AjaxUtil<InternshipFileBean> ajaxUtil = AjaxUtil.of();
        List<InternshipFileBean> beans = new ArrayList<>();
        Result<Record> records = internshipFileService.findByInternshipReleaseId(id);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipFileBean.class);
        }
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
