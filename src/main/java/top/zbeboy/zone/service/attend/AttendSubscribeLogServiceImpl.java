package top.zbeboy.zone.service.attend;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendSubscribeLogDao;
import top.zbeboy.zone.domain.tables.pojos.AttendSubscribeLog;

import javax.annotation.Resource;

@Service("attendSubscribeLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendSubscribeLogServiceImpl implements AttendSubscribeLogService {

    @Resource
    private AttendSubscribeLogDao attendSubscribeLogDao;

    @Async
    @Override
    public void save(AttendSubscribeLog attendSubscribeLog) {
        attendSubscribeLogDao.insert(attendSubscribeLog);
    }
}
