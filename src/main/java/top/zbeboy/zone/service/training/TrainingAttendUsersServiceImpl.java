package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingAttendUsersDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingAttendUsers;

import javax.annotation.Resource;
import java.util.List;

@Service("trainingAttendUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingAttendUsersServiceImpl implements TrainingAttendUsersService {

    private final DSLContext create;

    @Resource
    private TrainingAttendUsersDao trainingAttendUsersDao;

    @Autowired
    TrainingAttendUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<TrainingAttendUsers> trainingAttendUsers) {
        trainingAttendUsersDao.insert(trainingAttendUsers);
    }
}
