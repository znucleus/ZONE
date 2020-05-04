package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainingSpecialEditVo {
    @NotBlank(message = "实训专题ID不能为空")
    @Size(max = 64, message = "实训专题IID不正确")
    private String    trainingSpecialId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题200个字符以内")
    private String title;
    private String file;
    private String fileName;

    public String getTrainingSpecialId() {
        return trainingSpecialId;
    }

    public void setTrainingSpecialId(String trainingSpecialId) {
        this.trainingSpecialId = trainingSpecialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
