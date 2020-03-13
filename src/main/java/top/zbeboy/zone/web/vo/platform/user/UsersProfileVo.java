package top.zbeboy.zone.web.vo.platform.user;

import javax.validation.constraints.NotBlank;

public class UsersProfileVo {
    @NotBlank(message = "参数NAME不能为空")
    private String name;
    @NotBlank(message = "参数VALUE不能为空")
    private String value;
    private String password;
    private int mode;
    private String dynamicPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getDynamicPassword() {
        return dynamicPassword;
    }

    public void setDynamicPassword(String dynamicPassword) {
        this.dynamicPassword = dynamicPassword;
    }
}
