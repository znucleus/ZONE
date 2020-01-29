package top.zbeboy.zone.web.vo.data.nation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NationAddVo {
    @NotBlank(message = "民族不能为空")
    @Size(max = 30,message = "民族30个字符以内")
    private String nationName;

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }
}
