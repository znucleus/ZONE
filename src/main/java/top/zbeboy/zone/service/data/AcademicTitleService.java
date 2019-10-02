package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;

import java.util.List;

public interface AcademicTitleService {

    /**
     * 查询全部
     *
     * @return 全部数据
     */
    List<AcademicTitle> findAll();
}
