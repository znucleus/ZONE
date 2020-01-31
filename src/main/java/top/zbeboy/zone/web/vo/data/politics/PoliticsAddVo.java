package top.zbeboy.zone.web.vo.data.politics;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PoliticsAddVo {
    @NotBlank(message = "政治面貌不能为空")
    @Size(max = 30, message = "政治面貌30个字符以内")
    private String politicalLandscapeName;

    public String getPoliticalLandscapeName() {
        return politicalLandscapeName;
    }

    public void setPoliticalLandscapeName(String politicalLandscapeName) {
        this.politicalLandscapeName = politicalLandscapeName;
    }
}
