package top.zbeboy.zone.feign.register;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterRelease;
import top.zbeboy.zone.hystrix.register.RegisterEpidemicHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterDataBean;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterDataAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseEditVo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@FeignClient(value = "api-server", fallback = RegisterEpidemicHystrixClientFallbackFactory.class)
public interface RegisterEpidemicService {

    /**
     * 查询发布数据
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/api/register/epidemic/release/{id}")
    EpidemicRegisterRelease release(@PathVariable("id") String id);

    /**
     * 查询今日登记数据
     *
     * @param username                  用户
     * @param epidemicRegisterReleaseId 发布id
     * @return 数据
     */
    @GetMapping("/api/register/epidemic/data_today_username_and_epidemic_register_release_id/{username}/{epidemicRegisterReleaseId}")
    EpidemicRegisterData findTodayByUsernameAndEpidemicRegisterReleaseId(@PathVariable("username") String username, @PathVariable("epidemicRegisterReleaseId") String epidemicRegisterReleaseId);

    /**
     * 检查是否可操作
     *
     * @param username 用户
     * @return 数据
     */
    @GetMapping("/api/register/epidemic/condition/epidemic_operator/{username}")
    Boolean epidemicOperator(@PathVariable("username") String username);

    /**
     * 检查是否可查看
     *
     * @param username 用户
     * @return 数据
     */
    @GetMapping("/api/register/epidemic/condition/epidemic_review/{username}")
    Boolean epidemicReview(@PathVariable("username") String username);

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @PostMapping("/api/register/epidemic/data")
    AjaxUtil<EpidemicRegisterReleaseBean> data(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 保存
     *
     * @param epidemicRegisterReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/release/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody @Valid EpidemicRegisterReleaseAddVo epidemicRegisterReleaseAddVo);

    /**
     * 更新
     *
     * @param epidemicRegisterReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/release/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody EpidemicRegisterReleaseEditVo epidemicRegisterReleaseEditVo);

    /**
     * 删除
     *
     * @param epidemicRegisterReleaseId id
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/release/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam("epidemicRegisterReleaseId") String epidemicRegisterReleaseId);

    /**
     * 登记
     *
     * @param epidemicRegisterDataAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/data/save")
    AjaxUtil<Map<String, Object>> dataSave(@RequestBody EpidemicRegisterDataAddVo epidemicRegisterDataAddVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/api/register/epidemic/data/list")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 登记删除
     *
     * @param epidemicRegisterDataId 发布id
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/data/delete")
    AjaxUtil<Map<String, Object>> dataDelete(@RequestParam("username") String username, @RequestParam("epidemicRegisterDataId") String epidemicRegisterDataId);

    /**
     * 导出 列表 数据
     *
     * @param dataTablesUtil 请求
     */
    @PostMapping("/api/register/epidemic/data/export")
    List<EpidemicRegisterDataBean> export(@RequestBody DataTablesUtil dataTablesUtil);
}
