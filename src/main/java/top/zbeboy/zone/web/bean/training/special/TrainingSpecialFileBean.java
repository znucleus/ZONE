package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFile;

public class TrainingSpecialFileBean extends TrainingSpecialFile {
    private String originalFileName;
    private String ext;
    private Long fileSize;
    private Byte canOperator;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }
}
