package top.zbeboy.zone.service.system;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.SystemOperatorLogDao;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;

import javax.annotation.Resource;

@Service("systemOperatorLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemOperatorLogServiceImpl implements SystemOperatorLogService {

    @Resource
    private SystemOperatorLogDao systemOperatorLogDao;

    @Async
    @Override
    public void save(SystemOperatorLog systemOperatorLog) {
        systemOperatorLogDao.insert(systemOperatorLog);
    }
}
