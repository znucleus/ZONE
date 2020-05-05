package top.zbeboy.zone.web.vo.training.special;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingSpecialFileMappingVo {
    @NotBlank(message = "文件ID不能为空")
    @Size(max = 64, message = "文件ID不正确")
    private String fileId;
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
    @NotBlank(message = "文件原名不能为空")
    @Size(max = 300, message = "文件原名300个字符以内")
    private String originalFileName;
    @NotBlank(message = "文件新名不能为空")
    @Size(max = 300, message = "文件新名300个字符以内")
    private String newName;
    @NotBlank(message = "文件后缀不能为空")
    @Size(max = 20, message = "文件后缀20个字符以内")
    private String ext;
    @NotBlank(message = "相对路径不能为空")
    @Size(max = 800, message = "相对路径800个字符以内")
    private String relativePath;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
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

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
