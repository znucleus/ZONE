package top.zbeboy.zone.web.vo.platform.user;

import javax.validation.constraints.NotBlank;

public class UsersProfileVo {
    @NotBlank(message = "参数NAME不能为空")
    private String name;
    @NotBlank(message = "参数VALUE不能为空")
    private String value;

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
}
