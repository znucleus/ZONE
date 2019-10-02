package top.zbeboy.zone.web.vo.platform.user;

import top.zbeboy.zone.config.Workbook;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ResetPasswordVo {
    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号64位字符")
    @Pattern(regexp = Workbook.USERNAME_REGEX, message = "账号不正确")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(max = 20, message = "密码20位字符")
    @Pattern(regexp = Workbook.PASSWORD_REGEX, message = "密码不正确")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    @Size(max = 20, message = "确认密码20位字符")
    @Pattern(regexp = Workbook.PASSWORD_REGEX, message = "确认密码不正确")
    private String okPassword;
    @NotNull(message = "验证方式不能为空")
    private int verificationMode;
    private String passwordResetKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getVerificationMode() {
        return verificationMode;
    }

    public void setVerificationMode(int verificationMode) {
        this.verificationMode = verificationMode;
    }

    public String getPasswordResetKey() {
        return passwordResetKey;
    }

    public void setPasswordResetKey(String passwordResetKey) {
        this.passwordResetKey = passwordResetKey;
    }
}
