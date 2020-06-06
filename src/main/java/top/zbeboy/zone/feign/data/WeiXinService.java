package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.hystrix.data.WeiXinHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.weixin.WeiXinBean;

@FeignClient(value = "base-server", fallback = WeiXinHystrixClientFallbackFactory.class)
public interface WeiXinService {

    /**
     * 获取微信信息
     *
     * @param username 账号
     * @param appId    id
     * @return 数据
     */
    @GetMapping("/base/data/wei_xin/username/app_id/{username}/{appId}")
    WeiXin findByUsernameAndAppId(@PathVariable("username") String username, @PathVariable("appId") String appId);

    /**
     * 获取微信信息
     *
     * @param studentId 学生账号
     * @param appId     id
     * @return 数据
     */
    @GetMapping("/base/data/wei_xin/student_id/app_id/{studentId}/{appId}")
    WeiXinBean findByStudentIdAndAppId(@PathVariable("studentId") int studentId, @PathVariable("appId") String appId);

    /**
     * 保存
     *
     * @param weiXin 数据
     */
    @PostMapping("/base/data/wei_xin/save")
    void save(@RequestBody WeiXin weiXin);

    /**
     * 更新
     *
     * @param weiXin 数据
     */
    @PostMapping("/base/data/wei_xin/update")
    void update(@RequestBody WeiXin weiXin);
}
