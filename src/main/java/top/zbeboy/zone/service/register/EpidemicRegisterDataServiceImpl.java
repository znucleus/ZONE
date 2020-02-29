package top.zbeboy.zone.service.register;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.daos.EpidemicRegisterDataDao;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;

import javax.annotation.Resource;

@Service("epidemicRegisterDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EpidemicRegisterDataServiceImpl implements EpidemicRegisterDataService {

    @Resource
    private EpidemicRegisterDataDao epidemicRegisterDataDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(EpidemicRegisterData epidemicRegisterData) {
        epidemicRegisterDataDao.insert(epidemicRegisterData);
    }
}
