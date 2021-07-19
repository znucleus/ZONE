package top.zbeboy.zone.service.platform;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.pojos.Application;
import top.zbeboy.zbase.feign.platform.RoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private RoleService roleService;

    @Cacheable(cacheNames = CacheBook.MENU, key = "#username")
    @Override
    public String getMenu(List<String> roles, String username) {
        StringBuilder sb = new StringBuilder();
        Optional<List<Application>> optionalApplications = roleService.findInRoleEnNamesAndApplicationPidRelation(roles, "0");
        if (optionalApplications.isPresent()) {
            List<Application> applications = optionalApplications.get();
            for (Application application : applications) {
                Optional<List<Application>> optionalApplicationsList = roleService.findInRoleEnNamesAndApplicationPidRelation(roles, application.getApplicationId());
                if (optionalApplicationsList.isPresent()) {
                    List<Application> childApplications = optionalApplicationsList.get();
                    sb.append("<a href=\"").append(baseUrl(application.getApplicationUrl())).append("\" class=\"br-menu-link\">");
                    sb.append("<div class=\"br-menu-item\">");
                    sb.append("<i class=\"menu-item-icon ").append(application.getIcon()).append(" tx-20\"></i>");
                    sb.append("<span class=\"menu-item-label\">").append(application.getApplicationName()).append("</span>");
                    sb.append("<i class=\"menu-item-arrow fa fa-angle-down\"></i>");
                    sb.append("</div>");
                    sb.append("</a>");
                    sb.append("<ul class=\"br-menu-sub nav flex-column\">");
                    for (Application childApplication : childApplications) {
                        sb.append("<li class=\"nav-item\"><a href=\"").append(baseUrl(childApplication.getApplicationUrl())).append("\" class=\"nav-link dy_href\">").append(childApplication.getApplicationName()).append("</a></li>");
                    }
                    sb.append("</ul>");
                } else {
                    sb.append("<a href=\"").append(baseUrl(application.getApplicationUrl())).append("\" class=\"br-menu-link\">");
                    sb.append("<div class=\"br-menu-item\">");
                    sb.append("<i class=\"menu-item-icon ").append(application.getIcon()).append(" tx-20\"></i>");
                    sb.append("<span class=\"menu-item-label\">").append(application.getApplicationName()).append("</span>");
                    sb.append("</div>");
                    sb.append("</a>");
                }
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
