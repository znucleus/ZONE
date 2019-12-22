package top.zbeboy.zone.service.platform;

import java.util.List;

public interface MenuService {

    /**
     * 获取角色菜单
     * 缓存:是
     *
     * @param roles    角色
     * @param username 账号
     * @return 菜单html
     */
    String getMenu(List<String> roles, String username);
}
