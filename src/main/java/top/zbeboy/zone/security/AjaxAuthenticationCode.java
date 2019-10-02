package top.zbeboy.zone.security;

/**
 * Created by lenovo on 2016-09-04.
 * 安全登录错误码
 */
public class AjaxAuthenticationCode {

    /*
    权限异常
     */
    static final int AU_ERROR_CODE = 1;

    /*
    全部正确
     */
    static final int OK_CODE = 2;

    /*
    用户所在院校或班级被注销或本人学生，教职工信息不存在
     */
    public static final int SCHOOL_IS_DEL_CODE = 3;

    /*
    用户不存在
    */
    public static final int USERNAME_IS_NOT_EXIST_CODE = 4;

    /*
    账号/邮箱/手机号为空
   */
    public static final int USERNAME_IS_BLANK = 5;

    /*
   密码为空
    */
    public static final int PASSWORD_IS_BLANK = 6;

    /*
    邮箱未验证
     */
    public static final int EMAIL_IS_NOT_VALID = 7;

    /*
    账号已被注销
     */
    public static final int USERNAME_IS_ENABLES = 8;

    /*
   账号过期
    */
    public static final int USERNAME_ACCOUNT_NON_EXPIRED = 9;

    /*
    账号是否凭证过期
     */
    public static final int USERNAME_CREDENTIALS_NON_EXPIRED = 10;

    /*
    账号是否被锁
     */
    public static final int USERNAME_ACCOUNT_NON_LOCKED = 11;

    /*
    初始化服务异常
     */
    public static final int INIT_SERVE_ERROR = 12;

    /*
    用户类型是否为空
     */
    public static final int USER_TYPE_IS_BLANK = 13;
}
