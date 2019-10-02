package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.School;

import java.util.List;

public interface SchoolService {

    /**
     * 根据状态查询全部学校
     *
     * @param schoolIsDel 状态
     * @return 全部学校
     */
    List<School> findBySchoolIsDel(Byte schoolIsDel);
}
