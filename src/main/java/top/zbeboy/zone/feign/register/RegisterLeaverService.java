package top.zbeboy.zone.feign.register;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.hystrix.register.RegisterLeaverHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterDataVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "api-server", fallback = RegisterLeaverHystrixClientFallbackFactory.class)
public interface RegisterLeaverService {

    /**
     * 查询发布数据
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/api/register/leaver/release/{id}")
    LeaverRegisterRelease release(@PathVariable("id") String id);

    /**
     * 检查是否可查看
     *
     * @param username                用户
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/condition/leaver_review/{username}/{leaverRegisterReleaseId}")
    Boolean leaverReview(@PathVariable("username") String username, @PathVariable("leaverRegisterReleaseId") String leaverRegisterReleaseId);

    /**
     * 检查登记条件
     *
     * @param username                用户
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/condition/leaver_register/{username}/{leaverRegisterReleaseId}")
    Boolean leaverRegister(@PathVariable("username") String username, @PathVariable("leaverRegisterReleaseId") String leaverRegisterReleaseId);

    /**
     * 检查是否可操作
     *
     * @param username                用户
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/condition/leaver_operator/{username}/{leaverRegisterReleaseId}")
    Boolean leaverOperator(@PathVariable("username") String username, @PathVariable("leaverRegisterReleaseId") String leaverRegisterReleaseId);

    /**
     * 发布列表数据
     *
     * @param simplePaginationUtil 工具
     * @return 数据
     */
    @PostMapping("/api/register/leaver/data")
    AjaxUtil<LeaverRegisterReleaseBean> data(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 发布保存
     *
     * @param leaverRegisterReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo);

    /**
     * 选项删除
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/option/delete")
    AjaxUtil<Map<String, Object>> optionDelete(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId, @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId, @RequestParam("username") String username);

    /**
     * 选项内容更新
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @param optionContent           选项内容
     * @return true or false
     */
    @PostMapping("/api/register/leaver/option/update")
    AjaxUtil<Map<String, Object>> optionUpdate(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId, @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId, @RequestParam("optionContent") String optionContent, @RequestParam("username") String username);

    /**
     * 更新
     *
     * @param leaverRegisterReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo);

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/release/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId, @RequestParam("username") String username);

    /**
     * 保存
     *
     * @param leaverRegisterDataVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/save")
    AjaxUtil<Map<String, Object>> dataSave(@RequestBody LeaverRegisterDataVo leaverRegisterDataVo);

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/delete")
    AjaxUtil<Map<String, Object>> dataDelete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId, @RequestParam("username") String username);

    /**
     * 删除登记
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    @PostMapping("/api/register/leaver/data/list/delete")
    AjaxUtil<Map<String, Object>> dataListDelete(@RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId, @RequestParam("leaverRegisterDataId") String leaverRegisterDataId, @RequestParam("username") String username);

    /**
     * 统计列表数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @PostMapping("/api/register/leaver/data/list")
    AjaxUtil<LeaverRegisterDataBean> dataList(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 统计列表数据导出
     *
     * @param simplePaginationUtil 请求
     */
    @PostMapping("/api/register/leaver/data/export")
    List<LeaverRegisterDataBean> export(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 获取选项数据
     *
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/options_leaver_register_release_id/{leaverRegisterReleaseId}")
    List<LeaverRegisterOption> leaverRegisterOptions(@PathVariable("leaverRegisterReleaseId") String leaverRegisterReleaseId);

    /**
     * 获取数据域
     *
     * @param leaverRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/leaver/scopes_leaver_register_release_id/{leaverRegisterReleaseId}")
    List<LeaverRegisterScopeBean> leaverRegisterScopes(@PathVariable("leaverRegisterReleaseId") String leaverRegisterReleaseId);
}
