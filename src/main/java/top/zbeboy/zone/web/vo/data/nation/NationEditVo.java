package top.zbeboy.zone.web.vo.data.nation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NationEditVo {
    @NotNull(message = "民族ID不能为空")
    @Min(value = 1, message = "民族ID不正确")
    private int nationId;
    @NotBlank(message = "民族不能为空")
    @Size(max = 30, message = "民族30个字符以内")
    private String nationName;

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }
}
