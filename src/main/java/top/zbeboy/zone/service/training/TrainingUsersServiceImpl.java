package top.zbeboy.zone.service.training;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingUsersDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingUsers;

import javax.annotation.Resource;
import java.util.List;

@Service("trainingUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingUsersServiceImpl implements TrainingUsersService{

    @Resource
    private TrainingUsersDao trainingUsersDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<TrainingUsers> trainingUsers) {
        trainingUsersDao.insert(trainingUsers);
    }
}
