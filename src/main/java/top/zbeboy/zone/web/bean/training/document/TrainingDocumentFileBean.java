package top.zbeboy.zone.web.bean.training.document;

import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentFile;

public class TrainingDocumentFileBean extends TrainingDocumentFile {
    private String createDateStr;
    private Byte canOperator;
    private String originalFileName;
    private String ext;
    private Long fileSize;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }

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
}
