package top.zbeboy.zone.feign.platform;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.OauthClientUsers;
import top.zbeboy.zone.hystrix.platform.AppHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.app.AppAddVo;
import top.zbeboy.zone.web.vo.platform.app.AppEditVo;

import java.util.Map;

@FeignClient(value = "base-server", fallback = AppHystrixClientFallbackFactory.class)
public interface AppService {

    /**
     * 获取用户信息
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/platform/app/oauth_client_users/{id}")
    OauthClientUsers findOauthClientUsersById(@PathVariable("id") String id);

    /**
     * 获取用户信息
     *
     * @param id 主键
     * @return 数据
     */
    @PostMapping("/base/platform/app/oauth_client_users/id/username/relation")
    OauthClientUsersBean findOauthClientUsersByIdAndUsernameRelation(@RequestParam("id") String id, @RequestParam("username") String username);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/platform/app/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param appAddVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/platform/app/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AppAddVo appAddVo);

    /**
     * 更新
     *
     * @param appEditVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/platform/app/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody AppEditVo appEditVo);

    /**
     * 根据客户端id删除
     *
     * @param username 当前用户账号
     * @param clientId 客户端id
     * @return true or false
     */
    @PostMapping("/base/platform/app/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam("clientId") String clientId);

    /**
     * 备注
     *
     * @param clientId id
     * @param remark   数据
     * @return 备注
     */
    @PostMapping("/base/platform/app/remark")
    AjaxUtil<Map<String, Object>> remark(@RequestParam("username") String username, @RequestParam("clientId") String clientId, @RequestParam(value = "remark", required = false) String remark);
}
