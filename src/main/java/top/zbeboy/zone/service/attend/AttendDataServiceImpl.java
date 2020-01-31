package top.zbeboy.zone.service.attend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendDataDao;
import top.zbeboy.zone.domain.tables.pojos.AttendData;

import javax.annotation.Resource;

@Service("attendDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendDataServiceImpl implements AttendDataService {

    @Resource
    private AttendDataDao attendDataDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendData attendData) {
        attendDataDao.insert(attendData);
    }
}
