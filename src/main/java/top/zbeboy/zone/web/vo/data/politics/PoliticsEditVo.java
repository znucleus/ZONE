package top.zbeboy.zone.web.vo.data.politics;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PoliticsEditVo {
    @NotNull(message = "政治面貌ID不能为空")
    @Min(value = 1,message = "政治面貌ID不正确")
    private int politicalLandscapeId;
    @NotBlank(message = "政治面貌不能为空")
    @Size(max = 30,message = "政治面貌30个字符以内")
    private String politicalLandscapeName;

    public int getPoliticalLandscapeId() {
        return politicalLandscapeId;
    }

    public void setPoliticalLandscapeId(int politicalLandscapeId) {
        this.politicalLandscapeId = politicalLandscapeId;
    }

    public String getPoliticalLandscapeName() {
        return politicalLandscapeName;
    }

    public void setPoliticalLandscapeName(String politicalLandscapeName) {
        this.politicalLandscapeName = politicalLandscapeName;
    }
}
