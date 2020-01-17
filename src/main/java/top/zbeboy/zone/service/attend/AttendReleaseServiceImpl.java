package top.zbeboy.zone.service.attend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendReleaseDao;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;

import javax.annotation.Resource;

@Service("attendReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendReleaseServiceImpl implements AttendReleaseService{

    @Resource
    private AttendReleaseDao attendReleaseDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendRelease attendRelease) {
        attendReleaseDao.insert(attendRelease);
    }
}
