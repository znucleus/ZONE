package top.zbeboy.zone.service.util;

import top.zbeboy.zone.config.Workbook;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    /**
     * 根据开发环境不同取不同路径
     *
     * @param request 请求
     * @return 路径
     */

    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * 获取客服端ip地址
     *
     * @param request 请求
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取realPath
     *
     * @param request 请求
     * @return real path.
     */
    public static String getRealPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath(Workbook.DIRECTORY_SPLIT) + Workbook.DIRECTORY_SPLIT;
    }
}
