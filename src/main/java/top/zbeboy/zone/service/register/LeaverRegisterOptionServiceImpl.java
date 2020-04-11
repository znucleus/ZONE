package top.zbeboy.zone.service.register;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.LeaverRegisterOptionDao;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;

import javax.annotation.Resource;
import java.util.List;

@Service("leaverRegisterOptionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterOptionServiceImpl implements LeaverRegisterOptionService {

    private final DSLContext create;

    @Resource
    private LeaverRegisterOptionDao leaverRegisterOptionDao;

    @Autowired
    LeaverRegisterOptionServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<LeaverRegisterOption> leaverRegisterOptions) {
        leaverRegisterOptionDao.insert(leaverRegisterOptions);
    }
}
