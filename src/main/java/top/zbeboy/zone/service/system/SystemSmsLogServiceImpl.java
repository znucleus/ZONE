package top.zbeboy.zone.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.SystemSmsLogDao;
import top.zbeboy.zone.domain.tables.pojos.SystemSmsLog;

import javax.annotation.Resource;

@Service("systemSmsLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemSmsLogServiceImpl implements SystemSmsLogService {

    @Resource
    private SystemSmsLogDao systemSmsLogDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(SystemSmsLog systemSmsLog) {
        systemSmsLogDao.insert(systemSmsLog);
    }
}
