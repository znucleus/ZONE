package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainingSpecialAddVo {
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题200个字符以内")
    private String title;
    private String file;
    private String fileName;

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
