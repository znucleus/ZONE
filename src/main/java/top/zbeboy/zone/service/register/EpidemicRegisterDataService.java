package top.zbeboy.zone.service.register;

import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;

public interface EpidemicRegisterDataService {

    /**
     * 保存
     *
     * @param epidemicRegisterData 数据
     */
    void save(EpidemicRegisterData epidemicRegisterData);
}
