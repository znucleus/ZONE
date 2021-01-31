package top.zbeboy.zone.web.register.epidemic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.register.epidemic.EpidemicRegisterDataBean;
import top.zbeboy.zbase.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.register.RegisterEpidemicService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ExportInfo;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterDataAddVo;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterReleaseAddVo;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterReleaseEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.export.EpidemicRegisterDataExport;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RegisterEpidemicRestController {

    @Resource
    private UploadService uploadService;

    @Resource
    private RegisterEpidemicService registerEpidemicService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "疫情登记数据", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/register/epidemic/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<EpidemicRegisterReleaseBean> ajaxUtil = registerEpidemicService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param epidemicRegisterReleaseAddVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "疫情登记发布", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/register/epidemic/release/save")
    public ResponseEntity<Map<String, Object>> save(EpidemicRegisterReleaseAddVo epidemicRegisterReleaseAddVo, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        epidemicRegisterReleaseAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.save(epidemicRegisterReleaseAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param epidemicRegisterReleaseEditVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "疫情登记发布编辑", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/register/epidemic/release/update")
    public ResponseEntity<Map<String, Object>> update(EpidemicRegisterReleaseEditVo epidemicRegisterReleaseEditVo, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        epidemicRegisterReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.update(epidemicRegisterReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param epidemicRegisterReleaseId id
     * @return true or false
     */
    @ApiLoggingRecord(remark = "疫情登记发布删除", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/register/epidemic/release/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("epidemicRegisterReleaseId") String epidemicRegisterReleaseId, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.delete(users.getUsername(), epidemicRegisterReleaseId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 登记
     *
     * @param epidemicRegisterDataAddVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "疫情登记登记", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/register/epidemic/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(EpidemicRegisterDataAddVo epidemicRegisterDataAddVo, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        epidemicRegisterDataAddVo.setUsername(users.getUsername());
        epidemicRegisterDataAddVo.setChannelName(Workbook.channel.WEB.name());
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.dataSave(epidemicRegisterDataAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "疫情登记列表", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/register/epidemic/data/list")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("registerRealName");
        headers.add("registerUsername");
        headers.add("registerType");
        headers.add("epidemicStatus");
        headers.add("address");
        headers.add("institute");
        headers.add("channelName");
        headers.add("remark");
        headers.add("registerDateStr");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(registerEpidemicService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 登记删除
     *
     * @param epidemicRegisterDataId 发布id
     * @return true or false
     */
    @ApiLoggingRecord(remark = "疫情登记删除", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/register/epidemic/data/delete")
    public ResponseEntity<Map<String, Object>> dataDelete(@RequestParam("epidemicRegisterDataId") String epidemicRegisterDataId, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.dataDelete(users.getUsername(), epidemicRegisterDataId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 列表 数据
     *
     * @param request 请求
     */
    @ApiLoggingRecord(remark = "疫情登记导出", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/register/epidemic/data/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "registerDateStr", "desc",
                "疫情登记数据表", Workbook.registerFilePath());
        Optional<List<EpidemicRegisterDataBean>> optionalEpidemicRegisterDataBeans = registerEpidemicService.export(dataTablesUtil);
        List<EpidemicRegisterDataBean> beans;
        beans = optionalEpidemicRegisterDataBeans.orElseGet(ArrayList::new);
        EpidemicRegisterDataExport export = new EpidemicRegisterDataExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }
}
