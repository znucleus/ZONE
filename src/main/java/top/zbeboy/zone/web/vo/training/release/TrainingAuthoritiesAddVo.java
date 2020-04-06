package top.zbeboy.zone.web.vo.training.release;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainingAuthoritiesAddVo {
    @NotBlank(message = "实训发布ID不能为空")
    @Size(max = 64, message = "实训发布ID不正确")
    private String trainingReleaseId;
    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号不正确")
    private String username;
    @NotBlank(message = "生效时间不能为空")
    private String validDate;
    @NotBlank(message = "失效时间不能为空")
    private String expireDate;

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
