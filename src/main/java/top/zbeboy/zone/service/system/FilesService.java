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
     * delete faile.
     *
     * @param files data.
     */
    void delete(Files files);
}
