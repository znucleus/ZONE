package top.zbeboy.zone.web.vo.plugins;

import javax.validation.constraints.NotBlank;

public class XeditableVo {
    @NotBlank(message = "参数NAME不能为空")
    private String name;
    @NotBlank(message = "参数VALUE不能为空")
    private String value;
    @NotBlank(message = "参数PK不能为空")
    private String pk;

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

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
