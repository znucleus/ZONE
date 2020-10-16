package top.zbeboy.zone.filter;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.zbeboy.zbase.bean.data.department.DepartmentBean;
import top.zbeboy.zbase.bean.data.organize.OrganizeBean;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.DepartmentService;
import top.zbeboy.zbase.feign.data.OrganizeService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.security.AjaxAuthenticationCode;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SpringBootUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2016-09-03.
 * 安全登录配置
 */
public class SecurityLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().endsWith("/login")) {
            String username = StringUtils.deleteWhitespace(request.getParameter("username"));
            String password = StringUtils.deleteWhitespace(request.getParameter("password"));

            if (StringUtils.isBlank(username)) {// 账号/邮箱/手机号为空
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_IS_BLANK);
                return;
            }

            if (StringUtils.isBlank(password)) {// 密码为空
                response.getWriter().print(AjaxAuthenticationCode.PASSWORD_IS_BLANK);
                return;
            }

            ServletContext context = request.getSession().getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils
                    .getWebApplicationContext(context);
            if (Objects.isNull(ctx)) {
                response.getWriter().print(AjaxAuthenticationCode.INIT_SERVE_ERROR);
                return;
            }

            UsersService usersService = SpringBootUtil.getBean(UsersService.class);

            Users users = null;
            boolean hasUser = false;
            if (Pattern.matches(Workbook.MAIL_REGEX, username)) {
                users = usersService.findByEmail(username);
                hasUser = Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername());
            }

            if (!hasUser && Pattern.matches(Workbook.MOBILE_REGEX, username)) {
                users = usersService.findByMobile(username);
                hasUser = Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername());
            }

            if (!hasUser) {
                users = usersService.findByUsername(username);
                hasUser = Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername());
            }

            if (!hasUser) {
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_IS_NOT_EXIST_CODE);
                return;
            }

            if (Objects.isNull(users.getEnabled()) || !BooleanUtil.toBoolean(users.getEnabled())) {// 用户是否已被注销
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_IS_ENABLES);
                return;
            }

            if (Objects.isNull(users.getAccountNonExpired()) || !BooleanUtil.toBoolean(users.getAccountNonExpired())) {// 用户是否账号过期
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_ACCOUNT_NON_EXPIRED);
                return;
            }

            if (Objects.isNull(users.getCredentialsNonExpired()) || !BooleanUtil.toBoolean(users.getCredentialsNonExpired())) {// 用户是否凭证过期
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_CREDENTIALS_NON_EXPIRED);
                return;
            }

            if (Objects.isNull(users.getAccountNonLocked()) || !BooleanUtil.toBoolean(users.getAccountNonLocked())) {// 用户是否被锁
                response.getWriter().print(AjaxAuthenticationCode.USERNAME_ACCOUNT_NON_LOCKED);
                return;
            }

            if (Objects.isNull(users.getVerifyMailbox()) || !BooleanUtil.toBoolean(users.getVerifyMailbox())) {// 用户是否验证邮箱
                response.getWriter().print(AjaxAuthenticationCode.EMAIL_IS_NOT_VALID);
                return;
            }

            UsersTypeService usersTypeService = SpringBootUtil.getBean(UsersTypeService.class);
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.isNull(usersType) || Objects.isNull(usersType.getUsersTypeId()) || usersType.getUsersTypeId() <= 0) {
                response.getWriter().print(AjaxAuthenticationCode.USER_TYPE_IS_BLANK);
                return;
            }

            boolean schoolIsNotDel = false;
            switch (usersType.getUsersTypeName()) {
                case Workbook.STUDENT_USERS_TYPE:
                    StudentService studentService = SpringBootUtil.getBean(StudentService.class);
                    StudentBean studentBean = studentService.findByUsername(users.getUsername());
                    if (Objects.nonNull(studentBean) && Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        OrganizeService organizeService = SpringBootUtil.getBean(OrganizeService.class);
                        OrganizeBean organizeBean = organizeService.findByIdRelation(studentBean.getOrganizeId());
                        if (Objects.nonNull(organizeBean) && Objects.nonNull(organizeBean.getOrganizeId()) && organizeBean.getOrganizeId() > 0) {
                            schoolIsNotDel = !BooleanUtil.toBoolean(organizeBean.getSchoolIsDel()) && !BooleanUtil.toBoolean(organizeBean.getCollegeIsDel()) &&
                                    !BooleanUtil.toBoolean(organizeBean.getDepartmentIsDel()) && !BooleanUtil.toBoolean(organizeBean.getScienceIsDel()) &&
                                    !BooleanUtil.toBoolean(organizeBean.getGradeIsDel()) && !BooleanUtil.toBoolean(organizeBean.getOrganizeIsDel());
                        }
                    }
                    break;
                case Workbook.STAFF_USERS_TYPE:

                    StaffService staffService = SpringBootUtil.getBean(StaffService.class);
                    StaffBean staffBean = staffService.findByUsername(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        DepartmentService departmentService = SpringBootUtil.getBean(DepartmentService.class);
                        DepartmentBean departmentBean = departmentService.findByIdRelation(staffBean.getDepartmentId());
                        if (Objects.nonNull(departmentBean) && Objects.nonNull(departmentBean.getDepartmentId()) && departmentBean.getDepartmentId() > 0) {
                            schoolIsNotDel = !BooleanUtil.toBoolean(departmentBean.getSchoolIsDel()) && !BooleanUtil.toBoolean(departmentBean.getCollegeIsDel()) &&
                                    !BooleanUtil.toBoolean(departmentBean.getDepartmentIsDel());
                        }
                    }
                    break;
                default:
                    schoolIsNotDel = true;
            }

            // 学校已被注销
            if (!schoolIsNotDel) {
                response.getWriter().print(AjaxAuthenticationCode.SCHOOL_IS_DEL_CODE);
                return;
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }


}
