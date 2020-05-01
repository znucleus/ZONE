package top.zbeboy.zone.web.vo.training.document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingDocumentAddVo {
    @NotBlank(message = "实训发布ID不能为空")
    @Size(max = 64, message = "实训发布ID不正确")
    private String trainingReleaseId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题200个字符以内")
    private String documentTitle;
    @NotBlank(message = "内容不能为空")
    private String trainingDocumentContent;
    @NotNull(message = "是否原创不能为空")
    private Byte isOriginal;
    @Size(max = 500, message = "来源500个字符以内")
    private String origin;

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getTrainingDocumentContent() {
        return trainingDocumentContent;
    }

    public void setTrainingDocumentContent(String trainingDocumentContent) {
        this.trainingDocumentContent = trainingDocumentContent;
    }

    public Byte getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(Byte isOriginal) {
        this.isOriginal = isOriginal;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
