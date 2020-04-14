package top.zbeboy.zone.api.register.leaver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.register.common.RegisterControllerCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterDataVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class RegisterLeaverApiController {

    @Resource
    private RegisterControllerCommon registerControllerCommon;

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/api/register/leaver/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal) {
        String channel = Workbook.channel.API.name();
        simplePaginationUtil.setChannel(channel);
        simplePaginationUtil.setPrincipal(principal);
        AjaxUtil<LeaverRegisterReleaseBean> ajaxUtil = registerControllerCommon.data(simplePaginationUtil, channel, principal);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param leaverRegisterReleaseAddVo 数据
     * @param bindingResult              检验
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo, BindingResult bindingResult, Principal principal) {
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerControllerCommon.save(leaverRegisterReleaseAddVo, bindingResult, channel, principal);
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
        LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
        if (Objects.nonNull(leaverRegisterRelease)) {
            List<LeaverRegisterScopeBean> leaverRegisterScopeBeans = registerControllerCommon.leaverRegisterScopes(leaverRegisterReleaseId, leaverRegisterRelease.getDataScope());
            ajaxUtil.success().list(leaverRegisterScopeBeans).msg("获取数据成功");
        } else {
            ajaxUtil.fail().msg("根据发布ID未查询到离校登记发布数据");
        }
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
        List<LeaverRegisterOption> leaverRegisterOptions = registerControllerCommon.leaverRegisterOptions(leaverRegisterReleaseId);
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
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerControllerCommon.optionDelete(leaverRegisterOptionId, leaverRegisterReleaseId, channel, principal);
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
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerControllerCommon.optionUpdate(leaverRegisterOptionId, leaverRegisterReleaseId, optionContent, channel, principal);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param leaverRegisterReleaseEditVo 数据
     * @param bindingResult               检验
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo,
                                                      BindingResult bindingResult, Principal principal) {
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerControllerCommon.update(leaverRegisterReleaseEditVo, bindingResult, channel, principal);
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
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerControllerCommon.delete(leaverRegisterReleaseId, channel, principal);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param leaverRegisterDataVo 数据
     * @param bindingResult        检验
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(@Valid LeaverRegisterDataVo leaverRegisterDataVo, BindingResult bindingResult,
                                                        Principal principal) {
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerControllerCommon.dataSave(leaverRegisterDataVo, bindingResult, channel, principal);
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
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil = registerControllerCommon.dataDelete(leaverRegisterReleaseId, channel, principal);
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
        String channel = Workbook.channel.API.name();
        AjaxUtil<Map<String, Object>> ajaxUtil =
                registerControllerCommon.dataListDelete(leaverRegisterReleaseId, leaverRegisterDataId, channel, principal);
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
        AjaxUtil<LeaverRegisterDataBean> ajaxUtil = registerControllerCommon.dataList(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 列表 数据
     *
     * @param request 请求
     */
    @GetMapping("/api/register/leaver/data/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        registerControllerCommon.export(request, response);
    }


}
