package top.zbeboy.zone.service.platform;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.pojos.Application;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private RoleService roleService;

    @Cacheable(cacheNames = CacheBook.MENU, key = "#username")
    @Override
    public String getMenu(List<String> roles, String username) {
        StringBuilder sb = new StringBuilder();
        List<Application> applications = roleService.findInRoleEnNamesAndApplicationPidRelation(roles, "0");
        for (Application application : applications) {
            List<Application> childApplications = roleService.findInRoleEnNamesAndApplicationPidRelation(roles, application.getApplicationId());
            if (Objects.nonNull(childApplications) && !childApplications.isEmpty()) {
                sb.append("<li class=\"has-submenu\">");
                sb.append("<a href=\"").append(baseUrl(application.getApplicationUrl())).append("\">");
                sb.append("<i class=\"").append(application.getIcon()).append("\"></i>").append("<span>").append(application.getApplicationName()).append("</span>");
                sb.append("</a>");
                if (childApplications.size() < 8) {
                    sb.append("<ul class=\"submenu\">");
                    for (Application childApplication : childApplications) {
                        sb.append("<li><a href=\"").append(baseUrl(childApplication.getApplicationUrl())).append("\" class=\"dy_href\">").append(childApplication.getApplicationName()).append("</a></li>");
                    }
                    sb.append("</ul>");
                } else {
                    sb.append("<ul class=\"submenu megamenu\">");
                    int k = 0;
                    for (int i = 0; i < childApplications.size(); i += 7) {
                        sb.append("<li><ul>");
                        for (int j = 0; j < 8; j++) {
                            if (k < childApplications.size()) {
                                sb.append("<li><a href=\"").append(baseUrl(childApplications.get(k).getApplicationUrl())).append("\" class=\"dy_href\">").append(childApplications.get(k).getApplicationName()).append("</a></li>");
                            }
                            k++;
                        }
                        sb.append("</ul></li>");
                    }
                    sb.append("</ul>");
                }
                sb.append("</li>");
            } else {
                sb.append("<li>");
                sb.append("<a href=\"").append(baseUrl(application.getApplicationUrl())).append("\" class=\"dy_href\">");
                sb.append("<i class=\"").append(application.getIcon()).append("\"></i>").append("<span>").append(application.getApplicationName()).append("</span>");
                sb.append("</a>");
                sb.append("</li>");
            }

        }
        return sb.toString();
    }

    private String baseUrl(String url) {
        String result;
        if (url.equals("#")) {
            result = "javascript:;";
        } else {
            result = "#" + url;
        }
        return result;
    }
}
