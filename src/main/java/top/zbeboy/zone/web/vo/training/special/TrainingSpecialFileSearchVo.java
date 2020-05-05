package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainingSpecialFileSearchVo {
    @NotBlank(message = "文件类型ID不能为空")
    @Size(max = 64, message = "文件类型ID不正确")
    private String fileTypeId;
    private String originalFileName;
    private Byte mapping;

    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Byte getMapping() {
        return mapping;
    }

    public void setMapping(Byte mapping) {
        this.mapping = mapping;
    }
}
