package top.zbeboy.zone.service.training;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFile;
import top.zbeboy.zone.web.vo.training.special.TrainingSpecialFileSearchVo;

import java.util.List;
import java.util.Optional;

public interface TrainingSpecialFileService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    TrainingSpecialFile findById(String id);

    /**
     * 根据主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

    /**
     * 通过文件id查询
     *
     * @param fileId 文件id
     * @return 数据
     */
    List<TrainingSpecialFile> findByFileId(String fileId);

    /**
     * 根据条件查询全部
     *
     * @param trainingSpecialFileSearchVo 搜索条件
     * @return 数据
     */
    Result<Record> findAllByCondition(TrainingSpecialFileSearchVo trainingSpecialFileSearchVo);

    /**
     * 保存
     *
     * @param trainingSpecialFile 数据
     */
    void save(TrainingSpecialFile trainingSpecialFile);

    /**
     * 更新
     *
     * @param trainingSpecialFile 数据
     */
    void update(TrainingSpecialFile trainingSpecialFile);

    /**
     * 更新下载量
     *
     * @param id 主键
     */
    void updateDownloads(String id);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
