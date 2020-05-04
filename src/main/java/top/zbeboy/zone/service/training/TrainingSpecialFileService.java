package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;

public interface TrainingSpecialFileService {

    /**
     * 根据文件类型id关联查询
     *
     * @param fileTypeId 文件类型id
     * @return 数据
     */
    Result<Record> findByFileTypeId(String fileTypeId);

    /**
     * 根据文件类型id及映射关联查询
     *
     * @param fileTypeId 文件类型id
     * @param mapping    映射
     * @return 数据
     */
    Result<Record> findByFileTypeIdAndMapping(String fileTypeId, Byte mapping);
}
