package top.zbeboy.zone.service.platform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AuthorizeTypeDao;
import top.zbeboy.zone.domain.tables.pojos.AuthorizeType;

import javax.annotation.Resource;
import java.util.List;

@Service("authorizeTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AuthorizeTypeServiceImpl implements AuthorizeTypeService {

    @Resource
    private AuthorizeTypeDao authorizeTypeDao;

    @Override
    public List<AuthorizeType> findAll() {
        return authorizeTypeDao.findAll();
    }
}
