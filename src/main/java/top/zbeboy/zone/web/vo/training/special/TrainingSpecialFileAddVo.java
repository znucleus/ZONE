package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingSpecialFileAddVo {
    @NotBlank(message = "文件类型ID不能为空")
    @Size(max = 64, message = "文件类型ID不正确")
    private String fileTypeId;
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
    @NotBlank(message = "文件头不能为空")
    private String contentType;
    @NotBlank(message = "文件原名不能为空")
    @Size(max = 300, message = "文件原名300个字符以内")
    private String originalFileName;
    @NotBlank(message = "文件新名不能为空")
    @Size(max = 300, message = "文件新名300个字符以内")
    private String newName;
    @NotBlank(message = "文件后缀不能为空")
    @Size(max = 20, message = "文件后缀20个字符以内")
    private String ext;

    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
