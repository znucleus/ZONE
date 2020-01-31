package top.zbeboy.zone.service.platform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.OauthClientDetailsDao;
import top.zbeboy.zone.domain.tables.pojos.OauthClientDetails;

import javax.annotation.Resource;

@Service("oauthClientDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {

    @Resource
    private OauthClientDetailsDao oauthClientDetailsDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(OauthClientDetails oauthClientDetails) {
        oauthClientDetailsDao.insert(oauthClientDetails);
    }

    @Override
    public void update(OauthClientDetails oauthClientDetails) {
        oauthClientDetailsDao.update(oauthClientDetails);
    }

    @Override
    public void deleteById(String id) {
        oauthClientDetailsDao.deleteById(id);
    }
}
