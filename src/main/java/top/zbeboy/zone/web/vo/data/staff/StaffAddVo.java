package top.zbeboy.zone.web.vo.data.staff;

import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;

import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;

public class StaffAddVo {
    @NotNull(message = "学校不能为空")
    @Min(value = 1, message = "学校不正确")
    private int schoolId;
    @NotNull(message = "院不能为空")
    @Min(value = 1, message = "院不正确")
    private int collegeId;
    @NotNull(message = "系不能为空")
    @Min(value = 1, message = "系不正确")
    private int departmentId;
    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号64位字符")
    @Pattern(regexp = Workbook.USERNAME_REGEX, message = "账号不正确")
    private String username;
    @NotBlank(message = "邮箱不能为空")
    @Size(max = 100, message = "邮箱64位字符")
    @Pattern(regexp = SystemMailConfig.MAIL_REGEX, message = "邮箱不正确")
    private String email;
    @NotBlank(message = "姓名不能为空")
    @Size(max = 30, message = "姓名30位字符")
    private String realName;
    @NotBlank(message = "工号不能为空")
    @Size(max = 20, message = "工号20位字符")
    @Pattern(regexp = Workbook.STAFF_NUM_REGEX, message = "工号不正确")
    private String staffNumber;
    @NotBlank(message = "手机号不能为空")
    @Size(max = 15, message = "手机号15位字符")
    @Pattern(regexp = SystemMobileConfig.MOBILE_REGEX, message = "手机号不正确")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    @Size(max = 6, message = "验证码6位字符")
    @Pattern(regexp = Workbook.VERIFICATION_CODE_REGEX, message = "验证码不正确")
    private String verificationCode;
    @NotBlank(message = "密码不能为空")
    @Size(max = 20, message = "密码20位字符")
    @Pattern(regexp = Workbook.PASSWORD_REGEX, message = "密码不正确")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    @Size(max = 20, message = "确认密码20位字符")
    @Pattern(regexp = Workbook.PASSWORD_REGEX, message = "确认密码不正确")
    private String okPassword;
    @NotNull(message = "未同意入站协议")
    private Byte agreeProtocol;

    // 组装数据提交到事务后台保存
    private Byte enabled;
    private Byte accountNonExpired;
    private Byte credentialsNonExpired;
    private Byte accountNonLocked;
    private int usersTypeId;
    private String avatar;
    private String mailboxVerifyCode;
    private Timestamp mailboxVerifyValid;
    private String langKey;
    private Date joinDate;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOkPassword() {
        return okPassword;
    }

    public void setOkPassword(String okPassword) {
        this.okPassword = okPassword;
    }

    public Byte getAgreeProtocol() {
        return agreeProtocol;
    }

    public void setAgreeProtocol(Byte agreeProtocol) {
        this.agreeProtocol = agreeProtocol;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public Byte getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Byte accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Byte getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Byte credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Byte getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Byte accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getUsersTypeId() {
        return usersTypeId;
    }

    public void setUsersTypeId(int usersTypeId) {
        this.usersTypeId = usersTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMailboxVerifyCode() {
        return mailboxVerifyCode;
    }

    public void setMailboxVerifyCode(String mailboxVerifyCode) {
        this.mailboxVerifyCode = mailboxVerifyCode;
    }

    public Timestamp getMailboxVerifyValid() {
        return mailboxVerifyValid;
    }

    public void setMailboxVerifyValid(Timestamp mailboxVerifyValid) {
        this.mailboxVerifyValid = mailboxVerifyValid;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
