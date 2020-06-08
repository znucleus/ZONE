package top.zbeboy.zone.feign.platform;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.hystrix.platform.UsersTypeHystrixClientFallbackFactory;

@FeignClient(value = "base-server", fallback = UsersTypeHystrixClientFallbackFactory.class)
public interface UsersTypeService {

    /**
     * 根据用户类型ID查询
     *
     * @param id 类型ID
     * @return 用户类型
     */
    @GetMapping("/base/platform/users_type/{id}")
    UsersType findById(@PathVariable("id") int id);

    /**
     * 根据用户类型查询
     *
     * @param usersTypeName 类型名
     * @return 用户类型
     */
    @GetMapping("/base/platform/users_type_name/{usersTypeName}")
    UsersType findByUsersTypeName(@PathVariable("usersTypeName") String usersTypeName);
}
