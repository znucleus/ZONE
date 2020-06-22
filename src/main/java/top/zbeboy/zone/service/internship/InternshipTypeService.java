package top.zbeboy.zone.service.internship;

import top.zbeboy.zbase.domain.tables.pojos.InternshipType;

import java.util.List;

public interface InternshipTypeService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    InternshipType findById(int id);

    /**
     * 查询全部数据
     *
     * @return 数据
     */
    List<InternshipType> findAll();
}
