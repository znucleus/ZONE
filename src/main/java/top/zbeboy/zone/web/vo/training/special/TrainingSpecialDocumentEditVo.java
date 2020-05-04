package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainingSpecialDocumentEditVo {
    @NotBlank(message = "实训专题文章ID不能为空")
    @Size(max = 64, message = "实训专题文章ID不正确")
    private String trainingSpecialDocumentId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题200个字符以内")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;

    public String getTrainingSpecialDocumentId() {
        return trainingSpecialDocumentId;
    }

    public void setTrainingSpecialDocumentId(String trainingSpecialDocumentId) {
        this.trainingSpecialDocumentId = trainingSpecialDocumentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
