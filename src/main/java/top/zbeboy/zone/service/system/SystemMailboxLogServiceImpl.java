package top.zbeboy.zone.service.system;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.SystemMailboxLogDao;
import top.zbeboy.zone.domain.tables.pojos.SystemMailboxLog;

import javax.annotation.Resource;

@Service("systemMailboxLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemMailboxLogServiceImpl implements SystemMailboxLogService {

    @Resource
    private SystemMailboxLogDao systemMailboxLogDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(SystemMailboxLog systemMailboxLog) {
        systemMailboxLogDao.insert(systemMailboxLog);
    }
}
