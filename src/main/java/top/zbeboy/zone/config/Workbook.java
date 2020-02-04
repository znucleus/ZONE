package top.zbeboy.zone.config;

import top.zbeboy.zone.domain.tables.pojos.Users;

/**
 * Application constants.
 * 开发环境配置常量
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
public final class Workbook {

    /*
    开发环境
     */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /*
    生产环境
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /*
    注册类型
     */
    public static final String REGISTER_STUDENT = "student";
    public static final String REGISTER_STAFF = "staff";

    /*
    静态配置参数
     */
    public enum SystemConfigure {
        MAIL_SWITCH, MOBILE_SWITCH, FORBIDDEN_REGISTER, STATIC_RESOURCES_VERSION
    }

    /*
    目录分隔符
    */
    public static final String DIRECTORY_SPLIT = "/";

    /*
    后台url
     */
    public static final String WEB_BACKSTAGE = "/web/menu/backstage";
    /*
    oauth 授权url
     */
    public static final String OAUTH_AUTHORIZE = "/oauth/authorize";

    /*
    用户默认头像
     */
    public static final String USERS_AVATAR = "1000";

    /*
    用户类型参数
    */
    public static final String SYSTEM_USERS_TYPE = "系统";
    public static final String STUDENT_USERS_TYPE = "学生";
    public static final String STAFF_USERS_TYPE = "教职工";

    /*
    角色类型参数
     */
    public enum authorities {
        ROLE_SYSTEM, ROLE_ACTUATOR, ROLE_ADMIN
    }

    /*
    角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /*
    正则
    */
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{1,20}$";
    public static final String PASSWORD_REGEX = "^[a-zA-Z0-9]\\w{5,17}$";
    public static final String STUDENT_NUM_REGEX = "^[0-9]{13}$";
    public static final String STAFF_NUM_REGEX = "^[0-9]+$";
    public static final String VERIFICATION_CODE_REGEX = "^[0-9]{6}$";
    public static final String ID_CARD_REGEX = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    public static final String DORMITORY_NUMBER_REGEX = "^\\d{2}-\\d{3}$";
    public static final String YEAR_REGEX = "^\\d{4}$";

    /*
    通知类型参数
     */
    public enum notifyType {
        success, info, warning, danger
    }

    /*
    系统setting目录
    */
    public static final String SETTINGS_PATH = "." + DIRECTORY_SPLIT + "settings" + DIRECTORY_SPLIT;

    /*
    用户文档路径
    */
    public static final String USERS_PORTFOLIOS = "portfolios" + DIRECTORY_SPLIT;

    /*
    文件档案路径
    */
    public static final String FILES_PORTFOLIOS = "files" + DIRECTORY_SPLIT;

    /*
    临时文件档案路径
    */
    public static final String TEMP_FILES_PORTFOLIOS = "files" + DIRECTORY_SPLIT + "temp";

    /*
    实习日志模板
    */
    public static final String INTERNSHIP_JOURNAL_FILE_PATH = SETTINGS_PATH + "internshipJournalTemplate.docx";

    /*
    image suffix.
    */
    public enum imageSuffix {
        JPG, JPEG, BMP, GIF, PNG
    }

    /**
     * 头像路径
     *
     * @param users 用户
     * @return 路径
     */
    public static String avatarPath(Users users) {
        return USERS_PORTFOLIOS + users.getUsername() + DIRECTORY_SPLIT + "avatar" + DIRECTORY_SPLIT;
    }

    /**
     * 实习文件路径
     *
     * @return 路径
     */
    public static String internshipFilePath() {
        return FILES_PORTFOLIOS + "internship" + DIRECTORY_SPLIT;
    }
}
