package top.zbeboy.zone.service.system;

import top.zbeboy.zone.domain.tables.pojos.Files;

public interface FilesService {

    /**
     * 通过主键查询
     *
     * @param id id
     * @return result
     */
    Files findById(String id);

    /**
     * save file.
     *
     * @param files data.
     */
    void save(Files files);

    /**
     * update file.
     *
     * @param files data.
     */
    void update(Files files);

    /**
     * delete faile.
     *
     * @param files data.
     */
    void delete(Files files);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
