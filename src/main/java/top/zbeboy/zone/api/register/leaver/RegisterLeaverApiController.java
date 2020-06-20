package top.zbeboy.zone.api.register.leaver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.register.RegisterLeaverService;
import top.zbeboy.zone.service.export.LeaverRegisterDataExport;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.ExportInfo;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterDataVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class RegisterLeaverApiController {
    @Resource
    private RegisterLeaverService registerLeaverService;

    @Resource
    private UploadService uploadService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/api/register/leaver/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<LeaverRegisterReleaseBean> ajaxUtil = registerLeaverService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param leaverRegisterReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/save")
    public ResponseEntity<Map<String, Object>> save(LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        leaverRegisterReleaseAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = registerLeaverService.save(leaverRegisterReleaseAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取域数据
     *
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/release/scopes")
    public ResponseEntity<Map<String, Object>> scopes(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId) {
        AjaxUtil<LeaverRegisterScopeBean> ajaxUtil = AjaxUtil.of();
        List<LeaverRegisterScopeBean> beans = registerLeaverService.leaverRegisterScopes(leaverRegisterReleaseId);
        ajaxUtil.success().list(beans).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取选项数据
     *
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/release/options")
    public ResponseEntity<Map<String, Object>> options(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId) {
        AjaxUtil<LeaverRegisterOption> ajaxUtil = AjaxUtil.of();
        List<LeaverRegisterOption> leaverRegisterOptions = registerLeaverService.leaverRegisterOptions(leaverRegisterReleaseId);
        ajaxUtil.success().list(leaverRegisterOptions).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 选项删除
     *
     * @param leaverRegisterOptionId 选项id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/option/delete")
    public ResponseEntity<Map<String, Object>> optionDelete(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId,
                                                            @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                            Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = registerLeaverService.optionDelete(leaverRegisterOptionId, leaverRegisterReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 选项内容更新
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @param optionContent           选项内容
     * @return true or false
     */
    @PostMapping("/api/register/leaver/option/update")
    public ResponseEntity<Map<String, Object>> optionUpdate(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId,
                                                            @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                            @RequestParam("optionContent") String optionContent,
                                                            Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerLeaverService.optionUpdate(leaverRegisterOptionId, leaverRegisterReleaseId, optionContent, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param leaverRegisterReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/update")
    public ResponseEntity<Map<String, Object>> update(LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo,
                                                      Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        leaverRegisterReleaseEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerLeaverService.update(leaverRegisterReleaseEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                      Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = registerLeaverService.delete(leaverRegisterReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param leaverRegisterDataVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(LeaverRegisterDataVo leaverRegisterDataVo,
                                                        Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        leaverRegisterDataVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerLeaverService.dataSave(leaverRegisterDataVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/delete")
    public ResponseEntity<Map<String, Object>> dataDelete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                          Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = registerLeaverService.dataDelete(leaverRegisterReleaseId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除登记
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/list/delete")
    public ResponseEntity<Map<String, Object>> dataListDelete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                              @RequestParam("leaverRegisterDataId") String leaverRegisterDataId,
                                                              Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerLeaverService.dataListDelete(leaverRegisterReleaseId, leaverRegisterDataId, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/api/register/leaver/data/list")
    public ResponseEntity<Map<String, Object>> dataList(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<LeaverRegisterDataBean> ajaxUtil = registerLeaverService.dataList(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 列表 数据
     *
     * @param request 请求
     */
    @GetMapping("/api/register/leaver/data/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SimplePaginationUtil simplePaginationUtil = new SimplePaginationUtil(request, "studentNumber", "asc",
                "离校登记数据表", Workbook.registerFilePath());
        List<LeaverRegisterDataBean> beans = registerLeaverService.export(simplePaginationUtil);

        LeaverRegisterDataExport export = new LeaverRegisterDataExport(beans);
        ExportInfo exportInfo = simplePaginationUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }


}
